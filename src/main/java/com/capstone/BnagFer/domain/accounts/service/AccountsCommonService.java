package com.capstone.BnagFer.domain.accounts.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountsCommonService {

    private final UserJpaRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // 현재 로그인한 사용자의 이메일
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));
    }

    public void checkUserProfile(User user) {
        if (user.getProfile() == null)
            throw new AccountsExceptionHandler(ErrorCode.PROFILE_NOT_EXIST);
    }

    public void validateStaffAccess(User user) {
        if (!user.getIsStaff()) {
            throw new AccountsExceptionHandler(ErrorCode.USER_IS_NOT_STAFF);
        }
    }
}