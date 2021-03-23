package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Patient;

public interface PatientDao extends CrudRepository<Patient, Long>, CustomizedPatientDao{
	public Iterable<Patient> findAllByOrderByIdDesc();
}
