package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.myteam.dto.CreateTeamTacticResponseDto;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t WHERE t.tactic.tacticId = :tacticId")
    Optional<Team> findTeamsByTacticId(@Param("tacticId") Long tacticId);
}
