package com.demo.dao;

import lombok.Data;

/**
 * DTO for capturing user login credentials.
 * Used in the /api/auth/login endpoint.
 */
@Data
public class AuthenticationRequest {
	
	/**
	 * User's email used for authentication.
	 */
	private String email;

	/**
	 * User's password used for authentication.
	 */
	private String password;
}
