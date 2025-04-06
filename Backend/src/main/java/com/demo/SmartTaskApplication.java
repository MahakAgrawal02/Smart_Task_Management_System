package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Smart Task Management System application.
 * 
 * The @SpringBootApplication annotation enables:
 * - Auto-configuration
 * - Component scanning
 * - Configuration properties support
 */
@SpringBootApplication
public class SmartTaskApplication {

	/**
	 * Main method that bootstraps the Spring Boot application.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(SmartTaskApplication.class, args);
	}
}
