package com.example.url_authorization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        // 실제로는 서비스에서 사용자 목록을 가져옴
        model.addAttribute("message", "사용자 관리 페이지 (ADMIN 권한 필요)");
        return "admin/users";
    }

    @GetMapping("/settings")
    public String settings() {
        return "admin/settings";
    }
}