package com.capstone.BnagFer.domain.report.service;
import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.dto.UserReportRequest;
import com.capstone.BnagFer.domain.report.entity.ReportActivity;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.entity.UserReport;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.domain.report.repository.UserReportRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final UserReportRepository userReportRepository;
    private final UserJpaRepository userRepository;

    public UserReportResponseDto reportUser(User reporter, Long reportedUserId, ReportActivity reportActivity) {
        /*
           유저 신고 기능
              - 유저는 자기 자신을 신고할 수 없음.
              - 신고할 유저가 존재하는지 체크.
              - 신고할 유저가 이미 ban 당했는지 체크.
         */
        User reportedUser = userRepository.findById(reportedUserId).orElseThrow(() -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
        validateSelfReport(reporter.getId(), reportedUser.getId());
        validateUserBanned(reportedUser);
        UserReportRequest request = new UserReportRequest();
        reportedUser.changeActivity(UserActivity.FLAGGED);
        UserReport userReport = request.toEntity(reporter, reportedUser, reportActivity);
        userReportRepository.save(userReport);
        return UserReportResponseDto.from(userReport);
    }

    private void validateUserBanned(User reportedUser) { //이미 차단된 계정인지 확인
        if (reportedUser.getUserActivity().equals(UserActivity.BAN)) {
            throw new ReportExceptionHandler(ErrorCode.USER_BANNED);
        }
    }

    private void validateSelfReport(Long reporterId, Long reportedUserId) { //자기 자신을 신고하는지 확인
        if (reporterId.equals(reportedUserId)) {
            throw new ReportExceptionHandler(ErrorCode.CANNOT_REPORT_YOURSELF);
        }
    }
}
