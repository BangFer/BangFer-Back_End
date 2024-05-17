package com.capstone.BnagFer.domain.board.repository;

import com.capstone.BnagFer.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
