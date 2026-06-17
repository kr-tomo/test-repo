package com.organization.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/hello").permitAll()
                .requestMatchers(HttpMethod.GET, "/fields").permitAll()
                .requestMatchers("/fields/**").hasAuthority("ROLE_ACCOUNT_MANAGEMENT")
                .requestMatchers("/fields").hasAuthority("ROLE_ACCOUNT_MANAGEMENT")
                .requestMatchers("/mentor-profiles/me/**").hasAuthority("ROLE_MENTOR")
                .requestMatchers(HttpMethod.POST, "/instant-qas").hasAuthority("ROLE_MENTEE")
                .requestMatchers(HttpMethod.GET, "/instant-qas/me/**").hasAuthority("ROLE_MENTEE")
                .requestMatchers(HttpMethod.GET, "/instant-qas/*/answers").hasAuthority("ROLE_MENTEE")
                .requestMatchers("/instant-qas/**").hasAuthority("ROLE_MENTOR")
                .requestMatchers(HttpMethod.POST, "/match-requests").hasAuthority("ROLE_MENTEE")
                .requestMatchers(HttpMethod.GET, "/match-requests/me").hasAuthority("ROLE_MENTEE")
                .requestMatchers("/admin/match-requests/**").hasAuthority("ROLE_MATCHING_MANAGEMENT")
                .requestMatchers("/admin/**").hasAuthority("ROLE_ACCOUNT_MANAGEMENT")
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // For H2 console

        return http.build();
    }
}
