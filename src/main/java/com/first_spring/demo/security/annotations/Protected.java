package com.first_spring.demo.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Annotation applies only to methods
@Retention(RetentionPolicy.RUNTIME) // Available at runtime for reflection
public @interface Protected {
    
}
