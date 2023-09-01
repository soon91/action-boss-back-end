package com.sparta.actionboss.domain.notification.repository;

import com.sparta.actionboss.domain.notification.entity.Notification;
import com.sparta.actionboss.domain.post.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT i FROM Notification i WHERE i.recipient.userId = :userId")
    List<Notification> findNotificationByUserId(@Param("userId") Long userId);

    void deleteByCreatedAtBefore(LocalDateTime localDateTime);
}
