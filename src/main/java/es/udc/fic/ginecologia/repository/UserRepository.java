package es.udc.fic.ginecologia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.udc.fic.ginecologia.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
}
