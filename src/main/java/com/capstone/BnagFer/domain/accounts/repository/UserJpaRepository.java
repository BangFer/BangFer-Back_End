package com.capstone.BnagFer.domain.accounts.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, String provider);
}
