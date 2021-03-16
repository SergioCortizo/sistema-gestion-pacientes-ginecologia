package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.DiagnosticTest;

public interface DiagnosticTestDao extends CrudRepository<DiagnosticTest, Integer>, CustomizedDiagnosticTestDao {

	@Query("SELECT new java.lang.Boolean(count(*) > 0) FROM DiagnosticTest d WHERE LOWER(d.name) LIKE LOWER(?1)")
	public boolean existsByName(String name);
	
	public Optional<DiagnosticTest> findByName(String name);
}
