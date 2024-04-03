package com.capstone.BnagFer.domain.accounts.repository;

import com.capstone.BnagFer.domain.accounts.entity.Profile;
import com.capstone.BnagFer.domain.accounts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByUser(User user);

    Optional<Profile> findByUser(User user);

    Optional<Profile> findByUserId(Long userId);
}
