package com.capstone.BnagFer.domain.notification.event;

import lombok.Getter;

@Getter
public class TacticCommentCreatedEvent {
    private final Long authorId;
    private final Long recipientId;
    private final NotificationType notificationType;

    public enum NotificationType {
        NEW_COMMENT,
        NEW_REPLY
    }

    public TacticCommentCreatedEvent(Long authorId, Long recipientId, NotificationType notificationType) {
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.notificationType = notificationType;
    }
}