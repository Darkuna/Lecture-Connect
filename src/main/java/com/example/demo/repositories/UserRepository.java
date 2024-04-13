package com.example.demo.repositories;

import com.example.demo.models.UserInfo;
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

    Iterable<Userx> findByUsernameContaining(String username);

    @Query("SELECT u FROM Userx u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    Iterable<Userx> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM Userx u WHERE :role MEMBER OF u.roles")
    Iterable<Userx> findByRole(@Param("role") UserxRole role);

    Optional<Userx> findByUsername(String username);
}
