package com.capstone.BnagFer.domain.report.service;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.accounts.repository.UserJpaRepository;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.board.exception.BoardExceptionHandler;
import com.capstone.BnagFer.domain.board.repository.BoardRepository;
import com.capstone.BnagFer.domain.report.dto.BoardReportResponseDto;
import com.capstone.BnagFer.domain.report.dto.UserReportResponseDto;
import com.capstone.BnagFer.domain.report.entity.BoardActivity;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import com.capstone.BnagFer.domain.report.exception.ReportExceptionHandler;
import com.capstone.BnagFer.domain.report.repository.BoardReportRepository;
import com.capstone.BnagFer.domain.report.repository.UserReportRepository;
import com.capstone.BnagFer.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private static final int REPORT_LIMIT = 5;
    private final UserReportRepository userReportRepository;
    private final BoardReportRepository boardReportRepository;
    private final UserJpaRepository userRepository;
    private final BoardRepository boardRepository;

    public void reportUser(Long reporterId, Long reportedUserId, String text) {

        /*
           유저 신고 기능
              - 유저는 자기 자신을 신고할 수 없음.
              - 신고할 유저가 존재하는지 체크.
              - 신고할 유저가 이미 ban 당했는지 체크합니다.
              - 이미 해당 유저를 신고했다면 신고가 접수되지 않습니다.
              - 신고당한 횟수가 REPORT_LIMIT 초과한다면 일반 유저에서 조치 필요 유저로 activity 값이 변경됩니다.
         */

        validateSelfReport(reporterId, reportedUserId);
        User reportedUser = userRepository.findById(reportedUserId).orElseThrow(
                () -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));

        User reporter = getReporterEntityById(reporterId);
        if (userReportRepository.existsByReporterAndReportedUser(reporter,
                reportedUser)) {
            return;
        }
        userReportRepository.save(UserReportResponseDto.of(reporter, reportedUser, text));

        if (userReportRepository.countByReportedUser(reportedUser) > REPORT_LIMIT) {
            reportedUser.changeActivity(UserActivity.FLAGGED);
        }
    }

    @Transactional
    public void reportPost(Long reporterId, Long reportedBoardId, String text) {

         /*
            게시물 신고 기능
               - 신고할 컨텐츠가 존재하는지 체크합니다.
               - 본인이 작성한 게시물을 신고할 수 없습니다.
               - 신고할 컨첸츠가 이미 제한되었는지 체크합니다.
               - 이미 해당 컨텐츠를 신고했다면 신고가 접수되지 않습니다.
               - 신고당한 횟수가 REPORT_LIMIT 초과한다면 일반 컨텐츠에서 선정적인 컨첸츠로 분류됩니다.
          */

        Board reportedBoard = boardRepository.findById(reportedBoardId).orElseThrow(
                () -> new BoardExceptionHandler(ErrorCode.BOARD_NOT_FOUND));
        validateSelfReport(reporterId, reportedBoard.getUser().getId());
        validatedRestrictedContent(reportedBoard);

        User reporter = getReporterEntityById(reporterId);
        if (boardReportRepository.existsByReporterAndReportedBoard(reporter,
                reportedBoard)) {
            return;
        }
        boardReportRepository.save(BoardReportResponseDto.of(reporter, reportedBoard, text));

        if (boardReportRepository.countByReportedBoard(reportedBoard) > REPORT_LIMIT) {
            reportedBoard.changeActivity(BoardActivity.FLAGGED);
        }
    }

    private User getReporterEntityById(Long reporterId) {
        return userRepository.findById(reporterId).orElseThrow(
                () -> new ReportExceptionHandler(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUserBanned(User reportedUser) {
        if (reportedUser.getUserActivity() == UserActivity.BAN) {
            throw new ReportExceptionHandler(ErrorCode.USER_BANNED);
        }
    }

    private void validateSelfReport(Long reporterId, Long reportedUserId) {
        if (reporterId.equals(reportedUserId)) {
            throw new ReportExceptionHandler(ErrorCode.CANNOT_REPORT_YOURSELF);
        }
    }

    private void validatedRestrictedContent(Board reportedBoard) {
        if (reportedBoard.getBoardActivity() == BoardActivity.RESTRICTED) {
            throw new ReportExceptionHandler(ErrorCode.RESTRICTED_CONTENT);
        }
    }
}
