package com.capstone.BnagFer.domain.accounts.jwt.exception;

import com.capstone.BnagFer.domain.accounts.jwt.util.HttpResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Access Denied: ", accessDeniedException);

        HttpResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN,
                TokenErrorCode.FORBIDDEN.getErrorResponse());
    }
}
