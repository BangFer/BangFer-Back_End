package com.capstone.BnagFer.domain.accounts.dto.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmailVerifyDto(
        @NotBlank(message = "[ERROR] 이메일 입력은 필수 입니다.")
        String email,

        @NotBlank(message = "[ERROR] 코드 입력은 필수 입니다.")
        String code
) {
}
