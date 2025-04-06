package com.demo.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.demo.dao.UserDto;
import com.demo.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entity class representing a user in the system.
 * Implements Spring Security's UserDetails interface for authentication.
 */
@Data
@Entity
public class User implements UserDetails {

    /**
     * Primary key - unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the user.
     */
    private String name;

    /**
     * Email address used as the username for login.
     */
    private String email;

    /**
     * Encrypted password of the user.
     */
    private String password;

    /**
     * Role of the user (e.g., ADMIN, USER).
     */
    private UserRole userRole;

    /**
     * Returns the authorities granted to the user (used by Spring Security).
     * Maps user role to a SimpleGrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    /**
     * Returns the email as the username for authentication.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     * Always returns true (not expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * Always returns true (not locked).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials have expired.
     * Always returns true (credentials valid).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     * Always returns true (user enabled).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Converts the User entity to a UserDto object, omitting the password.
     *
     * @return UserDto containing user information (excluding sensitive data).
     */
    public UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setUserRole(userRole);
        return userDto;
    }
}
