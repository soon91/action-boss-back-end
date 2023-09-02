package com.sparta.actionboss.domain.notification.service;

import com.sparta.actionboss.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationDeleteScheduler {
    private final NotificationRepository notificationRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void autoDeleteNotificationRepository() {
        // 30일 지난 알림 자동 삭제
        notificationRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(30));
    }
}
