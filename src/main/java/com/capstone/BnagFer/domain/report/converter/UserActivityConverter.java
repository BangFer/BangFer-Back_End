package com.capstone.BnagFer.domain.report.converter;

import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.global.common.ErrorCode;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class UserActivityConverter implements Converter<String, UserActivity> {
    //문자열 -> Enum으로 변환
    @Override
    public UserActivity convert(String source) {
        if (source.isEmpty()) {
            return UserActivity.NORMAL;
        }
        try {
            return UserActivity.valueOf(source.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ReportExceptionHandler(ErrorCode._BAD_REQUEST);
        }
    }
}
