package com.capstone.BnagFer.domain.myteam.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class TeamMemberExceptionHandler extends CustomException {
    public TeamMemberExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
