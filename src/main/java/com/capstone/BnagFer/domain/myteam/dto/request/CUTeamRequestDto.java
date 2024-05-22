package com.capstone.BnagFer.domain.myteam.dto.request;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import jakarta.validation.constraints.NotBlank;

public record CUTeamRequestDto (@NotBlank(message = "팀 이름 입력은 필수입니다.")String teamName) {
    public Team toEntity() {
        return Team.builder()
                .teamName(teamName)
                .build();
    }
}
