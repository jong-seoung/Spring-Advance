package com.jong.lombok_slf4j.controller;

import com.jong.lombok_slf4j.dto.UserCreateRequest;
import com.jong.lombok_slf4j.dto.UserResponse;
import com.jong.lombok_slf4j.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("Received request to create user: {}", request.getUsername());

        try {
            UserResponse response = userService.createUser(request);
            log.info("User creation successful for username: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("User creation failed for username: {}, error: {}",
                    request.getUsername(), e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.debug("Received request to get user by ID: {}", id);

        UserResponse response = userService.getUserById(id);
        log.debug("Successfully retrieved user with ID: {}", id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        log.debug("Received request to get user by username: {}", username);

        UserResponse response = userService.getUserByUsername(username);
        log.debug("Successfully retrieved user by username: {}", username);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllActiveUsers() {
        log.info("Received request to get all active users");

        List<UserResponse> responses = userService.getAllActiveUsers();
        log.info("Successfully retrieved {} active users", responses.size());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/login")
    public ResponseEntity<Void> recordLogin(@PathVariable Long id) {
        log.info("Received request to record login for user ID: {}", id);

        // 먼저 사용자를 조회하여 username을 얻음
        UserResponse user = userService.getUserById(id);
        userService.recordUserLogin(user.getUsername());

        log.info("Login recorded successfully for user ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        log.info("Received request to deactivate user ID: {}", id);

        userService.deactivateUser(id);
        log.info("User deactivated successfully for ID: {}", id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/recent/{days}")
    public ResponseEntity<List<UserResponse>> getRecentlyLoggedInUsers(@PathVariable int days) {
        log.debug("Received request to get users logged in within last {} days", days);

        List<UserResponse> responses = userService.getRecentlyLoggedInUsers(days);
        log.debug("Retrieved {} recently logged in users", responses.size());

        return ResponseEntity.ok(responses);
    }
}