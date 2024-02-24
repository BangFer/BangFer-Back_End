package com.capstone.BnagFer.domain.accounts.controller;

import com.capstone.BnagFer.domain.accounts.dto.UserLoginRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.UserLoginResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupResponseDto;
import com.capstone.BnagFer.domain.accounts.jwt.config.JwtProvider;
import com.capstone.BnagFer.domain.accounts.service.AccountsService;
import com.capstone.BnagFer.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountsController {

    private final AccountsService accountsService;

    @PostMapping("/login")
    public ApiResponse<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return ApiResponse.onSuccess(accountsService.login(requestDto));
    }

    @PostMapping("/signup")
    public ApiResponse<UserSignupResponseDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        return ApiResponse.onSuccess(accountsService.signup(requestDto));
    }
}
