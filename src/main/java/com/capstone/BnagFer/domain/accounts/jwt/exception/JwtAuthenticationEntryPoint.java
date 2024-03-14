package com.capstone.BnagFer.domain.accounts.jwt.exception;

import com.capstone.BnagFer.domain.accounts.jwt.util.HttpResponseUtil;
import com.capstone.BnagFer.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        HttpStatus httpStatus;
        ApiResponse<String> errorResponse;

        log.error(">>>>>> AuthenticationException: ", authException);
        httpStatus = HttpStatus.UNAUTHORIZED;
        errorResponse = ApiResponse.onFailure(
                TokenErrorCode.UNAUTHORIZED.getCode(),
                TokenErrorCode.UNAUTHORIZED.getMessage(),
                authException.getMessage());

        HttpResponseUtil.setErrorResponse(response, httpStatus, errorResponse);
    }
}
