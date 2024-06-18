package com.capstone.BnagFer.domain.board.repository;

import com.capstone.BnagFer.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCommentRepository extends JpaRepository<Comment, Long> {
    long countByBoardId(Long boardId);
}
