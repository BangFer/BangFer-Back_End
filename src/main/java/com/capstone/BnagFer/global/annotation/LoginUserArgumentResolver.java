package com.capstone.BnagFer.global.annotation;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.jwt.exception.SecurityCustomException;
import com.capstone.BnagFer.domain.accounts.jwt.exception.TokenErrorCode;
import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetails;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.accounts.service.AccountsQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final UserJpaRepository userJpaRepository;
    private final AccountsQueryService accountsQueryService;

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
                                  WebDataBinderFactory binderFactory) {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("User details from security context: {}", userDetails);
        User user = accountsQueryService.getUserByEmail(((CustomUserDetails)userDetails).getUsername());
        log.info("User details from accountsQueryService: {}", user);
        return user;
    }
}