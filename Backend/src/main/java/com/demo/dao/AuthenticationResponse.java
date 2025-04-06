package com.demo.dao;

import com.demo.enums.UserRole;

import lombok.Data;

@Data
public class AuthenticationResponse {
	
	private String jwt;
	private long userId;
	private UserRole userRole;

}
