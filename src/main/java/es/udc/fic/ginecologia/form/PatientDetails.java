package es.udc.fic.ginecologia.form;

import java.time.LocalDate;
import java.time.Period;

import es.udc.fic.ginecologia.model.Patient;

public class PatientDetails {
	private Patient patient;

	private Integer age;

	public PatientDetails() {

	}

	public PatientDetails(Patient patient) {
		this.patient = patient;

		this.age = Period.between(patient.getBirthday().toLocalDate(), LocalDate.now()).getYears();
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
