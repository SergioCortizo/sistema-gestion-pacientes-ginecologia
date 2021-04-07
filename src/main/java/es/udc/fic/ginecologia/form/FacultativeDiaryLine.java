package es.udc.fic.ginecologia.form;

import java.time.LocalDate;
import java.time.LocalTime;

import es.udc.fic.ginecologia.model.MeetingState;

public class FacultativeDiaryLine {
	
	private Integer id;
	
	private LocalDate date;

	private LocalTime hour;

	private MeetingState state;

	private String reason;

	private String patient;
	
	private String user;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getHour() {
		return hour;
	}

	public void setHour(LocalTime hour) {
		this.hour = hour;
	}

	public MeetingState getState() {
		return state;
	}

	public void setState(MeetingState state) {
		this.state = state;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
