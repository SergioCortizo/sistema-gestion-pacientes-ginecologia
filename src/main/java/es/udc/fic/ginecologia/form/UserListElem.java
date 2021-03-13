package es.udc.fic.ginecologia.form;

import java.time.format.DateTimeFormatter;
import java.util.Set;

import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;

public class UserListElem {
	private Integer id;
	
	private String name;
    
    private String username;
	
    private String email;
    
    private boolean enabled;
    
    private String discharge_date;
    
    private Set<Role> roles;

	public UserListElem(User user) {
		this.name = user.getName();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.enabled = user.isEnabled(); 
		this.id = user.getId();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		
		this.discharge_date = user.getDischarge_date().format(formatter);
		
		this.roles = user.getRoles();
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

	public String getDischarge_date() {
		return discharge_date;
	}

	public void setDischarge_date(String discharge_date) {
		this.discharge_date = discharge_date;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
