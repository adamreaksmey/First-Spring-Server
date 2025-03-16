package com.first_spring.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.first_spring.demo.entities.users.User;

/**
 * This interface is responsible for interacting with the User entity in the
 * database.
 * It extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Delete a User by customedId
     * 
     * @param customedId The customedId of the User to be deleted
     */
    void deleteByCustomedId(Long customedId);

    /**
     * Find a User by username
     * 
     * @param username The username of the User to be found
     */
    Optional<User> findByUsername(String username);
    // /**
    // * Delete a User by customedId
    // *
    // * @param customedId The customedId of the User to be deleted
    // * ( using @Modifying and @Transactional to make the query a DML query )
    // */
    // @Modifying
    // @Transactional
    // @Query("DELETE FROM User u WHERE u.customedId = :customedId")
    // void deleteByCustomedId(@Param("customedId") Long customedId);
}
