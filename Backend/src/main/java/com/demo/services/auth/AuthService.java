package com.demo.services.auth;

import com.demo.dao.SignupRequest;
import com.demo.dao.UserDto;

public interface AuthService {
	
	UserDto signupUser(SignupRequest signupRequest);
	
	boolean hasUserWithEmail(String email);

}
