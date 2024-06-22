package com.capstone.BnagFer.domain.report.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.Board;
import com.capstone.BnagFer.domain.report.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
    boolean existsByReporterAndReportedBoard(User reporter, Board reportedBoard);
    Long countByReportedBoard(Board reportedBoard);
}
