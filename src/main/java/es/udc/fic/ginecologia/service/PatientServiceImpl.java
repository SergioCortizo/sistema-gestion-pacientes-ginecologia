package es.udc.fic.ginecologia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.repository.PatientDao;

@Transactional
@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private PatientDao patientDao;

	@Override
	public Iterable<Patient> findAllPatients(Integer facultativeId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(facultativeId)) {
			throw new PermissionException();
		}

		return patientDao.findAllByOrderByIdDesc();
	}

	@Override
	public Patient findPatient(Integer facultativeId, Long patientId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(facultativeId)) {
			throw new PermissionException();
		}

		Optional<Patient> patient = patientDao.findById(patientId);

		if (!patient.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		return patient.get();
	}

	@Override
	public Iterable<Patient> findPatients(Integer facultativeId, String name, String DNI_NIF, String histNumSERGAS,
			boolean enabled) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(facultativeId)) {
			throw new PermissionException();
		}

		return patientDao.findPatients(name, DNI_NIF, histNumSERGAS, enabled);
	}
}
