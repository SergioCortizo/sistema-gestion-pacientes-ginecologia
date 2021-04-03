package es.udc.fic.ginecologia.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.Contraceptive;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.ContraceptiveDao;
import es.udc.fic.ginecologia.repository.MeetingDao;
import es.udc.fic.ginecologia.repository.PatientDao;

@Transactional
@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private PatientDao patientDao;

	@Autowired
	private ContraceptiveDao contraceptiveDao;

	@Autowired
	private MeetingDao meetingDao;

	@Override
	public Iterable<Patient> findAllPatients(Integer userId) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		if (permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			return patientDao.findByEnabledOrderByIdDesc(true);
		}

		return patientDao.findAllByOrderByIdDesc();
	}

	@Override
	public Iterable<Patient> findLastSeenPatients(Integer userId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId)) {
			throw new PermissionException();
		}

		if (permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			return patientDao.findByEnabledOrderByIdDesc(true);
		}
		
		return meetingDao.findLastSeenPatients(userId);
	}

	@Override
	public Patient findPatient(Integer userId, Long patientId) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<Patient> patient = patientDao.findById(patientId);

		if (!patient.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		return patient.get();
	}

	@Override
	public Iterable<Patient> findPatients(Integer userId, String name, String DNI_NIF, String histNumSERGAS,
			boolean enabled) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		if (permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			patientDao.findPatients(name, DNI_NIF, histNumSERGAS, true);
		}

		return patientDao.findPatients(name, DNI_NIF, histNumSERGAS, enabled);
	}

	@Override
	public void addPatient(Integer userId, Patient patient, List<Integer> contraceptivesIds)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(userId);

		if (patientDao.findByDNI_NIF(patient.getDNI_NIF()).isPresent()) {
			throw new DuplicateInstanceException("entities.patient", patient.getDNI_NIF());
		}

		if (patientDao.findByHist_numsergas(patient.getHist_numsergas()).isPresent()) {
			throw new DuplicateInstanceException("entities.patient", patient.getHist_numsergas());
		}

		Iterable<Contraceptive> contraceptives = contraceptiveDao.findAllById(contraceptivesIds);
		Set<Contraceptive> contraceptivesToAdd = new HashSet<Contraceptive>(
				(Collection<? extends Contraceptive>) contraceptives);

		patient.setContraceptives(contraceptivesToAdd);
		patient.setUser(user);
		patient.setEnabled(true);

		patientDao.save(patient);

	}

	@Override
	public void updatePatient(Integer userId, Long patientId, Patient patient, List<Integer> contraceptivesIds)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException {
		if (!permissionChecker.checkIsFacultative(userId) && !permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<Patient> patientInDB = patientDao.findById(patientId);

		if (!patientInDB.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		Patient result = patientInDB.get();

		if (!patient.getDNI_NIF().equals(result.getDNI_NIF())) {
			if (patientDao.findByDNI_NIF(patient.getDNI_NIF()).isPresent()) {
				throw new DuplicateInstanceException("entities.patient", patient.getDNI_NIF());
			}
		}

		if (!patient.getHist_numsergas().equals(result.getHist_numsergas())) {
			if (patientDao.findByHist_numsergas(patient.getHist_numsergas()).isPresent()) {
				throw new DuplicateInstanceException("entities.patient", patient.getHist_numsergas());
			}
		}

		Iterable<Contraceptive> contraceptives = contraceptiveDao.findAllById(contraceptivesIds);
		Set<Contraceptive> contraceptivesToAdd = new HashSet<Contraceptive>(
				(Collection<? extends Contraceptive>) contraceptives);

		result.setContraceptives(contraceptivesToAdd);

		result.setName(patient.getName());
		result.setDNI_NIF(patient.getDNI_NIF());
		result.setMobile_phone(patient.getMobile_phone());
		result.setLandline(patient.getLandline());
		result.setBirthday(patient.getBirthday());
		result.setHist_numsergas(patient.getHist_numsergas());
		result.setPostal_address(patient.getPostal_address());
		result.setLocation(patient.getLocation());
		result.setEmail(patient.getEmail());
		result.setAllergies(patient.getAllergies());
		result.setDiseases(patient.getDiseases());
		result.setInterventions(patient.getInterventions());
		result.setFamily_background(patient.getFamily_background());
		result.setSmoker(patient.isSmoker());
		result.setMenarche(patient.getMenarche());
		result.setMenopause(patient.getMenopause());

		if (patient.getLast_menstruation_date() != null) {
			result.setLast_menstruation_date(patient.getLast_menstruation_date());
		}

		result.setPregnancies(patient.getPregnancies());
		result.setChildbirths(patient.getChildbirths());
		result.setCesarean_sections(patient.getCesarean_sections());
		result.setMisbirths(patient.getMisbirths());
		result.setMenstrual_type(patient.getMenstrual_type());
	}

	@Override
	public void changePatientEnablingState(Integer userId, Long patientId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<Patient> patient = patientDao.findById(patientId);

		if (!patient.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		Patient result = patient.get();

		result.setEnabled(!result.isEnabled());

	}

	@Override
	public void changeAsPatientOfInterest(Integer userId, Long patientId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(userId);

		Optional<Patient> patient = patientDao.findById(patientId);

		if (!patient.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		Patient result = patient.get();

		Set<Patient> patientsOfInterest = user.getPatientsOfInterest();

		if (patientsOfInterest.contains(result)) {
			patientsOfInterest.remove(result);
		} else {
			patientsOfInterest.add(result);
		}

		user.setPatientsOfInterest(patientsOfInterest);
	}
}
