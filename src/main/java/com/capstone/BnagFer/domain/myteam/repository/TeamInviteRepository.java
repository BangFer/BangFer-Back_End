package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamInviteRepository extends JpaRepository<TeamInvite, Long> {
    List<TeamInvite> findByInvitedUser(User user);

    @Query("SELECT COUNT(ti) > 0 FROM TeamInvite ti WHERE ti.team = :team AND ti.invitedUser = :invitedUser")
    boolean existsByTeamAndInvitedUser(@Param("team") Team team, @Param("invitedUser") User invitedUser);
}
