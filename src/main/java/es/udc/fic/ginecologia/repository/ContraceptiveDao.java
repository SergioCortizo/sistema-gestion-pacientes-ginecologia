package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Contraceptive;

public interface ContraceptiveDao extends CrudRepository<Contraceptive, Integer>, CustomizedContraceptiveDao {
	
	@Query("SELECT new java.lang.Boolean(count(*) > 0) FROM Contraceptive c WHERE LOWER(c.name) LIKE LOWER(?1)")
	public boolean existsByName(String name);
	
	public Optional<Contraceptive> findByName(String name);
}
