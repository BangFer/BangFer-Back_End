package com.capstone.BnagFer.domain.accounts.repository;

import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.report.entity.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, String provider);

    @Modifying
    @Query("DELETE FROM User u WHERE u.deleted = TRUE AND u.deletedAt < :dateTime")
    void deleteInactiveUsers(LocalDateTime dateTime);

    Page<User> findByUserActivity(UserActivity userActivity, Pageable pageable);
}
