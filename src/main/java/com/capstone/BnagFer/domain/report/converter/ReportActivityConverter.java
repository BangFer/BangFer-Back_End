package com.capstone.BnagFer.domain.report.converter;

import com.capstone.BnagFer.domain.report.entity.ReportActivity;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.global.common.ErrorCode;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class ReportActivityConverter implements Converter<String, ReportActivity> {
    @Override
    public ReportActivity convert(String source) {
        if (source.isEmpty()) {
            return ReportActivity.NORMAL;
        }
        try {
            return ReportActivity.valueOf(source.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ReportExceptionHandler(ErrorCode._BAD_REQUEST);
        }
    }

}
