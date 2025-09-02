package com.example.custom_user_details.controller;

import com.example.custom_user_details.dto.UserRegistrationDto;
import com.example.custom_user_details.model.User;
import com.example.custom_user_details.security.CustomUserDetails;
import com.example.custom_user_details.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("authorities", authentication.getAuthorities());
        }
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid UserRegistrationDto registrationDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            User user = userService.registerUser(registrationDto);

            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            log.warn("❌ 회원가입 실패: {}", e.getMessage());
            bindingResult.rejectValue("username", "error.userRegistrationDto", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("email", userDetails.getEmail());
        model.addAttribute("fullName", userDetails.getFullName());
        model.addAttribute("authorities", userDetails.getAuthorities());

        userService.updateLastLoginTime(userDetails.getUsername());

        return "dashboard";
    }
}
