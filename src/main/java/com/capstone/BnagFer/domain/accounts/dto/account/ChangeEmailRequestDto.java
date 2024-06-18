package com.capstone.BnagFer.domain.accounts.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangeEmailRequestDto(
        @NotBlank(message = "[ERROR] 현재 이메일 입력은 필수 입니다.")
        @Schema(description = "currentEmail", example = "test1234@naver.com")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "[ERROR] 이메일 형식에 맞지 않습니다.")
        String currentEmail,

        @NotBlank(message = "[ERROR] 새 이메일 입력은 필수 입니다.")
        @Schema(description = "newEmail", example = "test12345@naver.com")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "[ERROR] 이메일 형식에 맞지 않습니다.")
        String newEmail
) {
}
