package es.udc.fic.ginecologia.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "calendar_entry")
public class CalendarEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDateTime entryDate;

	@Column(columnDefinition = "ENUM('opened', 'closed', 'cancelled')")
	@Enumerated(EnumType.STRING)
	private MeetingState state;

	private String reason;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "patient_id")
	private Patient patient;

	public CalendarEntry() {

	}

	public CalendarEntry(LocalDateTime entryDate, String reason, User user, Patient patient) {
		this.entryDate = entryDate;
		this.state = MeetingState.opened;
		this.reason = reason;
		this.user = user;
		this.patient = patient;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDateTime entryDate) {
		this.entryDate = entryDate;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	@Override
	public String toString() {
		return "CalendarEntry [id=" + id + ", entryDate=" + entryDate.toString() + ", state=" + state + ", reason="
				+ reason + "]";
	}

}
