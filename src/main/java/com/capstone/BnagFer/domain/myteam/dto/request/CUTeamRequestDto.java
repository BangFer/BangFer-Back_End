package com.capstone.BnagFer.domain.myteam.dto.request;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CUTeamRequestDto (
        @NotBlank(message = "팀 이름 입력은 필수입니다.")
        @Schema(description = "teamName", example = "맨체스터 유나이티드")
        String teamName,
        @NotNull(message = "전술 ID 입력은 필수입니다.")
        Long tacticId
) {
    public Team toEntity(Tactic tactic) {
        return Team.builder()
                .teamName(teamName)
                .tactic(tactic)
                .build();
    }
}
