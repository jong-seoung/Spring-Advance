package com.example.url_authorization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("authorities", auth.getAuthorities());
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "user/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "user/settings";
    }
}