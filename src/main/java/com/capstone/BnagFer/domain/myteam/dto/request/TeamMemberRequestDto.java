package com.capstone.BnagFer.domain.myteam.dto.request;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import jakarta.validation.constraints.NotBlank;

public record TeamMemberRequestDto(
        Long userId,
        Long teamId
) {
    public TeamMember toEntity(User user, Team team) {
        return TeamMember.builder()
                .user(user)
                .team(team)
                .build();
    }

}
