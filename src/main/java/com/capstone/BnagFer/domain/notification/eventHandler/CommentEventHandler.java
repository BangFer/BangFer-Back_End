package com.capstone.BnagFer.domain.notification.eventHandler;

import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.entity.NotificationTemplate;
import com.capstone.BnagFer.domain.notification.event.CommentCreatedEvent;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import com.capstone.BnagFer.global.common.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommentEventHandler extends BaseNotificationEventHandler<CommentCreatedEvent> {
    private final UserJpaRepository userJpaRepository;

    public CommentEventHandler(FcmNotificationService fcmNotificationService,
                               UserJpaRepository userJpaRepository) {
        super(fcmNotificationService);
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    protected Class<CommentCreatedEvent> getSupportedEventType() {
        return CommentCreatedEvent.class;
    }

    @Override
    protected FcmNotificationRequestDto createNotificationRequest(CommentCreatedEvent event) {
        String commenterNickname = userJpaRepository.findById(event.getAuthorId())
                .orElseThrow(() -> new BoardExceptionHandler(ErrorCode.USER_NOT_FOUND))
                .getProfile().getNickname();

        Map<String, String> params = new HashMap<>();
        params.put("nickname", commenterNickname);
        params.put("contentType", "게시글");

        NotificationTemplate template = event.getNotificationType() == CommentCreatedEvent.NotificationType.NEW_COMMENT ?
                NotificationTemplate.NEW_COMMENT : NotificationTemplate.NEW_REPLY;

        return new FcmNotificationRequestDto(
                template.getTitle(),
                template.getBody(params)
        );
    }

    @Override
    protected Long getRecipientId(CommentCreatedEvent event) {
        return event.getRecipientId();
    }
}