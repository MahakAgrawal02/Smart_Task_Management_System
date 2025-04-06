package com.demo.services.auth;

import com.demo.dao.SignupRequest;
import com.demo.dao.UserDto;

public interface AuthService {

    /**
     * Registers a new user based on the signup request data.
     *
     * @param signupRequest contains name, email, password, and role
     * @return the registered user's DTO
     */
    UserDto signupUser(SignupRequest signupRequest);

    /**
     * Checks if a user already exists with the given email.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean hasUserWithEmail(String email);
}
