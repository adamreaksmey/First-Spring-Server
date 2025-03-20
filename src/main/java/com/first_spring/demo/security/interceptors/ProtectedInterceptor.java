package com.first_spring.demo.security.interceptors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.first_spring.demo.exceptions.FilterExceptionHandler;
import com.first_spring.demo.security.annotations.Protected;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor to check if a request handler method is protected by the @Protected annotation.
 */
@Component
public class ProtectedInterceptor implements HandlerInterceptor {
    @Override
    /**
     * This method is called before the request handler method is executed
     */
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        // ✅ Check if the request handler has @Protected annotation
        if (handler instanceof HandlerMethod method && method.hasMethodAnnotation(Protected.class)) {
            // Log the handler method
            System.out.println("Protected Interceptor Is Running: " + handler);
            
            // 🔒 Authentication required for this route
            if (request.getHeader("Authorization") == null) {
                System.out.println("Authorization Header is missing");
                FilterExceptionHandler.handleUnauthorized(response);
                return false; // Block request
            }
        }

        return true; // ✅ Continue request processing
    }
}
