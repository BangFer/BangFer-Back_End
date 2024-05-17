package com.capstone.BnagFer.domain.tactic.repository;

import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TacticRepository extends JpaRepository<Tactic, Long> {
    /*List<Tactic> findAllByAnonymousFalse();*/

    Page<Tactic> findAllByAnonymousFalse(Pageable pageable);
}
