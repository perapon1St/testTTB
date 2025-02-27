package com.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Allow all requests without authentication
        http
            .authorizeRequests()
            .requestMatchers("/**").permitAll() // Permit all URLs without authentication
            .and()
            .csrf().disable(); // Disable CSRF protection (if necessary for your case)

        return http.build();
    }
}

