package com.demo.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.demo.entities.User;
import com.demo.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for managing JWT (JSON Web Token) operations such as
 * token generation, validation, extraction, and retrieving authenticated user details.
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {
	
	private final UserRepository userRepository;

	/**
	 * Generates a JWT token for the given user details.
	 * 
	 * @param userDetails the authenticated user details
	 * @return a signed JWT token
	 */
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	/**
	 * Generates a JWT token with custom claims.
	 * 
	 * @param extraClaims additional claims to include
	 * @param userDetails the authenticated user details
	 * @return a signed JWT token
	 */
	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder()
			.setClaims(extraClaims)
			.setSubject(userDetails.getUsername())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * Returns the secret signing key used for token generation and validation.
	 * 
	 * @return HMAC SHA-256 signing key
	 */
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode("413F442847284862506553685660597033733676397924422645294848406351");
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Validates the token by comparing username and checking expiry.
	 * 
	 * @param token the JWT token
	 * @param userDetails the authenticated user details
	 * @return true if the token is valid, false otherwise
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	/**
	 * Extracts the username (subject) from the JWT token.
	 * 
	 * @param token the JWT token
	 * @return username (email)
	 */
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Checks if the token is expired.
	 * 
	 * @param token the JWT token
	 * @return true if token is expired, false otherwise
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Extracts the expiration date from the JWT token.
	 * 
	 * @param token the JWT token
	 * @return expiration date
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Extracts a specific claim from the JWT token.
	 * 
	 * @param <T> claim type
	 * @param token the JWT token
	 * @param claimsResolver function to extract the claim
	 * @return extracted claim
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Extracts all claims from the JWT token.
	 * 
	 * @param token the JWT token
	 * @return all claims
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	/**
	 * Retrieves the currently logged-in user from the security context.
	 * 
	 * @return authenticated User object, or null if not found
	 */
	public User getLoggedInUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated()) {
	        User user = (User) authentication.getPrincipal();
	        Optional<User> optionalUser = userRepository.findById(user.getId());
	        return optionalUser.orElse(null);
	    }

	    return null;
	}
}
