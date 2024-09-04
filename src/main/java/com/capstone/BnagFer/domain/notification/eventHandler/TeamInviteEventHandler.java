package com.capstone.BnagFer.domain.notification.eventHandler;

import com.capstone.BnagFer.domain.notification.dto.FcmNotificationRequestDto;
import com.capstone.BnagFer.domain.notification.entity.NotificationTemplate;
import com.capstone.BnagFer.domain.notification.event.TeamInviteCreatedEvent;
import com.capstone.BnagFer.domain.notification.service.FcmNotificationService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TeamInviteEventHandler extends BaseNotificationEventHandler<TeamInviteCreatedEvent> {

    public TeamInviteEventHandler(FcmNotificationService fcmNotificationService) {
        super(fcmNotificationService);
    }

    @Override
    protected FcmNotificationRequestDto createNotificationRequest(TeamInviteCreatedEvent event) {
        Map<String, String> params = new HashMap<>();
        params.put("inviterNickname", event.getInviterNickname());
        params.put("teamName", event.getTeamName());

        return new FcmNotificationRequestDto(
                NotificationTemplate.TEAM_INVITE.getTitle(),
                NotificationTemplate.TEAM_INVITE.getBody(params)
        );
    }

    @Override
    protected Long getRecipientId(TeamInviteCreatedEvent event) {
        return event.getInvitedUserId();
    }
}