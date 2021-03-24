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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {

	@Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
	private String name;
	private String DNI_NIF;
	private String mobile_phone;
	private String landline;
	private LocalDateTime birthday;
	private String hist_numsergas;
	private String postal_address;
	private String location;
	private String email;
	private LocalDateTime discharge_date;
	private String allergies;
	private String diseases;
	private String interventions;
	private String family_background;
	private boolean smoker;
	private Integer menarche;
	private Integer menopause;
	private LocalDateTime last_menstruation_date;
	private Integer pregnancies;
	private Integer childbirths;
	private Integer cesarean_sections;
	private Integer misbirths;
	private String menstrual_type;
	private boolean enabled;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "patient_contraceptive",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "contraceptive_id")
            )
    private Set<Contraceptive> contraceptives = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="patient_id", referencedColumnName="id")
    private Set<Meeting> meetings = new HashSet<>();
	
	public Patient() {
		
	}

	public Patient(String name, String dNI_NIF, String mobilePhone, String landline, LocalDateTime birthday,
			String histNumSERGAS, String postalAddress, String location, String email, String allergies,
			String diseases, String interventions, String familyBackground, boolean smoker, Integer menarche,
			Integer menopause, LocalDateTime lastMenstruationDate, Integer pregnancies, Integer childbirths,
			Integer cesareanSections, Integer misbirths, String menstrualType, User user) {
		this.name = name;
		DNI_NIF = dNI_NIF;
		this.mobile_phone = mobilePhone;
		this.landline = landline;
		this.birthday = birthday;
		this.hist_numsergas = histNumSERGAS;
		this.postal_address = postalAddress;
		this.location = location;
		this.email = email;
		this.allergies = allergies;
		this.diseases = diseases;
		this.interventions = interventions;
		this.family_background = familyBackground;
		this.smoker = smoker;
		this.menarche = menarche;
		this.menopause = menopause;
		this.last_menstruation_date = lastMenstruationDate;
		this.pregnancies = pregnancies;
		this.childbirths = childbirths;
		this.cesarean_sections = cesareanSections;
		this.misbirths = misbirths;
		this.menstrual_type = menstrualType;
		this.user = user;
		
		this.discharge_date= LocalDateTime.now();
		this.enabled= true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDNI_NIF() {
		return DNI_NIF;
	}

	public void setDNI_NIF(String dNI_NIF) {
		DNI_NIF = dNI_NIF;
	}

	public String getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public LocalDateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDateTime birthday) {
		this.birthday = birthday;
	}

	public String getHist_numsergas() {
		return hist_numsergas;
	}

	public void setHist_numsergas(String hist_numsergas) {
		this.hist_numsergas = hist_numsergas;
	}

	public String getPostal_address() {
		return postal_address;
	}

	public void setPostal_address(String postal_address) {
		this.postal_address = postal_address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getDiseases() {
		return diseases;
	}

	public void setDiseases(String diseases) {
		this.diseases = diseases;
	}

	public String getInterventions() {
		return interventions;
	}

	public void setInterventions(String interventions) {
		this.interventions = interventions;
	}

	public String getFamily_background() {
		return family_background;
	}

	public void setFamily_background(String familyBackground) {
		this.family_background = familyBackground;
	}

	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public Integer getMenarche() {
		return menarche;
	}

	public void setMenarche(Integer menarche) {
		this.menarche = menarche;
	}

	public Integer getMenopause() {
		return menopause;
	}

	public void setMenopause(Integer menopause) {
		this.menopause = menopause;
	}

	public LocalDateTime getLast_menstruation_date() {
		return last_menstruation_date;
	}

	public void setLast_menstruation_date(LocalDateTime last_menstruation_date) {
		this.last_menstruation_date = last_menstruation_date;
	}

	public LocalDateTime getDischarge_date() {
		return discharge_date;
	}

	public void setDischarge_date(LocalDateTime dischargeDate) {
		this.discharge_date = dischargeDate;
	}

	public Integer getPregnancies() {
		return pregnancies;
	}

	public void setPregnancies(Integer pregnancies) {
		this.pregnancies = pregnancies;
	}

	public Integer getChildbirths() {
		return childbirths;
	}

	public void setChildbirths(Integer childbirths) {
		this.childbirths = childbirths;
	}

	public Integer getCesarean_sections() {
		return cesarean_sections;
	}

	public void setCesarean_sections(Integer cesareanSections) {
		this.cesarean_sections = cesareanSections;
	}

	public Integer getMisbirths() {
		return misbirths;
	}

	public void setMisbirths(Integer misbirths) {
		this.misbirths = misbirths;
	}

	public String getMenstrual_type() {
		return menstrual_type;
	}

	public void setMenstrual_type(String menstrual_type) {
		this.menstrual_type = menstrual_type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Contraceptive> getContraceptives() {
		return contraceptives;
	}

	public void setContraceptives(Set<Contraceptive> contraceptives) {
		this.contraceptives = contraceptives;
	}

	public Set<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(Set<Meeting> meetings) {
		this.meetings = meetings;
	}
	
}
