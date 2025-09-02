package com.jong.lombok_slf4j.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String fullName;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        log.trace("User entity created: {}", this.username);
    }

    @PostPersist
    protected void onPostPersist() {
        log.debug("User saved to database with ID: {}", this.id);
    }

    @PreUpdate
    protected void onUpdate() {
        log.trace("User entity being updated: {}", this.username);
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
        log.debug("Updated last login time for user: {}", this.username);
    }
}