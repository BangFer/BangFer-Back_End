package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamInviteRepository extends JpaRepository<TeamInvite, Long> {
    @Query("SELECT ti FROM TeamInvite ti JOIN FETCH ti.team t JOIN FETCH ti.invitedUser iu WHERE t = :team AND iu = :invitedUser")
    TeamInvite findByTeamAndInvitedUser(@Param("team") Team team, @Param("invitedUser") User invitedUser);
}
