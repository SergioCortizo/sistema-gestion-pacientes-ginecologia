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
@Table(name = "meeting")
public class Meeting {

	@Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
	
	private String activity;
	
	private String comments;
	
	private LocalDateTime meeting_date;
	
	@Column(columnDefinition = "ENUM('opened', 'closed', 'cancelled')")
    @Enumerated(EnumType.STRING)
	private MeetingState state;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="patient_id")
	private Patient patient;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;	

	public Meeting() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDateTime getMeeting_date() {
		return meeting_date;
	}

	public void setMeeting_date(LocalDateTime meeting_date) {
		this.meeting_date = meeting_date;
	}

	public MeetingState getState() {
		return state;
	}

	public void setState(MeetingState state) {
		this.state = state;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
