package com.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entities.User;
import com.demo.enums.UserRole;

/**
 * Repository interface for User entity.
 * Provides CRUD operations and custom query methods related to users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds the first user with the given email.
     * This is typically used for authentication and login.
     *
     * @param email the email of the user
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<User> findFirstByEmail(String email);

    /**
     * Finds a user by their role (e.g., ADMIN, EMPLOYEE).
     * Useful for role-based operations.
     *
     * @param userRole the role of the user
     * @return an Optional containing the user with the given role, or empty if none found
     */
    Optional<User> findByUserRole(UserRole userRole);

}
