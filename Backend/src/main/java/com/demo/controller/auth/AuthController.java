package com.demo.controller.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.demo.dao.AuthenticationRequest;
import com.demo.dao.AuthenticationResponse;
import com.demo.dao.SignupRequest;
import com.demo.dao.UserDto;
import com.demo.entities.User;
import com.demo.repositories.UserRepository;
import com.demo.services.auth.AuthService;
import com.demo.services.jwt.UserService;
import com.demo.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class AuthController {
	
	private final AuthService authService;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
		log.info("Received signup request for email: {}", signupRequest.getEmail());

		if (authService.hasUserWithEmail(signupRequest.getEmail())) {
			log.warn("Signup failed: Email already exists - {}", signupRequest.getEmail());
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this email");
		}
		
		UserDto createdUserDto = authService.signupUser(signupRequest);
		
		if (createdUserDto == null) {
			log.error("Signup failed for email: {}", signupRequest.getEmail());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
		}
		
		log.info("User created successfully: {}", createdUserDto.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
		log.info("Login attempt for email: {}", authenticationRequest.getEmail());

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getEmail(),
							authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			log.error("Login failed for email: {}", authenticationRequest.getEmail());
			throw new BadCredentialsException("Incorrect username or password");
		}

		final UserDetails userDetails = userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
		Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());
		final String jwtToken = jwtUtil.generateToken(userDetails);
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			authenticationResponse.setJwt(jwtToken);
			authenticationResponse.setUserId(user.getId());
			authenticationResponse.setUserRole(user.getUserRole());
			log.info("Login successful for user ID: {}, role: {}", user.getId(), user.getUserRole());
		} else {
			log.warn("User not found after successful authentication: {}", authenticationRequest.getEmail());
		}

		return authenticationResponse;
	}
}
