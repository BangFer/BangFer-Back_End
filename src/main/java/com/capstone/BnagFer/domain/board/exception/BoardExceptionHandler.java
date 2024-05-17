package com.capstone.BnagFer.domain.board.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class BoardExceptionHandler extends CustomException {
    public BoardExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}