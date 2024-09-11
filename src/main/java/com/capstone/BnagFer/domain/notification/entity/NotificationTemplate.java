package com.capstone.BnagFer.domain.notification.entity;

import lombok.Getter;

import java.util.Map;

public enum NotificationTemplate {
    NEW_COMMENT("새 댓글", "{nickname}님이 회원님의 {contentType}에 댓글을 달았습니다."),
    NEW_REPLY("새 대댓글", "{nickname}님이 회원님의 댓글에 대댓글을 달았습니다."),
    POSITION_ALLOCATED("포지션 할당", "{teamName} 팀에서 {position} 포지션이 할당되었습니다."),
    TEAM_INVITE("팀 초대", "{inviterNickname}님이 {teamName} 팀에 초대하였습니다.");

    @Getter
    private final String title;
    private final String bodyTemplate;

    NotificationTemplate(String title, String bodyTemplate) {
        this.title = title;
        this.bodyTemplate = bodyTemplate;
    }

    public String getBody(Map<String, String> params) {
        String body = bodyTemplate;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            body = body.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return body;
    }
}
