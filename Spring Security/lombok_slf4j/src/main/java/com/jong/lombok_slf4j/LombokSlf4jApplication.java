package com.jong.lombok_slf4j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class LombokSlf4jApplication {

	public static void main(String[] args) {
		log.info("Starting Lombok SLF4J Demo Application...");
		SpringApplication.run(LombokSlf4jApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReady() {
		log.info("=".repeat(60));
		log.info("🚀 Lombok SLF4J Demo Application Started Successfully!");
		log.info("📝 H2 Console: http://localhost:8080/h2-console");
		log.info("🌐 API Base URL: http://localhost:8080/api/users");
		log.info("📊 Available Endpoints:");
		log.info("  POST /api/users - Create user");
		log.info("  GET  /api/users - Get all active users");
		log.info("  GET  /api/users/{id} - Get user by ID");
		log.info("  GET  /api/users/username/{username} - Get user by username");
		log.info("  POST /api/users/{id}/login - Record login");
		log.info("  PUT  /api/users/{id}/deactivate - Deactivate user");
		log.info("  GET  /api/users/recent/{days} - Get recently logged users");
		log.info("=".repeat(60));
	}
}