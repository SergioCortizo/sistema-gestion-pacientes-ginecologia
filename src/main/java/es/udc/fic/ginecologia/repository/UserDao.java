package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.udc.fic.ginecologia.model.User;

@Repository
public interface UserDao extends CrudRepository<User, Integer>, CustomizedUserDao {
	boolean existsByUsername(String username);
	boolean existsById(Long id);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
	
	Optional<User> findById(Long id);
	Optional<User> findByUsername(String username);
	
	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_FACULTATIVE' AND u.enabled=1")
	public Iterable<User> findFacultatives();
	
}
