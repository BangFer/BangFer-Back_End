package com.capstone.BnagFer.domain.accounts.service.account;

import com.capstone.BnagFer.domain.accounts.dto.account.*;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.jwt.exception.SecurityCustomException;
import com.capstone.BnagFer.domain.accounts.jwt.exception.TokenErrorCode;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetails;
import com.capstone.BnagFer.global.util.RedisUtil;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountsService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final AccountsCommonService accountsCommonService;

    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {

        // 회원 정보 존재 하는지 확인
        User user = userJpaRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));

        // 소프트 딜리트된 사용자인지 확인
        if (user.getDeleted()) {
            throw new AccountsExceptionHandler(ErrorCode.USER_IS_DELETED);
        }

        // 회원 pw 일치 여부
        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // fcm 토큰 저장
        redisUtil.saveFCMToken(requestDto.email(), requestDto.fcmToken());

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 로그인 성공 시 토큰 생성
        String accessToken = jwtProvider.createJwtAccessToken(customUserDetails);
        String refreshToken = jwtProvider.createJwtRefreshToken(customUserDetails);

        return UserLoginResponseDto.from(user, accessToken, refreshToken);
    }

    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {

        // pw, pw 확인 일치 확인
        if (!requestDto.password().equals(requestDto.passwordCheck()))
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_EQUAL);

        // 이메일 중복 확인
        accountsCommonService.checkUserEmail(requestDto.email());

        String encodedPw = passwordEncoder.encode(requestDto.password());
        User user = requestDto.toEntity(encodedPw);

        return UserSignupResponseDto.from(userJpaRepository.save(user));
    }

    public UserSignupResponseDto socialSignup(UserSignupRequestDto userSignupRequestDto) {
        if (userJpaRepository
                .findByEmailAndProvider(userSignupRequestDto.email(), userSignupRequestDto.provider())
                .isPresent()
        ) throw new AccountsExceptionHandler(ErrorCode.USER_ALREADY_EXIST);
        User user = userJpaRepository.save(userSignupRequestDto.toEntity());
        return UserSignupResponseDto.from(user);
    }

    public void logout(HttpServletRequest request) {
        try {
            String accessToken = jwtProvider.resolveAccessToken(request);

            redisUtil.removeFCMToken(jwtProvider.getUserEmail(accessToken));

            redisUtil.save(
                    accessToken,
                    "logout",
                    jwtProvider.getExpTime(accessToken),
                    TimeUnit.MILLISECONDS
            );

            redisUtil.delete(
                    jwtProvider.getUserEmail(accessToken)
            );
        } catch (ExpiredJwtException e) {
            throw new SecurityCustomException(TokenErrorCode.TOKEN_EXPIRED);
        }
    }

    public void updatePassword(HttpServletRequest request, ChangePwRequestDto requestDto, User user) {

        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (requestDto.password().equals(requestDto.newPassword())) {
            throw new AccountsExceptionHandler(ErrorCode.CANNOT_USE_SAME_PASSWORD);
        }

        if (!requestDto.newPassword().equals(requestDto.passwordCheck())) {
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        user.updatePassword(passwordEncoder.encode(requestDto.newPassword()));

        logout(request);
    }

    public void forgotPassword(ForgotPwRequestDto requestDto) {
        User user = userJpaRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));

        if (!requestDto.password().equals(requestDto.passwordCheck())) {
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        user.updatePassword(passwordEncoder.encode(requestDto.password()));
    }

    public void updateEmail(HttpServletRequest request, User user, ChangeEmailRequestDto requestDto) {

        if (!requestDto.currentEmail().equals(user.getEmail())) {
            throw new AccountsExceptionHandler(ErrorCode.EMAIL_NOT_MATCH);
        }

        // 새 이메일이 이미 사용 중인지 확인
        accountsCommonService.checkUserEmail(requestDto.newEmail());

        // 이메일 변경
        user.updateEmail(requestDto);

        logout(request);
    }

    // 회원 soft delete
    public void deleteAccount(String email, User user) {

        Optional<User> deleteUserOpt = userJpaRepository.findByEmail(email);

        if (deleteUserOpt.isPresent()) {
            User deleteUser = deleteUserOpt.get();

            if (!user.getId().equals(deleteUser.getId())) {
                throw new AccountsExceptionHandler(ErrorCode.NO_USER_AUTHORIZATION);
            }

            deleteUser.softDelete();
            userJpaRepository.save(deleteUser);
        } else {
            throw new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND);
        }
    }

    // 매일 자정에 실행되도록 설정, soft delete 후 30 지나면 삭제
    @Scheduled(cron = "0 0 0 * * ?")
    public void purgeDeletedUsers() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        userJpaRepository.deleteInactiveUsers(thirtyDaysAgo);
    }

    public void recoverAccount(String email) {
        Optional<User> recoverUserOpt = userJpaRepository.findByEmail(email);

        if (recoverUserOpt.isPresent()) {
            User recoverUser = recoverUserOpt.get();
            recoverUser.recoverDelete();
            userJpaRepository.save(recoverUser);
        } else {
            throw new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND);
        }
    }
}
