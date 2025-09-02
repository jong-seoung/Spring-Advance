package com.jong.security_basic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now());
        response.put("user", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("message", "API 접근 성공 - Basic Auth 인증 완료");

        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities());
        userInfo.put("authenticated", authentication.isAuthenticated());
        userInfo.put("authType", authentication.getClass().getSimpleName());
        
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/admin/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats(Authentication authentication) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 4);
        stats.put("activeUsers", 1);
        stats.put("adminUser", authentication.getName());
        stats.put("serverTime", LocalDateTime.now());

        return ResponseEntity.ok(stats);
    }
}