package com.example.session_csrf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SessionPolicyConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER")
                        .build(),
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build()
        );
    }

    @Bean
    public SecurityFilterChain ifRequiredFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/if-required/**")
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/if-required/login", "/if-required/public/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/if-required/login")
                                .loginProcessingUrl("/if-required/authenticate")
                                .defaultSuccessUrl("/if-required/dashboard")
                                .permitAll()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                ).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean
    public SecurityFilterChain alwaysFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/always/**")
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/always/login", "/always/public/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/always/login")
                                .loginProcessingUrl("/always/authenticate")
                                .defaultSuccessUrl("/always/dashboard")
                                .permitAll()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                ).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean
    public SecurityFilterChain neverFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/never/**")
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/never/login", "/never/public/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/never/login")
                                .loginProcessingUrl("/never/authenticate")
                                .defaultSuccessUrl("/never/dashboard")
                                .permitAll()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER)
                ).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean
    public SecurityFilterChain statelessFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/api/public/**").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.realmName("API"))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).csrf(AbstractHttpConfigurer::disable).build();
    }
}