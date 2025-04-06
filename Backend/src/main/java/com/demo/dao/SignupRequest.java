package com.demo.dao;

import lombok.Data;

/**
 * Data Transfer Object for handling user signup requests.
 */
@Data
public class SignupRequest {

    /**
     * Name of the user signing up.
     */
    private String name;

    /**
     * Email address of the user. Used as a unique identifier for login.
     */
    private String email;

    /**
     * Password chosen by the user.
     */
    private String password;
}
