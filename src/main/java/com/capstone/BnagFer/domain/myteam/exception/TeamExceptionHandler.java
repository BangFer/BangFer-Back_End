package com.capstone.BnagFer.domain.myteam.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class TeamExceptionHandler extends CustomException {
    public TeamExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
