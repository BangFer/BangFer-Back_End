package com.capstone.BnagFer.domain.accounts.service;

import com.capstone.BnagFer.domain.accounts.dto.UserLoginRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.UserLoginResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupResponseDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetails;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountsService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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
}
