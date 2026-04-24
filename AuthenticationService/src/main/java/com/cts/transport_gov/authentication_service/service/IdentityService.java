package com.cts.transport_gov.authentication_service.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.transport_gov.authentication_service.dtos.AuthRequest;
import com.cts.transport_gov.authentication_service.dtos.AuthResponse;
import com.cts.transport_gov.authentication_service.dtos.ResetPasswordReq;
import com.cts.transport_gov.authentication_service.dtos.UserRegistrationReq;
import com.cts.transport_gov.authentication_service.dtos.UserResponse;
import com.cts.transport_gov.authentication_service.model.AuditLog;
import com.cts.transport_gov.authentication_service.model.User;


/**
 * COMMAND: Orchestrate all Identity and Access Management (IAM) operations.
 * Logic: Acts as the primary service layer to handle user lifecycle, authentication security, 
 * audit persistence, and credential recovery.
 */
public interface IdentityService {

    // Interfaces require manual logger initialization to be used in default methods or for consistency
    Logger log = LoggerFactory.getLogger(IdentityService.class);

    /**
     * Registers a new user, encodes their password, and creates an initial audit log entry.
     * @param req The registration request containing user details.
     * @return UserResponse A DTO containing the saved user's safe details.
     */
    UserResponse registerUser(UserRegistrationReq req);

    /**
     * Authenticates a user based on their email and password.
     * If successful, generates a JWT token and logs the login action in the database.
     * @param req The authentication request containing the email and raw password.
     * @return AuthResponse containing the JWT token and basic user details.
     */
    AuthResponse authenticate(AuthRequest req);

    /**
     * Creates and saves an audit log entry for a specific user action.
     * @param user The user performing the action.
     * @param action A string description of the action.
     * @param resource The module or resource being accessed.
     */
    void logAction(User user, String action, String resource);

    /**
     * Retrieves a list of all system audit logs. This action is restricted to Administrators.
     * @param adminId The ID of the user requesting the logs.
     * @return List of AuditLog entities.
     */
    List<AuditLog> getAllLogs(Long adminId);

    /**
     * Suspends a user account by changing their status to INACTIVE.
     * @param email The email address of the user to suspend.
     * @return A success message confirming the suspension.
     */
    String suspendUser(String email);

    /**
     * Reactivates a suspended user account by changing their status back to ACTIVE.
     * @param email The email address of the user to reactivate.
     * @return A success message confirming the reactivation.
     */
    String reactivateUser(String email);

    /**
     * Initiates the forgot password flow by generating a secure token.
     * @param email The email address associated with the account.
     * @return A generic success message.
     */
    String forgotPassword(String email);

    /**
     * Resets a user's password using a valid, unexpired reset token.
     * @param req The request containing the token and the new password.
     * @return A success message confirming the password update.
     */
    String resetPassword(ResetPasswordReq req);
    
    /**
     * Retrieves user details by ID for profile management.
     * @param userId Unique identifier of the user.
     * @return UserResponse safe DTO.
     */
    UserResponse getUserById(Long userId);

}