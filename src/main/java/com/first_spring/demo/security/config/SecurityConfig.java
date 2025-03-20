package com.first_spring.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

import com.first_spring.demo.exceptions.FilterExceptionHandler;
import com.first_spring.demo.security.filters.JwtFilter;
import com.first_spring.demo.security.utils.JwtUtil;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Defines AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ✅ Defines Password Encoder (for hashing passwords)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Authentication Provider (used for verifying user credentials)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ✅ Security Filter Chain ( for configuring security rules )
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        /**
                         * Configure authentication rules for different endpoints here.
                         */
                        .requestMatchers("/api/users/**").authenticated()
                        .anyRequest()
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Call custom unauthorized handler instead of forwarding
                            FilterExceptionHandler.handleUnauthorized(response);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Call custom forbidden handler instead of forwarding
                            FilterExceptionHandler.handleForbidden(response);
                        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // ✅ Ensure JWT is processed
                .addFilterBefore(jwtAuthenticationFilter(), SecurityContextHolderAwareRequestFilter.class);

        return http.build();
    }

    @Bean
    public JwtFilter jwtAuthenticationFilter() {
        return new JwtFilter(jwtUtil, userDetailsService);
    }
}
