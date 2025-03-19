package com.first_spring.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.first_spring.demo.security.interceptors.ProtectedInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
        private final ProtectedInterceptor protectedInterceptor;

    public WebConfig(ProtectedInterceptor protectedInterceptor) {
        this.protectedInterceptor = protectedInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(protectedInterceptor);
    }
}
