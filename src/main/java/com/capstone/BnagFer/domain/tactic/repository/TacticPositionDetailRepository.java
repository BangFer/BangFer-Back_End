package com.capstone.BnagFer.domain.tactic.repository;

import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacticPositionDetailRepository extends JpaRepository<TacticPositionDetail, Long> {
    void deleteByTactic_TacticId(Long tacticId);
}
