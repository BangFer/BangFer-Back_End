package com.capstone.BnagFer.domain.accounts.controller;

import com.capstone.BnagFer.domain.accounts.dto.*;
import com.capstone.BnagFer.domain.accounts.dto.social.UserSocialLoginRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.social.UserSocialSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.jwt.dto.JwtDto;
import com.capstone.BnagFer.domain.accounts.jwt.exception.SecurityCustomException;
import com.capstone.BnagFer.domain.accounts.jwt.exception.TokenErrorCode;
import com.capstone.BnagFer.domain.accounts.service.AccountsQueryService;
import com.capstone.BnagFer.domain.accounts.service.AccountsService;
import com.capstone.BnagFer.domain.accounts.service.KakaoService;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountsController {

    private final AccountsService accountsService;
    private final AccountsQueryService accountsQueryService;
    private final JwtProvider jwtProvider;
    private final KakaoService kakaoService;

    @PostMapping("/login")
    public ApiResponse<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return ApiResponse.onSuccess(accountsService.login(requestDto));
    }

    @PostMapping("/signup")
    public ApiResponse<UserSignupResponseDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        return ApiResponse.onSuccess(accountsService.signup(requestDto));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        accountsService.logout(request);
        return ApiResponse.onSuccess("로그아웃 성공");
    }

    @GetMapping("/{email}")
    public ApiResponse<User> getUserByEmail(@PathVariable String email) {
        User user = accountsQueryService.getUserByEmail(email);
        return ApiResponse.onSuccess(user);
    }

    @PostMapping("/changePw")
    public ResponseEntity<ApiResponse<String>> changePassword(
            HttpServletRequest request,
            @Valid @RequestBody ChangePwRequestDto requestDto) {
        accountsService.changePassword(request, requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("비밀번호 변경 성공"));
    }

    @PostMapping("/forgotPw")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPwRequestDto requestDto) {
        accountsService.forgotPassword(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("비밀번호 변경 성공"));
    }

    @GetMapping("/reissue")
    public ApiResponse<JwtDto> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
        try {
            jwtProvider.validateRefreshToken(refreshToken);
            return ApiResponse.onSuccess(
                    jwtProvider.reissueToken(refreshToken)
            );
        } catch (ExpiredJwtException eje) {
            throw new SecurityCustomException(TokenErrorCode.TOKEN_EXPIRED, eje);
        } catch (IllegalArgumentException iae) {
            throw new SecurityCustomException(TokenErrorCode.INVALID_TOKEN, iae);
        }
    }

    @PostMapping("/social/signup/kakao")
    public ApiResponse<UserSignupResponseDto> signupByKakao(@Valid @RequestBody UserSocialSignupRequestDto requestDto) {
        return ApiResponse.onSuccess(kakaoService.signupByKakao(requestDto));
    }

    @PostMapping("/social/login/kakao")
    public ApiResponse<UserLoginResponseDto> loginByKakao(@Valid @RequestBody UserSocialLoginRequestDto requestDto) {
        return ApiResponse.onSuccess(kakaoService.loginByKakao(requestDto));
    }
}
