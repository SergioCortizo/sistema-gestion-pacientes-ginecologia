package es.udc.fic.ginecologia.form;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

public class CalendarEntryForm {

	private String reason;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateEntry;

	private LocalTime hourEntry;

	private Integer facultative;

	private Long patient;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDate getDateEntry() {
		return dateEntry;
	}

	public void setDateEntry(LocalDate dateEntry) {
		this.dateEntry = dateEntry;
	}

	public LocalTime getHourEntry() {
		return hourEntry;
	}

	public void setHourEntry(LocalTime hourEntry) {
		this.hourEntry = hourEntry;
	}

	public Integer getFacultative() {
		return facultative;
	}

	public void setFacultative(Integer facultative) {
		this.facultative = facultative;
	}

	public Long getPatient() {
		return patient;
	}

	public void setPatient(Long patient) {
		this.patient = patient;
	}

	@Override
	public String toString() {
		return "CalendarEntryForm [reason=" + reason + ", dateEntry=" + dateEntry.toString() + ", hourEntry=" + hourEntry.toString()
				+ ", facultative=" + facultative + ", patient=" + patient + "]";
	}

}
