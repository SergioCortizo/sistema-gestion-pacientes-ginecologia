package es.udc.fic.ginecologia.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "meeting")
public class Meeting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String activity;

	private String comments;

	private LocalDateTime meeting_date;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "patient_id")
	private Patient patient;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany
	@JoinColumn(name = "meeting_id", referencedColumnName = "id")
	private Set<Answer> answers = new HashSet<>();

	@OneToMany
	@JoinColumn(name = "meeting_id", referencedColumnName = "id")
	private Set<ComplementaryTest> complementaryTests = new HashSet<>();

	@OneToMany
	@JoinColumn(name = "meeting_id", referencedColumnName = "id")
	private Set<Recipe> recipes = new HashSet<>();

	public Meeting() {

	}

	public Meeting(String activity, String comments) {
		this.meeting_date = LocalDateTime.now();

		this.activity = activity;
		this.comments = comments;
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

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}

	public Set<ComplementaryTest> getComplementaryTests() {
		return complementaryTests;
	}

	public void setComplementaryTests(Set<ComplementaryTest> complementaryTests) {
		this.complementaryTests = complementaryTests;
	}

	public Set<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(Set<Recipe> recipes) {
		this.recipes = recipes;
	}

	@Override
	public String toString() {
		return "Meeting [id=" + id + ", activity=" + activity + ", meeting_date=" + meeting_date.toString() + "]";
	}

}
