package es.udc.fic.ginecologia.form;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.udc.fic.ginecologia.common.MeetingByDateDescendingComparator;
import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Patient;

public class PatientDetails {
	private Patient patient;

	private List<Meeting> meetings;

	private Integer age;

	public PatientDetails() {

	}

	public PatientDetails(Patient patient) {
		this.patient = patient;

		this.meetings = new ArrayList<>(patient.getMeetings());
		Collections.sort(this.meetings, new MeetingByDateDescendingComparator());

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

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

}
