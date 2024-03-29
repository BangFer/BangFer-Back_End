package com.capstone.BnagFer.domain.accounts.service;

import com.capstone.BnagFer.domain.accounts.dto.*;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.jwt.exception.SecurityCustomException;
import com.capstone.BnagFer.domain.accounts.jwt.exception.TokenErrorCode;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetails;
import com.capstone.BnagFer.domain.accounts.jwt.util.RedisUtil;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountsService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final AccountsServiceUtils accountsServiceUtils;

    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {

        // 회원 정보 존재 하는지 확인
        User user = userJpaRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));

        // 회원 pw 일치 여부
        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_EQUAL);
        }

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
        if (userJpaRepository.existsByEmail(requestDto.email())) {
            throw new AccountsExceptionHandler(ErrorCode.USER_ALREADY_EXIST);
        }

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

    public void changePassword(HttpServletRequest request, ChangePwRequestDto requestDto) {
        if (!requestDto.password().equals(requestDto.passwordCheck())) {
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        User user = accountsServiceUtils.getCurrentUser();
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userJpaRepository.save(user);

        logout(request);
    }

    public void forgotPassword(ForgotPwRequestDto requestDto) {
        User user = userJpaRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));

        if (!requestDto.password().equals(requestDto.passwordCheck())) {
            throw new AccountsExceptionHandler(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userJpaRepository.save(user);
    }
}
