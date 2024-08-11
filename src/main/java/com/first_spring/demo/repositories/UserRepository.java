package com.first_spring.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.first_spring.demo.entities.users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // void deleteByUserId(String userId);  // Custom method to delete by userId ( if you had a userId )
}
