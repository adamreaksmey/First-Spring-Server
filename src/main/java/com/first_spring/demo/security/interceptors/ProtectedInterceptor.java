package com.first_spring.demo.security.interceptors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.first_spring.demo.security.annotations.Protected;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ProtectedInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        // ✅ Check if the request handler has @Protected annotation
        if (handler instanceof HandlerMethod method && method.hasMethodAnnotation(Protected.class)) {
            // 🔒 Authentication required for this route
            if (request.getHeader("Authorization") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
                return false; // Block request
            }
        }

        return true; // ✅ Continue request processing
    }
}
