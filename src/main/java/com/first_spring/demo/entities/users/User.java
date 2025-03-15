package com.first_spring.demo.entities.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class is responsible for representing a user in the application.
 * It is annotated with @Entity to indicate that it is an entity in the database.
 * It is annotated with @Table to indicate the name of the table in the database.
 * It is annotated with @Data to generate the getters and setters.
 * It is annotated with @NoArgsConstructor to generate a no-argument constructor.
 */
@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Very similar to an auto incrementation
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "customed_id", nullable = false, unique = true)
    private Long customedId;

    // // Constructors, Getters, and Setters
    // public User() {
    // }

    // public User(String name, String email) {
    //     this.name = name;
    //     this.email = email;
    // }

    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getName() {
    //     return name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // public String getEmail() {
    //     return email;
    // }

    // public void setEmail(String email) {
    //     this.email = email;
    // }
}
