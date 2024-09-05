package com.capstone.BnagFer.domain.notification.event;

import lombok.Getter;

@Getter
public class CommentCreatedEvent {
    private final Long authorId;
    private final Long recipientId;
    private final NotificationType notificationType;

    public enum NotificationType {
        NEW_COMMENT,
        NEW_REPLY
    }

    public CommentCreatedEvent(Long authorId, Long recipientId, NotificationType notificationType) {
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.notificationType = notificationType;
    }
}