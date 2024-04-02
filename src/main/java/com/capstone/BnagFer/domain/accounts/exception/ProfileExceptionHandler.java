package com.capstone.BnagFer.domain.accounts.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class ProfileExceptionHandler extends CustomException {
    public ProfileExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
