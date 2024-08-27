package com.capstone.BnagFer.domain.notification.repository;

import com.capstone.BnagFer.domain.notification.entity.FcmNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FcmNotificationRepository extends JpaRepository<FcmNotification, Long> {
    @Query("SELECT n FROM FcmNotification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<FcmNotification> findRecentByUserId(@Param("userId") Long userId);
}
