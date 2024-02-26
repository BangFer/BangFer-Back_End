package com.capstone.BnagFer.domain.accounts.jwt.config;

import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private String ROLES = "roles";

    private long tokenValidMillisecond = 60 * 60 * 1000L; // 1 hour

    private final CustomUserDetailService userDetailService;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decodedKey);
    }

    // Jwt 생성
    public String createToken(String userPk, List<String> roles) {

        // user 구분을 위해 Claims 에 User Pk 값을 넣어줌
        Claims claims = Jwts.claims().subject(String.valueOf(userPk)).build();
//        claims.put(ROLES, roles);
        // 생성 날짜, 만료 날짜를 위한 Date
        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(key)
                .compact();
    }

    // Jwt 로 인증 정보 조회
    public Authentication getAuthentication (String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // jwt 에서 회원 구분 pk 추출
    public String getUserPk(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    // HTTP Request 의 Header 에서 Token Parsing -> "X-AUTH-TOKEN: jwt"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // jwt 의 유효성 및 만료일자 확인
    public boolean validationToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return !claimsJws.getPayload().getExpiration().before(new Date()); // 만료 날짜가 현재에 비해 전이면 false
        } catch (Exception e) {
            return false;
        }
    }
}
