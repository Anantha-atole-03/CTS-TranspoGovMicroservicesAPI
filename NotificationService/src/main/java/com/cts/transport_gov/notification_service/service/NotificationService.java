package com.cts.transport_gov.notification_service.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.transport_gov.notification_service.dto.NotificationCreateRequest;
import com.cts.transport_gov.notification_service.dto.NotificationResponse;
import com.cts.transport_gov.notification_service.enums.NotificationCategory;
import com.cts.transport_gov.notification_service.enums.NotificationScope;
import com.cts.transport_gov.notification_service.enums.NotificationStatus;
import com.cts.transport_gov.notification_service.exception.CitizenNotFoundException;
import com.cts.transport_gov.notification_service.exception.UserNotFoundException;
import com.cts.transport_gov.notification_service.feign.CitizenServiceClient;
import com.cts.transport_gov.notification_service.feign.UserServiceClient;
import com.cts.transport_gov.notification_service.model.Notification;
import com.cts.transport_gov.notification_service.repository.INotificationRepository;
import com.cts.transport_gov.notification_service.utils.MailTemplates;

import feign.FeignException;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

	private static final String TEST_USER_EMAIL = "apreamey2463@gmail.com";
	private static final String TEST_CITIZEN_EMAIL = "atoleanantha03@gmail.com";

	private static final String USER_NOT_FOUND_ID = "User not found with id";
	private static final String USER_NOT_FOUND_EMAIL = "User not found with email";
	private final INotificationRepository notificationRepository;

	private final ModelMapper modelMapper;
	private final JavaMailSender mailSender;
	private final UserServiceClient userServiceClient;
	private final CitizenServiceClient citizenServiceClient;

	/**
	 * Retrieves all notifications belonging to a specific user. Validates that the
	 * user exists, then fetches notifications and maps them into
	 * NotificationResponse DTOs.
	 *
	 * @param userId ID of the user whose notifications are needed
	 * @return List of notification responses for the user
	 */

	@Override
	public List<NotificationResponse> getUserNotifications(Long userId) {

		// 1️ Input validation
		if (userId == null || userId <= 0) {
			throw new IllegalArgumentException("Invalid userId. It must be a positive number.");
		}

		// 2️ Validate user existence via AuthenticationService
		try {
			userServiceClient.getUserById(userId);
		} catch (FeignException.NotFound ex) {
			// User does not exist
			throw new UserNotFoundException("User not found with id: " + userId);
		} catch (FeignException.ServiceUnavailable ex) {
			// Auth service is down or not registered
			throw new ServiceUnavailableException(
					"Authentication Service is currently unavailable. Please try again later.");
		}

		// 3 Fetch notifications (empty list is valid)
		return notificationRepository.fetchUserNotifications(userId).stream()
				.map(notification -> modelMapper.map(notification, NotificationResponse.class)).toList();
	}

	/**
	 * Retrieves all notifications belonging to a specific citizen. Validates that
	 * the citizen exists, fetches notifications, and maps them into
	 * NotificationResponse DTOs.
	 *
	 * @param citizenId ID of the citizen
	 * @return List of notification responses for the citizen
	 */

	@Override
	public List<NotificationResponse> getCitizenNotifications(Long citizenId) {

		// 1️⃣ Input validation
		if (citizenId == null || citizenId <= 0) {
			throw new IllegalArgumentException("Invalid citizenId. It must be a positive number.");
		}

		// 2️⃣ Validate citizen existence via CitizenService
		try {
			citizenServiceClient.getCitizenById(citizenId);
		} catch (FeignException.NotFound ex) {
			// Citizen does not exist
			throw new CitizenNotFoundException("Citizen not found with id: " + citizenId);
		} catch (FeignException.ServiceUnavailable ex) {
			// Citizen service is down / not registered
			throw new ServiceUnavailableException("Citizen Service is currently unavailable. Please try again later.");
		} catch (FeignException ex) {
			// Covers timeouts, 400, 500, network issues, etc.
			throw new ServiceUnavailableException("Unable to validate citizen at this time. Please try again later.");
		}

		// 3️⃣ Fetch notifications (empty list is VALID)
		return notificationRepository.fetchCitizenNotifications(citizenId).stream()
				.map(notification -> modelMapper.map(notification, NotificationResponse.class)).toList();
	}

	/**
	 * Marks a specific notification as READ.
	 *
	 * @param notificationId ID of the notification to update
	 * @return Updated notification as NotificationResponse
	 */

	@Override
	public NotificationResponse markAsRead(Long notificationId) {

		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new RuntimeException("Notification not found"));

		notification.setStatus(NotificationStatus.READ);

		return modelMapper.map(notificationRepository.save(notification), NotificationResponse.class);
	}

	/**
	 * Sends a manual notification from a user to another user based on email. Saves
	 * the notification and triggers an email with the provided message.
	 *
	 * @param request contains message, sender userId, and receiver email
	 * @return Saved notification mapped to NotificationResponse
	 */

	@Override
	public NotificationResponse pushNotification(NotificationCreateRequest request) {

		log.info("Pushing notification manually for email={}", request.getEmail());

		try {
			userServiceClient.getUserById(request.getUserId());
		} catch (FeignException.NotFound e) {
			throw new RuntimeException(USER_NOT_FOUND_ID);
		}

		try {
			userServiceClient.getUserByEmail(request.getEmail());
		} catch (FeignException.NotFound e) {
			throw new RuntimeException(USER_NOT_FOUND_EMAIL);
		}

		Notification notification = Notification.builder().userId(request.getUserId()).message(request.getMessage())
				.category(request.getCategory()).status(NotificationStatus.UNREAD).scope(NotificationScope.USER)
				.build();

		Notification savedNotification = notificationRepository.save(notification);

		String emailContent = MailTemplates.getGeneralNotificationTemplate(request.getEmail(), request.getMessage());

		sendEmail(request.getEmail(), "TranspoGov Notification", emailContent);

		return modelMapper.map(savedNotification, NotificationResponse.class);
	}

	/**
	 * Broadcasts a system-generated notification to all citizens.
	 *
	 * @param message Notification message to broadcast
	 */

	@Override
	public void sendAllCitizenNotification(String message) {

		log.info("Broadcasting notification to all citizens");

		Notification notification = Notification.builder().message(message).scope(NotificationScope.ALL_CITIZENS)
				.category(NotificationCategory.PROGRAM).status(NotificationStatus.UNREAD).build();

		notificationRepository.save(notification);
	}

	/**
	 * Broadcasts a system-generated notification to all registered system users.
	 *
	 * @param message Notification message to broadcast
	 */

	@Override
	public void sendAllUserNotification(String message) {

		log.info("Broadcasting notification to all users");

		Notification notification = Notification.builder().message(message).scope(NotificationScope.ALL_USERS)
				.category(NotificationCategory.ROUTE).status(NotificationStatus.UNREAD).build();

		notificationRepository.save(notification);
	}

	/**
	 * Sends a global notification intended for the entire system, including users,
	 * citizens, and admins.
	 *
	 * @param message Notification message
	 */

	@Override
	public void sendGlobalNotification(String message) {

		log.info("Broadcasting notification to entire system");

		Notification notification = Notification.builder().message(message).scope(NotificationScope.GLOBAL)
				.category(NotificationCategory.PROGRAM).status(NotificationStatus.UNREAD).build();

		notificationRepository.save(notification);
	}

	/**
	 * Sends an OTP notification to a citizen based on email. Saves the notification
	 * and emails the OTP.
	 *
	 * @param email Citizen’s email address
	 * @param otp   One-Time Password to be sent
	 */

	@Override
	public void sendOtpNotification(String email, String otp) {

		log.info("System generated OTP notification for email={}", email);

		try {
			citizenServiceClient.getCitizenByEmail(email);
		} catch (FeignException.NotFound e) {
			throw new RuntimeException("Citizen not found with email");
		}

		Notification notification = Notification.builder().message("OTP:" + otp).scope(NotificationScope.CITIZEN)
				.category(NotificationCategory.OTP).status(NotificationStatus.UNREAD).build();

		notificationRepository.save(notification);

		String emailContent = MailTemplates.getOtpTemplate(email, otp);

		sendEmail(email, "OTP Verification", emailContent);
	}

	/**
	 * Sends a ticket booking notification to a citizen. Includes route information
	 * and ticket ID.
	 *
	 * @param email    Citizen’s email address
	 * @param route    Route details
	 * @param ticketId Ticket identifier
	 */

	@Override
	public void sendTicketNotification(String email, String route, Long ticketId) {

		log.info("System generated Ticket notification for email={}", email);

		try {
			citizenServiceClient.getCitizenByEmail(email);
		} catch (FeignException.NotFound e) {
			throw new RuntimeException("Citizen not found with email");
		}

		Notification notification = Notification.builder().message(route).category(NotificationCategory.TICKET)
				.status(NotificationStatus.UNREAD).scope(NotificationScope.CITIZEN).build();

		notificationRepository.save(notification);

		String emailContent = MailTemplates.getTicketBookedTemplate(email, route, ticketId);

		sendEmail(email, "Ticket Booked", emailContent);
	}

	/**
	 * Sends a new program assignment notification to a system user.
	 *
	 * @param email       User’s email address
	 * @param title       Title of the program
	 * @param description Program description
	 */

	@Override
	public void sendProgramNotification(String email, String title, String description) {

		log.info("System generated Program notification for email={}", email);

		try {
			userServiceClient.getUserByEmail(email);
		} catch (FeignException.NotFound e) {
			throw new RuntimeException("User not found with email");
		}

		Notification notification = Notification.builder().message(title).category(NotificationCategory.PROGRAM)
				.status(NotificationStatus.UNREAD).scope(NotificationScope.USER).build();

		notificationRepository.save(notification);

		String emailContent = MailTemplates.getNewProgramTemplate(email, title, description);

		sendEmail(email, "Program Assigned", emailContent);
	}

	/**
	 * Sends a route update notification to a system user.
	 *
	 * @param email User's email
	 * @param route Updated route details
	 */

	@Override
	public void sendRouteUpdateNotification(String email, String route) {

		log.info("System generated Route update for email={}", email);

		// ✅ Validate user exists via Feign (no DB access)
		userServiceClient.getUserByEmail(email);

		// ✅ Build notification using IDs only
		Notification notification = Notification.builder().message(route).scope(NotificationScope.USER)
				.category(NotificationCategory.ROUTE).status(NotificationStatus.UNREAD).build();

		notificationRepository.save(notification);

		// ✅ Send email using request data (no User entity)
		String emailContent = MailTemplates.getRouteUpdateTemplate("User", route);

		sendEmail(email, "Route Updated", emailContent);
	}

	/**
	 * Sends a compliance-related alert to a user.
	 *
	 * @param email  User’s email address
	 * @param entity Compliance alert content
	 */

	@Override
	public void sendComplianceNotification(String email, String entity) {

		log.info("System generated Compliance alert for email={}", email);

		// ✅ Validate user exists via Feign (no DB access)
		userServiceClient.getUserByEmail(email);

		// ✅ Build notification WITHOUT User entity
		Notification notification = Notification.builder().message(entity).category(NotificationCategory.COMPLIANCE)
				.status(NotificationStatus.UNREAD).build();

		notificationRepository.save(notification);

		// ✅ Email without User entity dependency
		String emailContent = MailTemplates.getComplianceAlertTemplate("User", entity);

		sendEmail(email, "Compliance Alert", emailContent);
	}

	/**
	 * Internal reusable method for sending formatted HTML emails.
	 *
	 * @param to           Recipient email address
	 * @param subject      Email subject
	 * @param htmlTemplate HTML content of the email
	 */
	private void sendEmail(String to, String subject, String htmlTemplate) {

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlTemplate, true);
			helper.setFrom("notifications@transpogov.com");

			mailSender.send(mimeMessage);
			log.info("Email successfully sent to {}", to);

		} catch (Exception e) {
			log.error("Failed to send email to {}: {}", to, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Test method to trigger all notification flows. Useful for verifying email
	 * templates and system notifications.
	 */

	@Transactional
	@Override
	public void testAll() {
		// OTP Notification for Citizen
		sendOtpNotification(TEST_CITIZEN_EMAIL, "4567");

		// Ticket Booking Notification for Citizen
		sendTicketNotification(TEST_CITIZEN_EMAIL, "Bus Route 101", 1L);

		// Program Notification for User
		sendProgramNotification(TEST_USER_EMAIL, "Free Travel Program", "Eligible for free travel in Metro");

		// Route Update Notification for User
		sendRouteUpdateNotification(TEST_USER_EMAIL, "Route 55 Updated");

		// Compliance Alert Notification for User
		sendComplianceNotification(TEST_USER_EMAIL, "Ticket Fraud Alert");

		// Broadcast Notification to ALL Citizens
		sendAllCitizenNotification("New Transport Scheme Available");

		// Broadcast Notification to ALL Users
		sendAllUserNotification("Internal System Update");

		// Global Notification for Entire System
		sendGlobalNotification("System Maintenance Tonight");
	}
}