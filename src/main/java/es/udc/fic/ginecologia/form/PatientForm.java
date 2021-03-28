package es.udc.fic.ginecologia.form;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class PatientForm {

	private String name;
	private String DNI_NIF;
	private String mobile_phone;
	private String landline;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthday;

	private String hist_numsergas;
	private String postal_address;
	private String location;
	private String email;
	private String allergies;
	private String diseases;
	private String interventions;
	private String family_background;
	private boolean smoker;
	private Integer menarche;
	private Integer menopause;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate last_menstruation_date;

	private Integer pregnancies;
	private Integer childbirths;
	private Integer cesarean_sections;
	private Integer misbirths;
	private String menstrual_type;
	private List<Integer> contraceptives;

	public PatientForm() {

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

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
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

	public void setFamily_background(String family_background) {
		this.family_background = family_background;
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

	public LocalDate getLast_menstruation_date() {
		return last_menstruation_date;
	}

	public void setLast_menstruation_date(LocalDate last_menstruation_date) {
		this.last_menstruation_date = last_menstruation_date;
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

	public void setCesarean_sections(Integer cesarean_sections) {
		this.cesarean_sections = cesarean_sections;
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

	public List<Integer> getContraceptives() {
		return contraceptives;
	}

	public void setContraceptives(List<Integer> contraceptives) {
		this.contraceptives = contraceptives;
	}

}
