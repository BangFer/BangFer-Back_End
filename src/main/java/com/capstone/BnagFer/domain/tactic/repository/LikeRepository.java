package com.capstone.BnagFer.domain.tactic.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.tactic.entity.Tactic;
import com.capstone.BnagFer.domain.tactic.entity.TacticLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<TacticLike, Long> {
    Optional<TacticLike> findByUserAndTactic(User user, Tactic tactic);
}
