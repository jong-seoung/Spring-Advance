package com.jong.lombok_slf4j.service;

import com.jong.lombok_slf4j.dto.UserCreateRequest;
import com.jong.lombok_slf4j.dto.UserResponse;
import com.jong.lombok_slf4j.exception.UserAlreadyExistsException;
import com.jong.lombok_slf4j.exception.UserNotFoundException;
import com.jong.lombok_slf4j.model.User;
import com.jong.lombok_slf4j.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j  // 이 어노테이션 하나로 log 객체 자동 생성
public class UserService {

    private final UserRepository userRepository;

    /**
     * 새 사용자 생성
     */
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Starting user creation process for username: {}", request.getUsername());

        // 입력 데이터 검증 로깅
        log.debug("Validating user creation request: {}", request);

        // 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Attempt to create user with duplicate username: {}", request.getUsername());
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Attempt to create user with duplicate email: {}", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        try {
            // 사용자 생성
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .active(true)
                    .build();

            log.debug("Created user entity: {}", user);

            User savedUser = userRepository.save(user);
            log.info("Successfully created user with ID: {} for username: {}",
                    savedUser.getId(), savedUser.getUsername());

            return convertToResponse(savedUser);

        } catch (Exception e) {
            log.error("Failed to create user for username: {}, error: {}",
                    request.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 사용자 조회
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.debug("Searching for user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new UserNotFoundException("User not found with ID: " + id);
                });

        log.debug("Found user: {}", user.getUsername());
        return convertToResponse(user);
    }

    /**
     * 사용자명으로 조회
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        log.debug("Searching for user with username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found with username: {}", username);
                    return new UserNotFoundException("User not found with username: " + username);
                });

        log.debug("Found user by username: {}", username);
        return convertToResponse(user);
    }

    /**
     * 모든 활성 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllActiveUsers() {
        log.info("Retrieving all active users");

        List<User> users = userRepository.findByActiveTrue();
        log.debug("Found {} active users", users.size());

        List<UserResponse> responses = users.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        log.info("Successfully retrieved {} active users", responses.size());
        return responses;
    }

    /**
     * 사용자 로그인 처리
     */
    public void recordUserLogin(String username) {
        log.info("Recording login for user: {}", username);

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            user.updateLastLogin();
            userRepository.save(user);

            log.info("Successfully recorded login for user: {} at {}",
                    username, user.getLastLoginAt());

        } catch (Exception e) {
            log.error("Failed to record login for user: {}, error: {}",
                    username, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 사용자 비활성화
     */
    public void deactivateUser(Long id) {
        log.info("Deactivating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (!user.getActive()) {
            log.warn("Attempt to deactivate already inactive user: {}", user.getUsername());
            return;
        }

        user.setActive(false);
        userRepository.save(user);

        log.info("Successfully deactivated user: {}", user.getUsername());
    }

    /**
     * 최근 로그인 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getRecentlyLoggedInUsers(int daysAgo) {
        log.debug("Searching for users logged in within last {} days", daysAgo);

        LocalDateTime since = LocalDateTime.now().minusDays(daysAgo);
        List<User> users = userRepository.findUsersLoggedInSince(since);

        log.debug("Found {} users logged in since {}", users.size(), since);

        return users.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Entity를 Response DTO로 변환
     */
    private UserResponse convertToResponse(User user) {
        log.trace("Converting user entity to response DTO for user: {}", user.getUsername());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}