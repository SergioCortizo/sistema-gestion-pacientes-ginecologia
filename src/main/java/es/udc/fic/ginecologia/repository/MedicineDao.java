package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Medicine;

public interface MedicineDao extends CrudRepository<Medicine, Integer> {

	@Query("SELECT new java.lang.Boolean(count(*) > 0) FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(?1)")
	public boolean existsByName(String name);
	
	public Optional<Medicine> findByName(String name);
}
