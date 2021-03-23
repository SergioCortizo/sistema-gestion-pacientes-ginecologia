package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Patient;

public interface PatientService {

	Iterable<Patient> findAllPatients(Integer facultativeId) throws InstanceNotFoundException, PermissionException;

	Patient findPatient(Integer facultativeId, Long patientId) throws InstanceNotFoundException, PermissionException;

	Iterable<Patient> findPatients(Integer facultativeId, String name, String DNI_NIF, String histNumSERGAS,
			boolean enabled) throws InstanceNotFoundException, PermissionException;

}
