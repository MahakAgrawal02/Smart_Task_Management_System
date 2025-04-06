package com.demo.config;

import com.demo.services.jwt.UserService;
import com.demo.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	// Injecting JWT utility for token operations
	private final JwtUtil jwtUtil;

	// Injecting custom UserService to load user details
	private final UserService userService;

	// This method is called once per request to filter and validate JWT
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, 
	                                @NonNull HttpServletResponse response, 
	                                @NonNull FilterChain filterChain) throws ServletException, IOException {
		// Get the Authorization header from the HTTP request
		final String authHeader = request.getHeader("Authorization");

		final String jwt;
		final String userEmail;

		// If the Authorization header is missing or doesn't start with "Bearer ", continue the filter chain
		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Extract the JWT token by removing the "Bearer " prefix
		jwt = authHeader.substring(7);

		// Extract username (email) from the JWT token
		userEmail = jwtUtil.extractUserName(jwt);

		// If the username is valid and there's no authentication already present
		if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Load user details from the UserService
			UserDetails userDetails = userService.userDetailService().loadUserByUsername(userEmail);

			// Validate the token against the loaded user details
			if (jwtUtil.isTokenValid(jwt, userDetails)) {
				// Create a new empty security context
				SecurityContext context = SecurityContextHolder.createEmptyContext();

				// Create an authentication token with user details and authorities
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				// Set additional authentication details using the request info
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set the authentication token into the security context
				context.setAuthentication(authToken);

				// Store the updated context into the SecurityContextHolder
				SecurityContextHolder.setContext(context);
			}
		}

		// Continue with the remaining filters in the chain
		filterChain.doFilter(request, response);
	}
}
