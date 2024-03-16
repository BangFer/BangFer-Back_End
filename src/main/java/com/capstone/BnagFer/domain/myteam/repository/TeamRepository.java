package com.capstone.BnagFer.domain.myteam.repository;

import com.capstone.BnagFer.domain.myteam.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
