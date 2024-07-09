package com.capstone.BnagFer.domain.accounts.service.account;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountsCommonService {

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