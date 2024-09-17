package com.capstone.BnagFer.domain.accounts.service.account;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountsCommonService {

    private final UserJpaRepository userJpaRepository;

    public void checkUserProfile(User user) {
        if (user.getProfile() == null)
            throw new AccountsExceptionHandler(ErrorCode.PROFILE_NOT_EXIST);
    }

    public void checkUserActivity(User user) {
        if (user.getUserActivity() == UserActivity.BAN)
            throw new AccountsExceptionHandler(ErrorCode.USER_IS_BANNED);
    }

    public void checkUserEmail(String email) {
        if (userJpaRepository.existsByEmail(email)) {
            throw new AccountsExceptionHandler(ErrorCode.EMAIL_ALREADY_EXIST);
        }
    }
}