package com.capstone.BnagFer.domain.report.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    boolean existsByReporterAndReportedUser(User reporter, User reportedUser);
    Long countByReportedUser(User reportedUser);

    Page<UserReport> findByReportedUser(User reportedUser, Pageable pageable);
}
