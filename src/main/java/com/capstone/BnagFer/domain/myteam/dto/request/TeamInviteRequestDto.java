package com.capstone.BnagFer.domain.myteam.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;

public record TeamInviteRequestDto(
        String nickName,
        Long teamId) {

    public TeamInvite toEntity(User invitedUser, Team team, User inviter) {
        return TeamInvite.builder()
                .invitedUser(invitedUser)
                .team(team)
                .inviter(inviter)
                .build();
    }
}

