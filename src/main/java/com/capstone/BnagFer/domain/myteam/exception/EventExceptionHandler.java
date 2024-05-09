package com.capstone.BnagFer.domain.myteam.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class EventExceptionHandler extends CustomException {
    public EventExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
