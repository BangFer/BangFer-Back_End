package com.capstone.BnagFer.domain.firebase.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class FirebaseExceptionHandler extends CustomException {
    public FirebaseExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}