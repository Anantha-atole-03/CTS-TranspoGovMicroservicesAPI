package com.cts.transport_gov.notification_service.utils;


public class MailTemplates {

	private MailTemplates() {
		throw new IllegalStateException("Utility class");
	}

	// ✅ Common Wrapper Template
	private static String baseTemplate(String name, String bodyContent) {

		return "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<div style='max-width:600px;margin:auto;border:1px solid #ddd;border-radius:8px;'>"
				+ "<div style='background:#004a99;color:white;padding:20px;text-align:center;'>"
				+ "<h2>TranspoGov Notification</h2>" + "</div>" + "<div style='padding:20px;'>" + "<p>Dear " + name
				+ ",</p>" + bodyContent + "<br><p>Regards,<br>TranspoGov Team</p>" + "</div>"
				+ "<div style='background:#f4f4f4;text-align:center;padding:10px;font-size:12px;'>"
				+ "&copy; 2026 TranspoGov" + "</div>" + "</div>" + "</body></html>";
	}

	// ✅ Ticket Booking Mail
	public static String getTicketBookedTemplate(String name, String routeTitle, Long ticketId) {

		String content = "<p>Your ticket has been successfully booked.</p>" + "<p><b>Route:</b> " + routeTitle + "</p>"
				+ "<p><b>Ticket ID:</b> " + ticketId + "</p>";

		return baseTemplate(name, content);
	}

	// ✅ New Program Mail
	public static String getNewProgramTemplate(String name, String programTitle, String description) {

		String content = "<p>You have been enrolled in a new transport program.</p>" + "<p><b>Program:</b> "
				+ programTitle + "</p>" + "<p>" + description + "</p>";

		return baseTemplate(name, content);
	}

	// ✅ Compliance Alert Mail
	public static String getComplianceAlertTemplate(String name, String entityType) {

		String content = "<p>A compliance issue has been detected for:</p>" + "<p><b>Entity:</b> " + entityType
				+ "</p>";

		return baseTemplate(name, content);
	}

	// ✅ Route Update Mail
	public static String getRouteUpdateTemplate(String name, String routeTitle) {

		String content = "<p>Your selected route has been updated.</p>" + "<p><b>Route:</b> " + routeTitle + "</p>";

		return baseTemplate(name, content);
	}

	// ✅ OTP Generation Mail
	public static String getOtpTemplate(String name, String otp) {

		String content = "<p>Your One-Time Password (OTP) is:</p>" + "<h2 style='color:#004a99;'>" + otp + "</h2>"
				+ "<p>This OTP is valid for 5 minutes.</p>";

		return baseTemplate(name, content);
	}

	// ✅ User ID / Account Creation Mail
	public static String getUserCreationTemplate(String name, String phone, String password) {

		String content = "<p>Your TranspoGov account has been successfully created.</p>" + "<p><b>Username:</b> "
				+ phone + "</p>" + "<p><b>Password:</b> " + password + "</p>"
				+ "<p>Please use this ID to login into the system.</p>";

		return baseTemplate(name, content);
	}

	// ✅ General Notification Mail
	public static String getGeneralNotificationTemplate(String name, String message) {

		String content = "<p>" + message + "</p>";

		return baseTemplate(name, content);
	}
}