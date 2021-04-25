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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String username;

	private String password;

	private String email;

	private boolean enabled;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	private String postal_address;

	private String location;

	private String dni;

	private String phone_number;

	private LocalDateTime discharge_date;

	private String collegiate_number;

	@OneToMany
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Set<Schedule> schedules = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_speciality", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "speciality_id"))
	private Set<Speciality> specialities = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "patients_of_interest", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "patient_id"))
	private Set<Patient> patientsOfInterest = new HashSet<>();
	
	@OneToMany
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Set<CommonTaskUser> commonTaskUsers = new HashSet<>();
	
	private LocalDateTime last_time_seen_notices;

	public User() {
	}

	public User(String name, String username, String email, String postalAddress, String location, String dNI,
			String phoneNumber, String collegiateNumber) {

		this.name = name;
		this.username = username;
		this.email = email;
		this.postal_address = postalAddress;
		this.location = location;
		dni = dNI;
		this.phone_number = phoneNumber;
		this.collegiate_number = collegiateNumber;

		this.discharge_date = LocalDateTime.now();
		this.enabled = true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public LocalDateTime getDischarge_date() {
		return discharge_date;
	}

	public void setDischarge_date(LocalDateTime discharge_date) {
		this.discharge_date = discharge_date;
	}

	public String getCollegiate_number() {
		return collegiate_number;
	}

	public void setCollegiate_number(String collegiate_number) {
		this.collegiate_number = collegiate_number;
	}

	public Set<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(Set<Schedule> schedules) {
		this.schedules = schedules;
	}

	public Set<Speciality> getSpecialities() {
		return specialities;
	}

	public void setSpecialities(Set<Speciality> specialities) {
		this.specialities = specialities;
	}

	public Set<Patient> getPatientsOfInterest() {
		return patientsOfInterest;
	}

	public void setPatientsOfInterest(Set<Patient> patientsOfInterest) {
		this.patientsOfInterest = patientsOfInterest;
	}

	public Set<CommonTaskUser> getCommonTaskUsers() {
		return commonTaskUsers;
	}

	public void setCommonTaskUsers(Set<CommonTaskUser> commonTaskUsers) {
		this.commonTaskUsers = commonTaskUsers;
	}

	public LocalDateTime getLast_time_seen_notices() {
		return last_time_seen_notices;
	}

	public void setLast_time_seen_notices(LocalDateTime last_time_seen_notices) {
		this.last_time_seen_notices = last_time_seen_notices;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", username=" + username + ", enabled=" + enabled + ", roles=" + roles + "]";
	}
}
