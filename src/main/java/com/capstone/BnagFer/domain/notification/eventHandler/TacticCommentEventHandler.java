package com.capstone.BnagFer.domain.notification.eventHandler;

import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.entity.NotificationTemplate;
import com.capstone.BnagFer.domain.notification.event.TacticCommentCreatedEvent;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import com.capstone.BnagFer.domain.tactic.exception.TacticExceptionHandler;
import com.capstone.BnagFer.global.common.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TacticCommentEventHandler extends BaseNotificationEventHandler<TacticCommentCreatedEvent> {
    private final UserJpaRepository userJpaRepository;

    public TacticCommentEventHandler(FcmNotificationService fcmNotificationService,
                                     UserJpaRepository userJpaRepository) {
        super(fcmNotificationService);
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    protected Class<TacticCommentCreatedEvent> getSupportedEventType() {
        return TacticCommentCreatedEvent.class;
    }

    @Override
    protected FcmNotificationRequestDto createNotificationRequest(TacticCommentCreatedEvent event) {
        String commenterNickname = userJpaRepository.findById(event.getAuthorId())
                .orElseThrow(() -> new TacticExceptionHandler(ErrorCode.USER_NOT_FOUND))
                .getProfile().getNickname();

        Map<String, String> params = new HashMap<>();
        params.put("nickname", commenterNickname);
        params.put("contentType", "전술");

        NotificationTemplate template = event.getNotificationType() == TacticCommentCreatedEvent.NotificationType.NEW_COMMENT ?
                NotificationTemplate.NEW_COMMENT : NotificationTemplate.NEW_REPLY;

        return new FcmNotificationRequestDto(
                template.getTitle(),
                template.getBody(params)
        );
    }

    @Override
    protected Long getRecipientId(TacticCommentCreatedEvent event) {
        return event.getRecipientId();
    }
}