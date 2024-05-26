package com.capstone.BnagFer.domain.accounts.dto.email;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmailVerifyDto(
        @NotBlank(message = "[ERROR] 이메일 입력은 필수 입니다.")
        @Schema(description = "email", example = "test1234@naver.com")
        String email,

        @NotBlank(message = "[ERROR] 코드 입력은 필수 입니다.")
        String code
) {
}
