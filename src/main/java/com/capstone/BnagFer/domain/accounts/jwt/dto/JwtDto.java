package com.capstone.BnagFer.domain.accounts.jwt.dto;

public record JwtDto(
        String accessToken,
        String refreshToken
) {
}