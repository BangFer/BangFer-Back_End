package com.capstone.BnagFer.domain.report.service;
import com.capstone.BnagFer.domain.report.dto.UserDto;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.report.dto.UserReportDto;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.domain.report.repository.UserReportRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportActionService {
    private final UserReportRepository userReportRepository;
    private final UserJpaRepository userRepository;

    public Page<UserDto> getUserByActivity(UserActivity userActivity, Pageable pageable) {
        return userRepository.findByUserActivity(userActivity, pageable).map(UserDto::from);
    } //주어진 사용자 활동 상태에 해당하는 사용자 목록 반환 by pagination

    public Page<UserReportDto> getUserReportRecord(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        return userReportRepository.findByReportedUser(user, pageable).map(UserReportDto::from);
    } //특정 사용자가 보고된 사용자 목록 반환
    @Transactional
    public UserDto changeUserActivity(UserActivity userActivity, Long reportedUserId) {
        User reportedUser = userRepository.findById(reportedUserId).orElseThrow(
                () -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        if(reportedUser.getUserActivity().equals(UserActivity.FLAGGED)) { //유저의 Activity가 FLAGGED이면 변경할 수 있게
            reportedUser.changeActivity(userActivity);
        }
        return UserDto.from(reportedUser);
    } //사용자의 활동 상태를 변경한다
}
