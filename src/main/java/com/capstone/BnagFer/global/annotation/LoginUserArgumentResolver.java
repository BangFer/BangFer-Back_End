package com.capstone.BnagFer.global.annotation;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.jwt.exception.SecurityCustomException;
import com.capstone.BnagFer.domain.accounts.jwt.exception.TokenErrorCode;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Transactional
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean parameterAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean userParameterType = parameter.getParameterType().isAssignableFrom(User.class);
        return parameterAnnotation && userParameterType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = jwtProvider.resolveAccessToken(httpServletRequest);
        String email = jwtProvider.getUserEmail(token);
        return userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityCustomException(TokenErrorCode.TOKEN_NOT_FOUND));
    }
}