package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import es.udc.fic.ginecologia.model.User;

public interface UserDao extends CrudRepository<User, Integer> {
	boolean existsByUsername(String username);
	boolean existsById(Long id);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
	
	Optional<User> findById(Long id);
	Optional<User> findByUsername(String username);
	
}
