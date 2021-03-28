package es.udc.fic.ginecologia.form;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.fic.ginecologia.model.Patient;

public class PatientConversor {
	public final static List<PatientElemList> createPatientElemList(Iterable<Patient> patients) {
		List<PatientElemList> result = new ArrayList<>();

		patients.forEach(p -> {
			PatientElemList patient = new PatientElemList(p);
			result.add(patient);
		});

		return result;

	}

	public final static Patient convertToPatient(PatientForm patientForm) {
		Patient patient = new Patient();

		patient.setName(patientForm.getName());
		patient.setDNI_NIF(patientForm.getDNI_NIF());
		patient.setMobile_phone(patientForm.getMobile_phone());
		patient.setLandline(patientForm.getLandline());
		patient.setBirthday(LocalDateTime.of(patientForm.getBirthday(), LocalTime.of(0, 0)));
		patient.setHist_numsergas(patientForm.getHist_numsergas());
		patient.setPostal_address(patientForm.getPostal_address());
		patient.setLocation(patientForm.getLocation());
		patient.setEmail(patientForm.getEmail());
		patient.setAllergies(patientForm.getAllergies());
		patient.setDiseases(patientForm.getDiseases());
		patient.setInterventions(patientForm.getInterventions());
		patient.setFamily_background(patientForm.getFamily_background());
		patient.setSmoker(patientForm.isSmoker());
		patient.setMenarche(patientForm.getMenarche());
		patient.setMenopause(patientForm.getMenopause());

		if (patientForm.getLast_menstruation_date() != null) {
			patient.setLast_menstruation_date(
					LocalDateTime.of(patientForm.getLast_menstruation_date(), LocalTime.of(0, 0)));
		}

		patient.setPregnancies(patientForm.getPregnancies());
		patient.setChildbirths(patientForm.getChildbirths());
		patient.setCesarean_sections(patientForm.getCesarean_sections());
		patient.setMisbirths(patientForm.getMisbirths());
		patient.setMenstrual_type(patientForm.getMenstrual_type());

		return patient;
	}

	public final static PatientForm convertToPatientForm(Patient patient) {
		PatientForm patientForm = new PatientForm();

		patientForm.setName(patient.getName());
		patientForm.setDNI_NIF(patient.getDNI_NIF());
		patientForm.setMobile_phone(patient.getMobile_phone());
		patientForm.setLandline(patient.getLandline());
		patientForm.setBirthday(patient.getBirthday().toLocalDate());
		patientForm.setHist_numsergas(patient.getHist_numsergas());
		patientForm.setPostal_address(patient.getPostal_address());
		patientForm.setLocation(patient.getLocation());
		patientForm.setEmail(patient.getEmail());
		patientForm.setAllergies(patient.getAllergies());
		patientForm.setDiseases(patient.getDiseases());
		patientForm.setInterventions(patient.getInterventions());
		patientForm.setFamily_background(patient.getFamily_background());
		patientForm.setSmoker(patient.isSmoker());
		patientForm.setMenarche(patient.getMenarche());
		patientForm.setMenopause(patient.getMenopause());

		patientForm.setLast_menstruation_date(patient.getLast_menstruation_date().toLocalDate());

		patientForm.setPregnancies(patient.getPregnancies());
		patientForm.setChildbirths(patient.getChildbirths());
		patientForm.setCesarean_sections(patient.getCesarean_sections());
		patientForm.setMisbirths(patient.getMisbirths());
		patientForm.setMenstrual_type(patient.getMenstrual_type());
		
		List<Integer> contraceptives = new ArrayList<>();
		patient.getContraceptives().forEach(c -> contraceptives.add(c.getId()));
		patientForm.setContraceptives(contraceptives);

		return patientForm;
	}

}
