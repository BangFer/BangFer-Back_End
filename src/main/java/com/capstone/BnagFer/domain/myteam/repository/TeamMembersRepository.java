package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
@Repository
public interface TeamMembersRepository extends JpaRepository<TeamMember, Long> {
    @Query("select m " +
            "from TeamMember m " +
            "where m.team = :team and m.user= :user")
    TeamMember findByTeamAndUser(@Param("team") Team team, @Param("user") User user);

    @Query("select m " +
            "from TeamMember m " +
            "where m.team = :team")
    List<TeamMember> findByTeam(@Param("team") Team team);

    boolean existsByTeamAndPosition(Team team, Position position);

}