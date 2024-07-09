package com.capstone.BnagFer.domain.report.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.dto.UserResponseDto;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.domain.report.repository.UserReportRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffActionQueryService {
    private final UserReportRepository userReportRepository;
    private final UserJpaRepository userRepository;
    public List<UserResponseDto>getUserByActivity(UserActivity userActivity) {
        return userRepository.findByUserActivity(userActivity)
                .stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());

    }

    public List<UserReportResponseDto> getUserReportRecord(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        return userReportRepository.findByReportedUser(user)
                .stream()
                .map(UserReportResponseDto::from)
                .collect(Collectors.toList());
    } //특정 사용자가 보고된 사용자 목록 반환
}
