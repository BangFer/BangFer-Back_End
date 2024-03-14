package com.capstone.BnagFer.domain.tactic.repository;

import com.capstone.BnagFer.domain.tactic.dto.TacticResponse;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TacticRepository extends JpaRepository<Tactic, Long> {
    @Query("select p from Tactic p where p.id = :petitionId")
    Tactic findByTacticId(@Param("tacticId") Long tacticId);

    /*@Query("select p from Tactic p")
    List<TacticResponse.tacticList> findAllPetition();*/

    List<Tactic> findAll();
}
