package com.demo.enums;

/**
 * Enum representing user roles in the system.
 */
public enum UserRole {

    /**
     * ADMIN users have full access to manage tasks, users, and system settings.
     */
    ADMIN,

    /**
     * EMPLOYEE users have restricted access to view and update only their assigned tasks.
     */
    EMPLOYEE
}
