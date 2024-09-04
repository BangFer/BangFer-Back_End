package com.capstone.BnagFer.domain.notification.eventHandler;

import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/*
일관성: 모든 이벤트 핸들러가 동일한 구조를 가지게 되어 코드의 일관성이 향상됩니다.
의존성 주입: 생성자를 통한 의존성 주입으로 테스트가 용이해지고, 컴파일 시점에 의존성 문제를 발견할 수 있습니다.
유지보수성: 각 핸들러의 책임이 명확해지고, 공통 로직은 부모 클래스에서 처리되어 유지보수가 쉬워집니다.
확장성: 새로운 이벤트 유형을 추가할 때 이 패턴을 따라 쉽게 구현할 수 있습니다.
 */


@RequiredArgsConstructor
public abstract class BaseNotificationEventHandler<T> {
    protected final FcmNotificationService fcmNotificationService;

    @Async
    @EventListener
    public void handle(T event) {
        FcmNotificationRequestDto requestDto = createNotificationRequest(event);
        Long recipientId = getRecipientId(event);
        fcmNotificationService.sendAlarm(requestDto, recipientId);
    }

    protected abstract FcmNotificationRequestDto createNotificationRequest(T event);
    protected abstract Long getRecipientId(T event);
}