package com.capstone.BnagFer.domain.board.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.BoardBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardBlockRepository extends JpaRepository<BoardBlock, Long> {
    boolean existsByBlockUserAndIsBlockedUser(User blockUser, User isBlockedUser);

    void deleteByBlockUserAndIsBlockedUser(User blockUser, User isBlockedUser);
}
