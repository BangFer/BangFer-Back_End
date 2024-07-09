package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import com.capstone.BnagFer.domain.tactic.entity.Position;
import com.capstone.BnagFer.domain.tactic.entity.TacticPositionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TeamMembersRepository extends JpaRepository<TeamMember, Long> {
    @Query("select m from TeamMember m " +
            "join fetch m.user u " + // User 정보를 즉시 로드
            "join fetch m.team t " + // Team 정보를 즉시 로드
            "where m.team = :team and m.user = :user")
    TeamMember findByTeamAndUser(@Param("team") Team team, @Param("user") User user);

    @Query("select m from TeamMember m join fetch m.user where m.team = :team")
    List<TeamMember> findByTeam(@Param("team") Team team);
    TeamMember findByTeamAndPosition(Team team, Position position);

    boolean existsByTeamAndId(Team team, Long id);

    List<TeamMember> findByUser(User user);


}