package es.udc.fic.ginecologia.service;

import java.util.List;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Patient;

public interface PatientService {

	Iterable<Patient> findAllPatients(Integer facultativeId) throws InstanceNotFoundException, PermissionException;

	Patient findPatient(Integer facultativeId, Long patientId) throws InstanceNotFoundException, PermissionException;

	Iterable<Patient> findPatients(Integer facultativeId, String name, String DNI_NIF, String histNumSERGAS,
			boolean enabled) throws InstanceNotFoundException, PermissionException;

	void addPatient(Integer userId, Patient patient, List<Integer> contraceptives)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;
	
	void changePatientEnablingState(Integer userId, Long patientId)
			throws InstanceNotFoundException, PermissionException;

	void updatePatient(Integer userId, Long patientId, Patient patient, List<Integer> contraceptivesIds)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

}
