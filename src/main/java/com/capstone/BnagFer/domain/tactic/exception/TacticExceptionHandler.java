package com.capstone.BnagFer.domain.tactic.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class TacticExceptionHandler extends CustomException {
    public TacticExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
