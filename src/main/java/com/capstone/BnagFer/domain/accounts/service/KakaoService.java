package com.capstone.BnagFer.domain.accounts.service;

import com.capstone.BnagFer.domain.accounts.dto.UserLoginResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.UserSignupResponseDto;
import com.capstone.BnagFer.domain.accounts.dto.social.KakaoProfile;
import com.capstone.BnagFer.domain.accounts.dto.social.RetKakaoOAuth;
import com.capstone.BnagFer.domain.accounts.dto.social.UserSocialLoginRequestDto;
import com.capstone.BnagFer.domain.accounts.dto.social.UserSocialSignupRequestDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.exception.AccountsExceptionHandler;
import com.capstone.BnagFer.domain.accounts.jwt.userdetails.CustomUserDetails;
import com.capstone.BnagFer.domain.accounts.jwt.util.JwtProvider;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final Gson gson;
    private final AccountsService accountsService;
    private final JwtProvider jwtProvider;
    private final UserJpaRepository userJpaRepository;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;


    // 토큰 정보 가져오기
    public RetKakaoOAuth getKakaoTokenInfo(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", baseUrl + kakaoRedirectUri);
        params.add("code", code);

        String requestUri = env.getProperty("social.kakao.url.token");
        if (requestUri == null) throw new AccountsExceptionHandler(ErrorCode._INTERNAL_SERVER_ERROR);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(requestUri, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK)
            return gson.fromJson(response.getBody(), RetKakaoOAuth.class);
        throw new AccountsExceptionHandler(ErrorCode._INTERNAL_SERVER_ERROR);
    }

    //사용자 정보 조회 요청 보내기
    public KakaoProfile getKakaoProfile(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        String requestUrl = env.getProperty("social.kakao.url.profile");
        if (requestUrl == null) throw new AccountsExceptionHandler(ErrorCode._INTERNAL_SERVER_ERROR);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return gson.fromJson(response.getBody(), KakaoProfile.class);
        } catch (Exception e) {
            log.error(e.toString());
            throw new AccountsExceptionHandler(ErrorCode._INTERNAL_SERVER_ERROR);
        }
        throw new AccountsExceptionHandler(ErrorCode._INTERNAL_SERVER_ERROR);
    }

    // 연결 끊기
    public void kakaoUnlink(String accessToken) {
        String unlinkUrl = env.getProperty("social.kakao.url.unlink");
        if (unlinkUrl == null) throw new AccountsExceptionHandler(ErrorCode._INTERNAL_SERVER_ERROR);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(unlinkUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) return;
        throw new AccountsExceptionHandler(ErrorCode._INTERNAL_SERVER_ERROR);
    }

    public UserSignupResponseDto signupByKakao(UserSocialSignupRequestDto requestDto) {
        KakaoProfile  kakaoProfile = getKakaoProfile(requestDto.accessToken());
        if (kakaoProfile == null) throw new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND);
//        if (kakaoProfile.getKakao_account().getEmail() == null) {
//            kakaoUnlink(requestDto.accessToken());
//            throw new AccountsExceptionHandler(ErrorCode.EMAIL_NOT_EXIST);
//        }

        UserSignupRequestDto signupRequestDto = UserSignupRequestDto.builder()
                .email(requestDto.email())
                .name(kakaoProfile.getProperties().getNickname())
                .nickName(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build();

        return accountsService.socialSignup(signupRequestDto);
    }

    public UserLoginResponseDto loginByKakao(UserSocialLoginRequestDto requestDto) {
        KakaoProfile kakaoProfile = getKakaoProfile(requestDto.accessToken());
        if (kakaoProfile == null) throw new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND);

//        String kakaoEmail = kakaoProfile.getKakao_account().getEmail();
//        if (kakaoEmail == null) throw new AccountsExceptionHandler(ErrorCode.EMAIL_NOT_EXIST);

        User user = userJpaRepository.findByEmailAndProvider(requestDto.email(), "kakao")
                .orElseThrow(() -> new AccountsExceptionHandler(ErrorCode.USER_NOT_FOUND));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        String accessToken = jwtProvider.createJwtAccessToken(customUserDetails);
        String refreshToken = jwtProvider.createJwtRefreshToken(customUserDetails);

        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}