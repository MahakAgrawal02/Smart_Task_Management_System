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
@CrossOrigin("*") // Allows cross-origin requests from all domains
@Slf4j
public class AuthController {

    // Injecting required services and repositories
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // Endpoint for user signup
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        log.info("Received signup request for email: {}", signupRequest.getEmail());

        // Check if user already exists with the provided email
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            log.warn("Signup failed: Email already exists - {}", signupRequest.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this email");
        }

        // Attempt to create new user
        UserDto createdUserDto = authService.signupUser(signupRequest);

        // If creation fails, respond with bad request
        if (createdUserDto == null) {
            log.error("Signup failed for email: {}", signupRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
        }

        // Return success response with created user info
        log.info("User created successfully: {}", createdUserDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    // Endpoint for user login
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Login attempt for email: {}", authenticationRequest.getEmail());

        try {
            // Authenticate the user using Spring Security
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            // Handle incorrect credentials
            log.error("Login failed for email: {}", authenticationRequest.getEmail());
            throw new BadCredentialsException("Incorrect username or password");
        }

        // Load authenticated user's details
        final UserDetails userDetails = userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());

        // Generate JWT token after successful authentication
        final String jwtToken = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        // If user is found, populate authentication response
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            authenticationResponse.setJwt(jwtToken);
            authenticationResponse.setUserId(user.getId());
            authenticationResponse.setUserRole(user.getUserRole());
            log.info("Login successful for user ID: {}, role: {}", user.getId(), user.getUserRole());
        } else {
            // Log warning if user not found despite successful authentication
            log.warn("User not found after successful authentication: {}", authenticationRequest.getEmail());
        }

        // Return response containing JWT and user details
        return authenticationResponse;
    }
}
