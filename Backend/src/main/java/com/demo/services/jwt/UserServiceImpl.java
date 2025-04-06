package com.demo.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of the UserService interface.
 * Provides user details for Spring Security authentication.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	/**
	 * Returns a custom UserDetailsService implementation that
	 * retrieves user details by email from the database.
	 *
	 * @return UserDetailsService
	 */
	@Override
	public UserDetailsService userDetailService() {
		return new UserDetailsService() {

			/**
			 * Loads a user by username (email).
			 *
			 * @param username the email of the user
			 * @return UserDetails of the authenticated user
			 * @throws UsernameNotFoundException if user is not found
			 */
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userRepository.findFirstByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			}
			
		};
	}
	
}
