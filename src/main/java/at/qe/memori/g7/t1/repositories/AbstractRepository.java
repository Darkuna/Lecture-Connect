package at.qe.memori.g7.t1.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Common base repository for all other repositories. Provides basic methods for
 * loading, saving and removing entities.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 *
 * @param <T>  The domain type this repository manages.
 * @param <ID> The type of the id of the entity this repository manages.
 */
@NoRepositoryBean
public interface AbstractRepository<T, ID extends Serializable> extends CrudRepository<T, ID>, Serializable {

}
