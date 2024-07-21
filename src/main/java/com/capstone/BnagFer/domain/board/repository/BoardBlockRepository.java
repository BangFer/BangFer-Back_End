package com.capstone.BnagFer.domain.board.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.board.entity.BoardBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardBlockRepository extends JpaRepository<BoardBlock, Long> {
    boolean existsByBlockUserAndIsBlockedUser(User blockUser, User isBlockedUser);

    void deleteByBlockUserAndIsBlockedUser(User blockUser, User isBlockedUser);

    List<BoardBlock> findByBlockUser(User blockUser);

    @Query("SELECT bb.isBlockedUser.id FROM BoardBlock bb WHERE bb.blockUser.id = :userId")
    List<Long> findIsBlockUserIdsByBlockUserId(@Param("userId") Long userId);
}
