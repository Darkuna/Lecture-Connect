package at.qe.memori.g7.t1.repositories;

import at.qe.memori.g7.t1.model.Userx;
import at.qe.memori.g7.t1.model.UserxRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link Userx} entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
public interface UserRepository extends AbstractRepository<Userx, String> {

    Iterable<Userx> findByUsernameContaining(String username);

    @Query("SELECT u FROM Userx u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    Iterable<Userx> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM Userx u WHERE :role MEMBER OF u.roles")
    Iterable<Userx> findByRole(@Param("role") UserxRole role);

}
