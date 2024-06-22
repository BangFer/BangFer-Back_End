package com.capstone.BnagFer.domain.report.exception;

import com.capstone.BnagFer.global.common.BaseErrorCode;
import com.capstone.BnagFer.global.common.exception.CustomException;

public class ReportExceptionHandler extends CustomException {
    public ReportExceptionHandler(BaseErrorCode code) {
        super(code);
    } }