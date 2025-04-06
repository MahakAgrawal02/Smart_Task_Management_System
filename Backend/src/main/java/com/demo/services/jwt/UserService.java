package com.demo.services.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService interface for loading user-specific data
 * used by Spring Security during authentication.
 */
public interface UserService {
	
	/**
	 * Returns a UserDetailsService used to fetch user details by username (email).
	 *
	 * @return UserDetailsService instance
	 */
	UserDetailsService userDetailService();
	
}
