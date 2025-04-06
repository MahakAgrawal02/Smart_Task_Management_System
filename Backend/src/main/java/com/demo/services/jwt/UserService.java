package com.demo.services.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
	
	UserDetailsService userDetailService();
	
}
