package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Speciality;


public interface SpecialityDao extends CrudRepository<Speciality, Integer>, CustomizedSpecialityDao{
	
	@Query("SELECT new java.lang.Boolean(count(*) > 0) FROM Speciality s WHERE LOWER(s.name) LIKE LOWER(?1)")
	public boolean existsByName(String name);
	
	public Optional<Speciality> findByName(String name);
	
}
