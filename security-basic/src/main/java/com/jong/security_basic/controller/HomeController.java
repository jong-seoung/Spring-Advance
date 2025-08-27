package com.jong.security_basic.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("authorities", authentication.getAuthorities());
            System.out.println(authentication.getAuthorities());
            System.out.println(authentication.getAuthorities());
        }
        return "home";
    }

    @GetMapping("/user")
    public String userPage(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "user";
    }
}