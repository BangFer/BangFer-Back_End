package com.capstone.BnagFer.domain.accounts.dto.social;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UserSocialSignupRequestDto(
        String email,
        String password,
        String name,
        String nickName,
        String provider
) {
    public User toEntity(PasswordEncoder passwordEncoder) {
        Profile profile = Profile.builder()
                .nickname(nickName)
                .build();
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
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
