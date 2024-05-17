package com.capstone.BnagFer.domain.board.repository;

import com.capstone.BnagFer.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
