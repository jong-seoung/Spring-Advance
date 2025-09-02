package com.example.session_csrf.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class SessionInfoController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/if-required/login")
    public String ifRequiredLogin(HttpServletRequest request) {
        logSessionInfo(request, "IF_REQUIRED - Login Page (Before Login)");
        return "if-required-login";
    }

    @GetMapping("/if-required/dashboard")
    public String ifRequiredDashboard(HttpServletRequest request, Model model) {
        logSessionInfo(request, "IF_REQUIRED - Login Page (After Login)");
        addSessionInfo(request, model, "IF_REQUIRED");
        return "dashboard";
    }

    @GetMapping("/always/login")
    public String alwaysLogin(HttpServletRequest request) {
        logSessionInfo(request, "ALWAYS - Login Page (Before Login)");
        return "always-login";
    }

    @GetMapping("/always/dashboard")
    public String alwaysDashboard(HttpServletRequest request, Model model) {
        logSessionInfo(request, "ALWAYS - Login Page (After Login)");
        addSessionInfo(request, model, "ALWAYS");
        return "dashboard";
    }

    @GetMapping("/never/login")
    public String neverLogin(HttpServletRequest request) {
        logSessionInfo(request, "NEVER - Login Page (Before Login)");
        return "never-login";
    }

    @GetMapping("/never/dashboard")
    public String neverDashboard(HttpServletRequest request, Model model) {
        logSessionInfo(request, "NEVER - Login Page (After Login)");
        addSessionInfo(request, model, "NEVER");
        return "dashboard";
    }

    @GetMapping("/api/public/test")
    @ResponseBody
    public Map<String, String> publicApiTest() {
        return Map.of(
                "message", "Public API - 인증 불필요",
                "timestamp", LocalDateTime.now().toString()
        );
    }

    @GetMapping("/api/info")
    @ResponseBody
    public Map<String, Object> getApiInfo(HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();

        HttpSession session = request.getSession(false);
        info.put("sessionExists", session != null);
        info.put("sessionId", session != null ? session.getId() : null);
        info.put("policy", "STATELESS");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        info.put("authenticated", auth != null && auth.isAuthenticated());
        info.put("username", auth != null ? auth.getName() : null);

        return info;
    }

    private void addSessionInfo(HttpServletRequest request, Model model, String policy) {
        HttpSession session = request.getSession(false);

        model.addAttribute("policy", policy);
        model.addAttribute("sessionExists", session != null);

        if (session != null) {
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("creationTime",
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault()));
            model.addAttribute("lastAccessedTime",
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getLastAccessedTime()), ZoneId.systemDefault()));
            model.addAttribute("maxInactiveInterval", session.getMaxInactiveInterval());
        }

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken != null) {
            model.addAttribute("csrfToken", csrfToken.getToken());
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            model.addAttribute("username", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities());
        }
    }

    private void logSessionInfo(HttpServletRequest request, String context) {
        log.info("\n========== SESSION INFO [{}] ==========", context);

        HttpSession session = request.getSession(false);

        if (session == null) {
            log.info("📌 세션 상태: 세션이 존재하지 않음 (NULL)");
        } else {
            log.info("✅ 세션 상태: 세션 존재");
            log.info("  - 세션 ID: {}", session.getId());
            log.info("  - 생성 시간: {}",
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault()));
            log.info("  - 마지막 접근: {}",
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getLastAccessedTime()), ZoneId.systemDefault()));
            log.info("  - 최대 비활성 시간: {} 초", session.getMaxInactiveInterval());
            log.info("  - 새 세션 여부: {}", session.isNew());
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            log.info("🔐 인증 상태: 인증됨");
            log.info("  - 사용자: {}", auth.getName());
            log.info("  - 권한: {}", auth.getAuthorities());
        } else {
            log.info("🔓 인증 상태: 인증되지 않음");
        }

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken != null) {
            log.info("🛡️ CSRF 토큰: 존재 ({})", csrfToken.getToken());
        } else {
            log.info("⚠️ CSRF 토큰: 없음");
        }

        log.info("📍 요청 URL: {}", request.getRequestURL());

        log.info("📍 쿠키 정보:");
        if(request.getCookies() != null) {
            for (var cookie: request.getCookies()) {
                if(cookie.getName().contains("SESSION") || cookie.getName().contains("XSRF")) {
                    log.info("    - {}: {}", cookie.getName(), cookie.getValue());
                }
            }
        } else {
            log.info("    - 쿠키 없음");
        }

        log.info("========================================\n");
    }
}
