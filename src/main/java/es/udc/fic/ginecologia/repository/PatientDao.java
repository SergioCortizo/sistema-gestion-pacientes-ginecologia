package es.udc.fic.ginecologia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Patient;

public interface PatientDao extends CrudRepository<Patient, Long>, CustomizedPatientDao{
	public Iterable<Patient> findAllByOrderByIdDesc();
	
	public Iterable<Patient> findByEnabledOrderByIdDesc(Boolean enabled);
	
	@Query("SELECT p FROM Patient p WHERE DNI_NIF=?1")
	public Optional<Patient> findByDNI_NIF(String DNI_NIF);
	
	@Query("SELECT p FROM Patient p WHERE hist_numsergas=?1")
	public Optional<Patient> findByHist_numsergas(String hist_numsergas);
}
