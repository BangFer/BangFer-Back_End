package com.capstone.BnagFer.domain.accounts.dto;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserSignupRequestDto(

        @NotBlank(message = "[ERROR] 이름 입력은 필수 입니다.")
        String name,

        @NotBlank(message = "[ERROR] 이메일 입력은 필수입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "[ERROR] 이메일 형식에 맞지 않습니다.")
        String email,

        @NotBlank(message = "[ERROR] 비밀번호 입력은 필수 입니다.")
        @Size(min = 8, message = "[ERROR] 비밀번호는 최소 8자리 이이어야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,64}$", message = "[ERROR] 비밀번호는 8자 이상, 64자 이하이며 특수문자 한 개를 포함해야 합니다.")
        String password,

        @NotBlank(message = "[ERROR] 비밀번호 재확인 입력은 필수 입니다.")
        String passwordCheck,

        String nickName,

        String provider
) {
        public User toEntity(String encodedPw) {
                Profile profile = Profile.builder()
                        .nickname(nickName)
                        .build();
                User user = User.builder()
                        .email(email)
                        .password(encodedPw)
                        .name(name)
                        .build();
                profile.setUser(user);
                user.setProfile(profile);
                return user;
        }

        public User toEntity() {
                Profile profile = Profile.builder()
                        .nickname(nickName)
                        .build();
                User user = User.builder()
                        .email(email)
                        .name(name)
                        .provider(provider)
                        .build();
                profile.setUser(user);
                user.setProfile(profile);

                return user;
        }
}