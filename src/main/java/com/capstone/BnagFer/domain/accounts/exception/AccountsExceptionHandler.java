package com.capstone.BnagFer.domain.accounts.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class AccountsExceptionHandler extends CustomException {
    public AccountsExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
