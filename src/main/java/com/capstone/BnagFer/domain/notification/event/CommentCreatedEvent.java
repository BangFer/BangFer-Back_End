package com.capstone.BnagFer.domain.notification.event;

public record CommentCreatedEvent(
        Long authorId,
        Long recipientId,
        NotificationType notificationType
) {
    public enum NotificationType {
        NEW_COMMENT,
        NEW_REPLY
    }
}