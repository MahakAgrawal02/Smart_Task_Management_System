package com.demo.dao;

import com.demo.enums.UserRole;
import lombok.Data;

/**
 * Data Transfer Object for user information.
 * Used for transferring user data between layers of the application.
 */
@Data
public class UserDto {

    /**
     * Unique identifier for the user.
     */
    private Long id;

    /**
     * Full name of the user.
     */
    private String name;

    /**
     * Email address used for user login and communication.
     */
    private String email;

    /**
     * Encrypted password of the user.
     */
    private String password;

    /**
     * Role assigned to the user (e.g., ADMIN, EMPLOYEE).
     */
    private UserRole userRole;
}
