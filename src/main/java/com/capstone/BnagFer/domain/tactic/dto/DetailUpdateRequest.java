package com.capstone.BnagFer.domain.tactic.dto;

import com.capstone.BnagFer.domain.tactic.entity.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DetailUpdateRequest(
        @NotNull(message = "[ERROR] 포지션 할당은 필수입니다.")
        Position position,
        String positionDescription
){}
