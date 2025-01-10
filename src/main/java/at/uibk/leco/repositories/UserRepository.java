package at.uibk.leco.repositories;

import at.uibk.leco.models.Userx;
import org.springframework.data.jpa.repository.JpaRepository;

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
