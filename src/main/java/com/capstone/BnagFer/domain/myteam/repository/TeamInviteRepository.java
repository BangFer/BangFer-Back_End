package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamInviteRepository extends JpaRepository<TeamInvite, Long> {
    List<TeamInvite> findByInvitedUser(User user);
}
