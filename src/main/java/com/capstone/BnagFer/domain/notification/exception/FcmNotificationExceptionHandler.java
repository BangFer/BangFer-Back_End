package com.capstone.BnagFer.domain.notification.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class FcmNotificationExceptionHandler extends CustomException {
    public FcmNotificationExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}