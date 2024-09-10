package com.capstone.BnagFer.domain.board.entity;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "board_block")
public class BoardBlock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_block_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_user_id")
    private User blockUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "is_blocked_user_id")
    private User isBlockedUser;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    public static BoardBlock create(User blockUser, User isBlockedUser) {
        BoardBlock boardBlock = new BoardBlock();
        boardBlock.blockUser = blockUser;
        boardBlock.isBlockedUser = isBlockedUser;
        return boardBlock;

    }
}
