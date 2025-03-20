package com.first_spring.demo.security.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.first_spring.demo.exceptions.FilterExceptionHandler;
import com.first_spring.demo.security.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final List<String> publicRoutes;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Constructor
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, List<String> publicRoutes) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.publicRoutes = publicRoutes;
    }

    /**
     * This method is called by the filter chain to apply the filter to the request.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        // 1️⃣ Get Authorization Header
        String authorizationHeader = request.getHeader("Authorization");

        String requestURI = request.getRequestURI();

        // If the route is public, skip authentication
        if (isPublicRoute(requestURI)) {
            System.out.println("🟢 Skipping JWT authentication for public route: " + requestURI);
            chain.doFilter(request, response);
            return;
        }

        // 2️⃣ If token is null, throw unauthorized error
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("running from doFilterInternal");
            FilterExceptionHandler.handleUnauthorized(response);
            return;
        }

        // Extract token and username
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        String username = jwtUtil.extractUsername(token);

        // 3️⃣ If username is null or user is already authenticated, throw unauthorized
        // error
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

    // ✅ Check if a route is public
    private boolean isPublicRoute(String requestURI) {
        return publicRoutes.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }
}
