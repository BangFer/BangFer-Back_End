package com.capstone.BnagFer.domain.accounts.dto.profile;

import com.capstone.BnagFer.domain.accounts.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateProfileRequestDto(

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
        String nickname,

        @NotBlank(message = "[ERROR] 이름 입력은 필수 입니다.")
        @Size(max = 10, message = "이름은 최대 10자까지 입력 가능합니다.")
        String name,

        String description,

        LocalDate dateOfBirth,

        Gender gender
) {
}
