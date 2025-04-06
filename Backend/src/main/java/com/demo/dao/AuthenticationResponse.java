package com.demo.dao;

import com.demo.enums.UserRole;
import lombok.Data;

/**
 * DTO for sending the response after successful authentication.
 * Contains JWT token and user information.
 */
@Data
public class AuthenticationResponse {
	
	/**
	 * The generated JSON Web Token for the authenticated user.
	 */
	private String jwt;

	/**
	 * The unique ID of the authenticated user.
	 */
	private long userId;

	/**
	 * The role of the authenticated user (e.g., ADMIN, EMPLOYEE).
	 */
	private UserRole userRole;
}
