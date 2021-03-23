package es.udc.fic.ginecologia.form;

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
}
