package com.capstone.BnagFer.domain.notification.event;

public record TacticCommentCreatedEvent(
        Long authorId,
        Long recipientId,
        NotificationType notificationType
) {
    public enum NotificationType {
        NEW_COMMENT,
        NEW_REPLY
    }
}