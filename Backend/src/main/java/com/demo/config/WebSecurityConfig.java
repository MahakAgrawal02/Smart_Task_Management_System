package com.demo.config;

import com.demo.enums.UserRole;
import com.demo.services.jwt.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	// Inject custom JWT authentication filter
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	// Inject custom user service
	private final UserService userService;

	// Define the security filter chain
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// Disable CSRF protection for stateless API
			.csrf(AbstractHttpConfigurer::disable)

			// Enable default CORS configuration
			.cors(cors -> {})

			// Define authorization rules for different endpoints
			.authorizeHttpRequests(request -> request
				// Allow public access to authentication endpoints
				.requestMatchers("/api/auth/**").permitAll()

				// Allow only ADMIN role to access admin endpoints
				.requestMatchers("/api/admin/**").hasAuthority(UserRole.ADMIN.name())

				// Allow only EMPLOYEE role to access employee endpoints
				.requestMatchers("/api/employee/**").hasAuthority(UserRole.EMPLOYEE.name())

				// Require authentication for all other requests
				.anyRequest().authenticated()
			)

			// Configure session management as stateless (no HTTP session)
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

			// Set custom authentication provider
			.authenticationProvider(authenticationProvider())

			// Add custom JWT filter before default username/password authentication filter
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Configure CORS settings
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// Allow requests from the Angular frontend running on localhost:4200
		config.setAllowedOrigins(List.of("http://localhost:4200"));

		// Allow specific HTTP methods
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		// Allow all headers
		config.setAllowedHeaders(List.of("*"));

		// Allow credentials such as cookies and authorization headers
		config.setAllowCredentials(true);

		// Apply the CORS configuration to all paths
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	// Define password encoder using BCrypt hashing
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Define authentication provider using DAO pattern and custom user service
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		// Set user details service for authentication
		authProvider.setUserDetailsService(userService.userDetailService());

		// Set password encoder for comparing hashed passwords
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	// Provide authentication manager from Spring configuration
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
