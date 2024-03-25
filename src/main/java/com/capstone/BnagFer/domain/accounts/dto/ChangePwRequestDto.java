package com.capstone.BnagFer.domain.accounts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePwRequestDto(
        @NotBlank(message = "[ERROR] 비밀번호 입력은 필수 입니다.")
        @Size(min = 8, message = "[ERROR] 비밀번호는 최소 8자리 이이어야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,64}$", message = "[ERROR] 비밀번호는 8자 이상, 64자 이하이며 특수문자 한 개를 포함해야 합니다.")
        String password,

        @NotBlank(message = "[ERROR] 비밀번호 재확인 입력은 필수 입니다.")
        String passwordCheck
) {
}
