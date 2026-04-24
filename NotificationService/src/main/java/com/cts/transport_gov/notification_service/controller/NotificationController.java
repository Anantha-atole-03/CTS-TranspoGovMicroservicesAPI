package com.cts.transport_gov.notification_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.notification_service.dto.ComplianceNotificationRequest;
import com.cts.transport_gov.notification_service.dto.NotificationCreateRequest;
import com.cts.transport_gov.notification_service.dto.NotificationResponse;
import com.cts.transport_gov.notification_service.dto.OtpNotificationRequest;
import com.cts.transport_gov.notification_service.dto.ProgramNotificationRequest;
import com.cts.transport_gov.notification_service.dto.RouteNotificationRequest;
import com.cts.transport_gov.notification_service.dto.TicketNotificationRequest;
import com.cts.transport_gov.notification_service.service.INotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notification")
@Transactional
public class NotificationController {

	private final INotificationService notificationService;

	public NotificationController(INotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/*
	 * Method: GET Argument: userId (Long) Description: Fetches all notifications
	 * for the given user by calling getUserNotifications() service method
	 * Return:ResponseEntity<ApiResponse<List<NotificationResponse>>> type
	 */
	// GET /notifications?userId=123
	@GetMapping("/user")
	public ResponseEntity<List<NotificationResponse>> getUserNotifications(@RequestParam Long userId) {
		log.info("Fetching user notifications");
		List<NotificationResponse> notifications = notificationService.getUserNotifications(userId);
		return ResponseEntity.ok(notifications);
	}

	@GetMapping("/citizen")
	public ResponseEntity<List<NotificationResponse>> getCitizenNotifications(@RequestParam Long userId) {
		log.info("Fetching user notifications");
		List<NotificationResponse> notifications = notificationService.getCitizenNotifications(userId);
		return ResponseEntity.ok(notifications);
	}

	/*
	 * PATCH /notifications/{notificationId} Method: PATCH Argument: notificationId
	 * (Long) Description: Marks a specific notification as read by calling
	 * markAsRead() service method
	 * Return:ResponseEntity<ApiResponse<NotificationResponse>> type
	 */

	@PatchMapping("/{notificationId}")
	public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long notificationId) {
		log.info("User Read the notifications");
		NotificationResponse response = notificationService.markAsRead(notificationId);
		return ResponseEntity.ok(response);
	}

	/*
	 * POST /notifications Method: POST Argument: NotificationCreateRequest DTO
	 * Description: Accepts NotificationCreateRequest DTO and calls
	 * pushNotification() method to create a new notification Return:
	 * ResponseEntity<ApiResponse<NotificationResponse>>type
	 */

	@PostMapping("/save")
	public ResponseEntity<NotificationResponse> pushNotification(@RequestBody NotificationCreateRequest request) {
		log.info("User got the New notifications");
		NotificationResponse response = notificationService.pushNotification(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/systemnotification/testall")
	public ResponseEntity<String> testAllSystemNotifications() {

		log.info("Testing all system generated notifications");

		notificationService.testAll();

		return ResponseEntity.ok("All System Notifications Sent Successfully");
	}

	@PostMapping("/ticket")
	public ResponseEntity<String> sendTicketNotification(@RequestBody TicketNotificationRequest request) {

		notificationService.sendTicketNotification(request.getEmail(), request.getRoute(), request.getTicketId());

		return ResponseEntity.ok("Ticket notification sent");
	}

	@PostMapping("/program")
	public ResponseEntity<String> sendProgramNotification(@RequestBody ProgramNotificationRequest request) {

		notificationService.sendProgramNotification(request.getEmail(), request.getTitle(), request.getDescription());

		return ResponseEntity.ok("Program notification sent");
	}

	@PostMapping("/route")
	public ResponseEntity<String> sendRouteNotification(@RequestBody RouteNotificationRequest request) {

		notificationService.sendRouteUpdateNotification(request.getEmail(), request.getRoute());

		return ResponseEntity.ok("Route update notification sent");
	}

	@PostMapping("/compliance")
	public ResponseEntity<String> sendComplianceNotification(@RequestBody ComplianceNotificationRequest request) {

		notificationService.sendComplianceNotification(request.getEmail(), request.getEntity());

		return ResponseEntity.ok("Compliance notification sent");
	}

	@PostMapping("/otp")
	public ResponseEntity<String> sendOtpNotification(@RequestBody OtpNotificationRequest request) {

		notificationService.sendOtpNotification(request.getEmail(), request.getOtp());

		return ResponseEntity.ok("OTP notification sent");
	}

}
