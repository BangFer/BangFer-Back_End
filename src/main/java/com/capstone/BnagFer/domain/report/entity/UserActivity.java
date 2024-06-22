package com.capstone.BnagFer.domain.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActivity {

    NORMAL("일반 유저"),
    FLAGGED("신고받은 유저"),
    BAN("이용 제한 유저");

    private final String description;
}