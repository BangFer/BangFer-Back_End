package com.capstone.BnagFer.domain.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportActivity {
    NORMAL("정상적인 게시물"),
    CURSING("욕설/비하"),
    OBSCENE("음란물/불건전한 만남 및 대화"),
    POLITICAL("정치적 발언"),
    IMPOSTOR("사칭"),
    COMMERCIAL("상업적 광고 및 판매");
    private final String description;
}
