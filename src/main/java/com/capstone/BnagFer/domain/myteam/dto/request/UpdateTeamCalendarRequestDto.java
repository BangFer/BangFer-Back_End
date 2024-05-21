package com.capstone.BnagFer.domain.myteam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateTeamCalendarRequestDto {
    @NotNull(message = "매치 정보 입력은 필수입니다.")
    String matchInfo;
}
