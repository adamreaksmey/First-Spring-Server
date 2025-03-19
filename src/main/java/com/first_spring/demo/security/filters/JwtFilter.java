package com.first_spring.demo.security.filters;

import java.io.IOException;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import com.first_spring.demo.security.JwtUtil;
import com.first_spring.demo.security.annotations.Protected;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Constructor
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method is called by the filter chain to apply the filter to the request.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        /**
         * ------------ ENTERING ANNOTATION REQUEST CHECK ------------
         */
        // ✅ Get the handler (controller method) for the request
        Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        // ✅ If the handler is a controller method, check for @Protected annotation
        boolean isProtected = Optional.ofNullable(handler)
                .filter(HandlerMethod.class::isInstance)
                .map(HandlerMethod.class::cast)
                .map(method -> method.hasMethodAnnotation(Protected.class))
                .orElse(false);

        // ✅ If method is NOT @Protected, skip authentication check
        if (!isProtected) {
            chain.doFilter(request, response);
            return;
        }
        /**
         * ------------ END ANNOTATION REQUEST CHECK ------------
         */

        // 1️⃣ Get Authorization Header
        String authorizationHeader = request.getHeader("Authorization");

        // 2️⃣ If token is null, throw unauthorized error
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            return;
        }

        // Extract token and username
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        String username = jwtUtil.extractUsername(token);

        // 3️⃣ If username is null or user is already authenticated, throw unauthorized error
        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 4️⃣ Check if token is valid
        if (!jwtUtil.isTokenValid(token, userDetails.getUsername())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        // 5️⃣ If authenticated, set authentication in SecurityContext
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 6️⃣ Continue request processing
        chain.doFilter(request, response);
    }
}
