package com.capstone.BnagFer.domain.accounts.dto.profile;

import com.capstone.BnagFer.domain.accounts.entity.Gender;
import com.capstone.BnagFer.domain.accounts.entity.Profile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateProfileRequestDto(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
        String nickname,

        String description,

        LocalDate dateOfBirth,

        Gender gender
) {
        public Profile toEntity() {
                return Profile.builder()
                        .nickname(nickname)
                        .description(description)
                        .dateOfBirth(dateOfBirth)
                        .gender(gender)
                        .build();
        }
}
