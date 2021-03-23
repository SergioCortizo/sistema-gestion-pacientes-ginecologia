package es.udc.fic.ginecologia.repository;

import es.udc.fic.ginecologia.model.Patient;

public interface CustomizedPatientDao {

	Iterable<Patient> findPatients(String name, String DNI_NIF, String histNumSERGAS, boolean enabled);

}
