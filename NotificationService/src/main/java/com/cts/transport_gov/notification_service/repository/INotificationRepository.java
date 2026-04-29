package com.cts.transport_gov.notification_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.notification_service.model.Notification;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
        SELECT n FROM Notification n
        WHERE
        (n.userId = :userId)
        OR
        (n.scope = 'ALL_USERS')
        OR
        (n.scope = 'GLOBAL')
    """)
    List<Notification> fetchUserNotifications(Long userId);

    @Query("""
        SELECT n FROM Notification n
        WHERE
        (n.citizenId = :citizenId)
        OR
        (n.scope = 'ALL_CITIZENS')
        OR
        (n.scope = 'GLOBAL')
    """)
    List<Notification> fetchCitizenNotifications(Long citizenId);
}
