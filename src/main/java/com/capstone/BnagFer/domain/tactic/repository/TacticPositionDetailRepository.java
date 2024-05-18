package com.capstone.BnagFer.domain.tactic.repository;

import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TacticPositionDetailRepository extends JpaRepository<TacticPositionDetail, Long> {
    @Transactional
    void deleteByTactic_TacticId(Long tacticId);
}
