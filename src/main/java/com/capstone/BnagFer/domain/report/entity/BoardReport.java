package com.capstone.BnagFer.domain.report.entity;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SoftDelete(columnName = "deleted_at")
@Table(name = "report_user_report")
public class BoardReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_board_id")
    private Board reportedBoard;

    @Column
    private String content;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PostPersist
    private void setCreatedAt() {
        reportedAt = LocalDateTime.now();
    }

}