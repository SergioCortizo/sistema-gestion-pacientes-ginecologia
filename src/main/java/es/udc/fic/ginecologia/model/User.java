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
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	@Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
	
    private String name;
    
    private String username;
	
    private String password;
	
    private String email;
    
    private boolean enabled;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            )
    private Set<Role> roles = new HashSet<>(); 
    
    private String postal_address;
    
    private String location;
    
    private String dni;
    
    private String phone_number;
        
    private LocalDateTime discharge_date;
    
    private String collegiate_number;
    
    public User() {}

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



	public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
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

	public String getPostalAddress() {
		return postal_address;
	}

	public void setPostalAddress(String postalAddress) {
		this.postal_address = postalAddress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDNI() {
		return dni;
	}

	public void setDNI(String dNI) {
		dni = dNI;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phone_number = phoneNumber;
	}

	public LocalDateTime getDischargeDate() {
		return discharge_date;
	}

	public void setDischargeDate(LocalDateTime dischargeDate) {
		this.discharge_date = dischargeDate;
	}

	public String getCollegiateNumber() {
		return collegiate_number;
	}

	public void setCollegiateNumber(String collegiateNumber) {
		this.collegiate_number = collegiateNumber;
	}
    
}
