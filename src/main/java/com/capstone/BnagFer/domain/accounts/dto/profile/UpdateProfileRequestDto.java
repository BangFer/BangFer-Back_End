package com.capstone.BnagFer.domain.accounts.dto.profile;

import com.capstone.BnagFer.domain.accounts.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateProfileRequestDto(

        @NotBlank(message = "닉네임은 필수입니다.")
        @Schema(name = "nickname", example = "거장 퍼거슨")
        @Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
        String nickname,

        @NotBlank(message = "[ERROR] 이름 입력은 필수 입니다.")
        @Size(max = 10, message = "이름은 최대 10자까지 입력 가능합니다.")
        String name,

        @Schema(description = "description", example = "명장 퍼거슨 입니다")
        String description,

        @Schema(description = "dateOfBirth", example = "2000-01-01")
        LocalDate dateOfBirth,

        @Schema(description = "gender", example = "MALE")
        Gender gender
) {
}
