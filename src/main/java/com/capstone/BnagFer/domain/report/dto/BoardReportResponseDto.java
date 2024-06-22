package com.capstone.BnagFer.domain.report.dto;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.report.entity.BoardReport;
import lombok.Builder;

@Builder
public record BoardReportResponseDto(
        User reporter,
        Board reportedBoard,
        String content
) {
    public static BoardReport of(User reporter, Board reportedBoard, String content) {
        return BoardReport.builder()
                .reporter(reporter)
                .reportedBoard(reportedBoard)
                .content(content)
                .build();
    }
}
