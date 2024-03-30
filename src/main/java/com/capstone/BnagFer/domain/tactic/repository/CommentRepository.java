package com.capstone.BnagFer.domain.tactic.repository;

import com.capstone.BnagFer.domain.tactic.entity.TacticComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<TacticComment, Long> {
}
