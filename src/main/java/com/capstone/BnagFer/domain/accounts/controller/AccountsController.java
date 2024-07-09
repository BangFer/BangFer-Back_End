package com.capstone.BnagFer.domain.accounts.controller;

import com.capstone.BnagFer.domain.accounts.dto.account.*;
import com.capstone.BnagFer.domain.accounts.dto.email.EmailVerifyDto;
import com.capstone.BnagFer.domain.accounts.dto.social.UserSocialLoginRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.social.UserSocialSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.jwt.dto.JwtDto;
import com.capstone.BnagFer.domain.accounts.jwt.exception.SecurityCustomException;
import com.capstone.BnagFer.domain.accounts.jwt.exception.TokenErrorCode;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsQueryService;
import com.capstone.BnagFer.domain.accounts.service.account.AccountsService;
import com.capstone.BnagFer.domain.accounts.service.account.KakaoService;
import com.capstone.BnagFer.domain.accounts.service.email.EmailService;
import com.capstone.BnagFer.global.annotation.LoginUser;
import com.capstone.BnagFer.global.common.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "회원 API")
@RequestMapping("/accounts")
@RestController
public class AccountsController {

    private final AccountsService accountsService;
    private final AccountsQueryService accountsQueryService;
    private final JwtProvider jwtProvider;
    private final KakaoService kakaoService;
    private final EmailService emailService;

    @Operation(summary = "일반 로그인", description = "이메일, 비밀번호를 입력받아 로그인을 진행합니다. 이때, FCM토큰을 같이 넘겨줘야 함." +
            "반환 값으로 JWT accessToken과 refreshToken이 발급됨. accessToken 값을 Authorize에 인증")
    @PostMapping("/login")
    public ApiResponse<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return ApiResponse.onSuccess(accountsService.login(requestDto));
    }

    @Operation(summary = "일반 회원가입", description = "이름, 이메일, 비밀번호를 입력받아 회원가입을 진행합니다. 이메일은 중복 불가, 비밀먼호는 인코딩 되어 저장됨. 참고) SMTP 이메일 인증 필요.")
    @PostMapping("/signup")
    public ApiResponse<UserSignupResponseDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        return ApiResponse.onSuccess(accountsService.signup(requestDto));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        accountsService.logout(request);
        return ApiResponse.onSuccess("로그아웃 성공");
    }

    @Operation(summary = "회원 탈퇴", description = "이메일을 입력받아 해당 회원을 탈퇴시킴. 바로 탈퇴 되는게 아닌 30일 동안 회원 유지 하고 탈퇴. (30일 안에 회원 복구 가능) " +
            "참고) SMTP 이메일 인증 필요.")
    @DeleteMapping("/delete/{email}")
    public ApiResponse<String> deleteAccount(@PathVariable(name = "email") String email, @LoginUser User user) {
        accountsService.deleteAccount(email, user);
        return ApiResponse.onSuccess("회원 탈퇴 성공");
    }

    @Operation(summary = "탈퇴 회원 복구", description = "이메일을 입력받아 해방 회원을 복구 시킴. 탈퇴 30일 이후면 복구 불가. " +
            "참고) SMTP 이메일 인증 필요.")
    @PostMapping("/recover/{email}")
    public ApiResponse<String> recoverAccount(@PathVariable(name = "email") String email) {
        accountsService.recoverAccount(email);
        return ApiResponse.onSuccess("회원 복구 성공");
    }

    @Operation(summary = "이메일로 회원 조회", description = "이메일로 해당 회원 조회한다.")
    @GetMapping("/{email}")
    public ApiResponse<User> getUserByEmail(@PathVariable(name = "email") String email) {
        User user = accountsQueryService.getUserByEmail(email);
        return ApiResponse.onSuccess(user);
    }

    @Operation(summary = "비밀번호 변경", description = "기존 비밀번호, 새 비밀번호, 새 비밀번호 확인을 받음. 로그인된 상태에서 가능. 비밀번호 변경 후 로그아웃 처리됨. " +
            "참고) SMTP 이메일 인증 필요.")
    @PutMapping("/changePw")
    public ApiResponse<String> changePassword(
            @LoginUser User user,
            HttpServletRequest request,
            @Valid @RequestBody ChangePwRequestDto requestDto) {
        accountsService.updatePassword(request, requestDto, user);
        return ApiResponse.onSuccess("비밀번호 변경 성공");
    }

    @Operation(summary = "비밀번호 분실", description = "이메일을 받아 해당회원의 새 비밀번호, 새 비밀번호 확인을 받음. " +
            "참고) SMTP 이메일 인증 필요.")
    @PutMapping("/forgotPw")
    public ApiResponse<String> forgotPassword(
            @Valid @RequestBody ForgotPwRequestDto requestDto) {
        accountsService.forgotPassword(requestDto);
        return ApiResponse.onSuccess("비밀번호 변경 성공");
    }

    @Operation(summary = "이메일 변경", description = "현재 이메일과 새 이메일을 받음. 이메일 변경 후 로그아웃 처림됨. " +
            "참고) SMTP 이메일 인증 2번 필요.")
    @PutMapping("/changeEmail")
    public ApiResponse<String> changeEmail(
            @LoginUser User user,
            HttpServletRequest request,
            @Valid @RequestBody ChangeEmailRequestDto requestDto) {
        accountsService.updateEmail(request, user, requestDto);
        return ApiResponse.onSuccess("이메일 변경 성공");
    }

    @Operation(summary = "토큰 재발급", description = "JWT accessToken이 만료됐을 시 refreshToken을 통해 accessToken 재발급.")
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

    @Operation(summary = "카카오 회원가입", description = "카카오 accessToken, 이메일을 입력받아 회원가입을 진행합니다. 이메일은 중복 불가.")
    @PostMapping("/social/signup/kakao")
    public ApiResponse<UserSignupResponseDto> signupByKakao(@Valid @RequestBody UserSocialSignupRequestDto requestDto) {
        return ApiResponse.onSuccess(kakaoService.signupByKakao(requestDto));
    }

    @Operation(summary = "카카오 로그인", description = "카카오 accessToken, 이메일을 입력받아 로그인. 이때, FCM토큰을 같이 넘겨줘야 함. " +
            "반환 값으로 JWT accessToken과 refreshToken이 발급됨. accessToken 값을 Authorize에 인증\"")
    @PostMapping("/social/login/kakao")
    public ApiResponse<UserLoginResponseDto> loginByKakao(@Valid @RequestBody UserSocialLoginRequestDto requestDto) {
        return ApiResponse.onSuccess(kakaoService.loginByKakao(requestDto));
    }

    @Operation(summary = "SMTP 이메일 전송", description = "해당 이메일로 인증번호 6자리를 보냄.")
    @PostMapping("/email/send-email")
    public ApiResponse<String> sendEmail(@RequestParam String email) throws Exception {
        return ApiResponse.onSuccess(emailService.sendMessage(email));
    }

    @Operation(summary = "이메일 인증", description = "SMTP 이메일 전송을 통해 받은 인증번호를 입력하여 인증")
    @PostMapping("/email/verify")
    public ApiResponse<String> verifyCode(@Valid @RequestBody EmailVerifyDto requestDto) {
        boolean check = emailService.verifyCode(requestDto);
        if (check) {
            return ApiResponse.onSuccess("인증 완료!");
        }
        else {
            return ApiResponse.onFailure(HttpStatus.BAD_REQUEST.name(), "인증 실패");
        }
    }
}
