package com.example.demo.repositories;

import com.example.demo.models.Userx;
import com.example.demo.models.UserxRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


/**
 * Repository for managing {@link Userx} entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
public interface UserRepository extends JpaRepository<Userx, String> {
    Optional<Userx> findByUsername(String username);
}
