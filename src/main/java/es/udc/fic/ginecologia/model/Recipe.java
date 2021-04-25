package es.udc.fic.ginecologia.model;

import java.time.LocalDate;
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
@Table(name = "recipe")
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDate dispensingDate;

	private String clarifications;

	@OneToMany
	@JoinColumn(name = "recipe_id", referencedColumnName = "id")
	private Set<RecipeMedicine> medicines = new HashSet<>();
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="meeting_id")
	private Meeting meeting;

	public Recipe() {

	}

	public Recipe(LocalDate dispensingDate, String clarifications) {
		this.dispensingDate = dispensingDate == null ? LocalDate.now() : dispensingDate;
		this.clarifications = clarifications;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDispensingDate() {
		return dispensingDate;
	}

	public void setDispensingDate(LocalDate dispensingDate) {
		this.dispensingDate = dispensingDate;
	}

	public String getClarifications() {
		return clarifications;
	}

	public void setClarifications(String clarifications) {
		this.clarifications = clarifications;
	}

	public Set<RecipeMedicine> getMedicines() {
		return medicines;
	}

	public void setMedicines(Set<RecipeMedicine> medicines) {
		this.medicines = medicines;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	@Override
	public String toString() {
		return "Recipe [id=" + id + ", dispensingDate=" + dispensingDate + "]";
	}

}
