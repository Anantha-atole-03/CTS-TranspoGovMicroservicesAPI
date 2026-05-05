package com.cts.transport_gov.notification_service.service;


import java.util.List;

import com.cts.transport_gov.notification_service.dto.NotificationCreateRequest;
import com.cts.transport_gov.notification_service.dto.NotificationResponse;

public interface INotificationService {
    List<NotificationResponse> getUserNotifications(Long userId);
    NotificationResponse markAsRead(Long notificationId);
    NotificationResponse pushNotification(NotificationCreateRequest request);
	List<NotificationResponse> getCitizenNotifications(Long citizenId);

void sendOtpNotification(String email, String otp);

    void sendTicketNotification(String email, String route, Long ticketId);

    void sendProgramNotification(String email, String title, String description);

    void sendRouteUpdateNotification(String email, String route);

    void sendComplianceNotification(String email, String entity);

    void sendAllCitizenNotification(String message);

    void sendAllUserNotification(String message);

    void sendGlobalNotification(String message);
	void testAll();


}