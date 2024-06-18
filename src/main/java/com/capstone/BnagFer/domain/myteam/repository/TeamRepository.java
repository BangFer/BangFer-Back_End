package com.capstone.BnagFer.domain.myteam.repository;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t JOIN t.teamMembers tm WHERE tm IN :teamMembers")
    List<Team> findByTeamMembers(@Param("teamMembers") List<TeamMember> teamMembers);
}
