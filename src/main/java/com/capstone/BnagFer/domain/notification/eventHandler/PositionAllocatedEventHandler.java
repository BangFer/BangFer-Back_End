package com.capstone.BnagFer.domain.notification.eventHandler;

import com.capstone.BnagFer.domain.myteam.exception.TeamMemberExceptionHandler;
import com.capstone.BnagFer.domain.myteam.repository.TeamMembersRepository;
import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.entity.NotificationTemplate;
import com.capstone.BnagFer.domain.notification.event.PositionAllocatedEvent;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import com.capstone.BnagFer.global.common.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PositionAllocatedEventHandler extends BaseNotificationEventHandler<PositionAllocatedEvent> {
    private final TeamMembersRepository teamMembersRepository;

    public PositionAllocatedEventHandler(FcmNotificationService fcmNotificationService,
                                         TeamMembersRepository teamMembersRepository) {
        super(fcmNotificationService);
        this.teamMembersRepository = teamMembersRepository;
    }

    @Override
    protected Class<PositionAllocatedEvent> getSupportedEventType() {
        return PositionAllocatedEvent.class;
    }

    @Override
    protected FcmNotificationRequestDto createNotificationRequest(PositionAllocatedEvent event) {
        Map<String, String> params = new HashMap<>();
        params.put("teamName", event.teamName());
        params.put("position", event.position().name());

        return new FcmNotificationRequestDto(
                NotificationTemplate.POSITION_ALLOCATED.getTitle(),
                NotificationTemplate.POSITION_ALLOCATED.getBody(params)
        );
    }

    @Override
    protected Long getRecipientId(PositionAllocatedEvent event) {
        return teamMembersRepository.findById(event.memberId())
                .orElseThrow(() -> new TeamMemberExceptionHandler(ErrorCode.CANNOT_FIND_TEAMMEMBER))
                .getUser().getId();
    }
}