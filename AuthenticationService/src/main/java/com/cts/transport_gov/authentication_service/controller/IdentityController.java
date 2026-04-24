package com.cts.transport_gov.authentication_service.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transport_gov.authentication_service.config.JwtUtil;
import com.cts.transport_gov.authentication_service.dtos.AuthRequest;
import com.cts.transport_gov.authentication_service.dtos.AuthResponse;
import com.cts.transport_gov.authentication_service.dtos.ForgotPasswordReq;
import com.cts.transport_gov.authentication_service.dtos.ResetPasswordReq;
import com.cts.transport_gov.authentication_service.dtos.UserRegistrationReq;
import com.cts.transport_gov.authentication_service.dtos.UserResponse;
import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.service.IdentityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class IdentityController {

    private final IdentityService identityService;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user into the system.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegistrationReq request) {
        log.info("Received registration request for email: {}", request.getEmail());
        return ResponseEntity.ok(identityService.registerUser(request));
    }

    /**
     * Authenticates user credentials and returns a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        log.info("Login attempt initiated for user: {}", req.getEmail());
        AuthResponse response = identityService.authenticate(req);
        log.info("User {} authenticated successfully.", req.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all system audit logs. Requires administrative access.
     */
    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getLogs(@RequestHeader("admin-id") Long adminId) {
        log.info("Audit logs requested by admin ID: {}", adminId);
        return ResponseEntity.ok(identityService.getAllLogs(adminId));
    }

    /**
     * Suspends a user account based on email.
     */
    @PutMapping("/suspend/{email}")
    public ResponseEntity<String> suspendUser(@PathVariable String email) {
        log.warn("Account suspension requested for: {}", email);
        return ResponseEntity.ok(identityService.suspendUser(email));
    }

    /**
     * Reactivates a previously suspended user account.
     */
    @PutMapping("/reactivate/{email}")
    public ResponseEntity<String> reactivateUser(@PathVariable String email) {
        log.info("Account reactivation requested for: {}", email);
        return ResponseEntity.ok(identityService.reactivateUser(email));
    }

    /**
     * Triggers a password reset email for the provided email address.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordReq req) {
        log.info("Forgot password request received for: {}", req.getEmail());
        return ResponseEntity.ok(identityService.forgotPassword(req.getEmail()));
    }

    /**
     * Resets the user password using a verified token/request.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordReq req) {
        log.info("Password reset execution attempted.");
        return ResponseEntity.ok(identityService.resetPassword(req));
    }

    /**
     * Validates the JWT token provided in the Authorization header.
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        log.info("Token validation requested by downstream service.");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            jwtUtil.validateToken(token);
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token); 
            log.debug("Token validated successfully for user: {}", email);
            return ResponseEntity.ok(new AuthResponse(token, email, role, "ACTIVE", "Token is valid"));
        } catch (Exception e) {
            log.error("Invalid or expired token rejected during validation check: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    /**
     * Fetches detailed profile information for a specific user ID.
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        log.info("Fetching user details for userId={}", userId);
        return ResponseEntity.ok(identityService.getUserById(userId));
    }
}