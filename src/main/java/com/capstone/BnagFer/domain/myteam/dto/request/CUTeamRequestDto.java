package com.capstone.BnagFer.domain.myteam.dto.request;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CUTeamRequestDto (
        @NotBlank(message = "팀 이름 입력은 필수입니다.")
        @Schema(description = "teamName", example = "맨체스터 유나이티드")
        String teamName
) {
    public Team toEntity() {
        return Team.builder()
                .teamName(teamName)
                .build();
    }
}
