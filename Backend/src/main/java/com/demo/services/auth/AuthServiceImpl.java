package com.demo.services.auth;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.dao.SignupRequest;
import com.demo.dao.UserDto;
import com.demo.entities.User;
import com.demo.enums.UserRole;
import com.demo.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    /**
     * Creates a default admin account if it does not already exist.
     * This method runs automatically after the bean is initialized.
     */
    @PostConstruct
    public void createAnAdminAccount() {
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin")); // Encrypting default admin password
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.println("Admin account created successfully!");
        } else {
            System.out.println("Admin account already exists!");
        }
    }

    /**
     * Registers a new user with role EMPLOYEE based on the signup request.
     *
     * @param signupRequest contains name, email, and password
     * @return UserDto of the newly created user
     */
    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword())); // Encrypt password
        user.setUserRole(UserRole.EMPLOYEE); // Assign default role as EMPLOYEE
        User createdUser = userRepository.save(user);
        return createdUser.getUserDto();
    }

    /**
     * Checks if a user already exists with the given email.
     *
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
