package com.capstone.BnagFer.domain.accounts.dto.profile;

import com.capstone.BnagFer.domain.accounts.entity.Gender;
import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateProfileRequestDto(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Schema(name = "nickname", example = "거장 퍼거슨")
        @Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
        String nickname,

        @Schema(name = "description", example = "명장 퍼거슨 입니다")
        String description,

        @Schema(name = "dateOfBirth", example = "2000-01-01")
        LocalDate dateOfBirth,

        @Schema(name = "gender", example = "MALE")
        Gender gender

) {
        public Profile toEntity(User user) {
                return Profile.builder()
                        .nickname(nickname)
                        .description(description)
                        .dateOfBirth(dateOfBirth)
                        .gender(gender)
                        .user(user)
                        .build();
        }
}
