package com.jong.lombok_slf4j.repository;

import com.jong.lombok_slf4j.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByActiveTrue();

    @Query("SELECT u FROM User u WHERE u.lastLoginAt > :since")
    List<User> findUsersLoggedInSince(LocalDateTime since);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}