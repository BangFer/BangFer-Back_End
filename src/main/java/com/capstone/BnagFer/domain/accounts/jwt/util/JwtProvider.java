package com.capstone.BnagFer.domain.accounts.jwt.util;

import com.capstone.BnagFer.domain.accounts.jwt.dto.JwtDto;
import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetailService;
import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final CustomUserDetailService customUserDetailService;
    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;

    public JwtProvider(
            CustomUserDetailService customUserDetailService, @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.token.access-expiration-time}") Long access,
            @Value("${spring.jwt.token.refresh-expiration-time}") Long refresh) {
        this.customUserDetailService = customUserDetailService;

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        accessExpMs = access;
        refreshExpMs = refresh;
    }

    // Jwt 생성
    public String createJwtAccessToken(CustomUserDetails customUserDetails) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusMillis(accessExpMs);

        return Jwts.builder()
                .header()
                .add("alg", "HS256")
                .add("typ", "JWT")
                .and()
                .claim("email", customUserDetails.getUsername())
                .claim("is_staff", customUserDetails.getStaff())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtRefreshToken(CustomUserDetails customUserDetails) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusMillis(refreshExpMs);

        return Jwts.builder()
                .header()
                .add("alg", "HS256")
                .add("typ", "JWT")
                .and()
                .claim("email", customUserDetails.getUsername())
                .claim("is_staff", customUserDetails.getStaff())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    // Jwt 로 인증 정보 조회
//    public Authentication getAuthentication (String token) {
//        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    // jwt 에서 회원 구분 pk 추출
    public String getUserPk(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt 의 유효성 및 만료일자 확인
    public boolean validationToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return !claimsJws.getPayload().getExpiration().before(new Date()); // 만료 날짜가 현재에 비해 전이면 false
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.warn("[*] No Token in req");

            return null;
        }

        log.info("[*] Token exists");

        return authorization.split(" ")[1];
    }

    public String getUserEmail(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public Boolean isStaff(String token) throws SignatureException {
        return (Boolean) Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("is_staff");
    }

//    public Boolean isExpired(String token) throws SignatureException {
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
//                .before(new Date());
//    }

    public JwtDto reissueToken(String refreshToken) throws SignatureException {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(getUserEmail(refreshToken));

        return new JwtDto(
                createJwtAccessToken((CustomUserDetails)userDetails),
                createJwtRefreshToken((CustomUserDetails)userDetails)
        );
    }
}
