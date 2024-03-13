package com.capstone.BnagFer.domain.accounts.controller;

import com.capstone.BnagFer.domain.accounts.annotation.AccountResolver;
import com.capstone.BnagFer.domain.accounts.dto.UserLoginRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.UserLoginResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.social.KakaoProfile;
import com.capstone.BnagFer.domain.accounts.dto.social.UserSocialSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.jwt.dto.JwtDto;
import com.capstone.BnagFer.domain.accounts.jwt.exception.SecurityCustomException;
import com.capstone.BnagFer.domain.accounts.jwt.exception.TokenErrorCode;
import com.capstone.BnagFer.domain.accounts.service.AccountsQueryService;
import com.capstone.BnagFer.domain.accounts.service.AccountsService;
import com.capstone.BnagFer.domain.accounts.service.KakaoService;
import com.capstone.BnagFer.global.common.ApiResponse;
import com.capstone.BnagFer.global.common.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/{email}")
    public ApiResponse<User> getUserByEmail(@PathVariable String email) {
        User user = accountsQueryService.getUserByEmail(email);
        return ApiResponse.onSuccess(user);
    }

    @GetMapping("/reissue")
    public ApiResponse<JwtDto> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
        try {
            jwtProvider.validationToken(refreshToken);
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
    public ApiResponse signupBySocial(@Valid @RequestBody UserSocialSignupRequestDto requestDto) {
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(requestDto.getAccessToken());
        if (kakaoProfile == null) new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND);
        if (kakaoProfile.getKakao_account().getEmail() == null) {
            kakaoService.kakaoUnlink(requestDto.getAccessToken());
            throw new CSocialAgreementException();
        }

        Long userId = signService.socialSignup(UserSignupRequestDto.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .name(kakaoProfile.getProperties().getNickname())
                .nickName(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());

        return responseService.getSingleResult(userId);
    }

    @GetMapping("/test")
    public ApiResponse<String> register(@AccountResolver User user) {
        return ApiResponse.onSuccess(user.getEmail());
    }
}
