package es.udc.fic.ginecologia.form;

import java.util.List;

public class UpdateForm {
	private String name;

    private String email;
    
    private String postalAddress;
    
    private String location;
    
    private String DNI;
    
    private String phoneNumber;
    
    private String collegiateNumber;
    
    private List<Integer> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String dNI) {
		DNI = dNI;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCollegiateNumber() {
		return collegiateNumber;
	}

	public void setCollegiateNumber(String collegiateNumber) {
		this.collegiateNumber = collegiateNumber;
	}

	public List<Integer> getRoles() {
		return roles;
	}

	public void setRoles(List<Integer> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UpdateForm [name=" + name + ", email=" + email + ", postalAddress=" + postalAddress + ", location="
				+ location + ", DNI=" + DNI + ", phoneNumber=" + phoneNumber + ", collegiateNumber=" + collegiateNumber
				+ ", roles=" + roles + "]";
	}
    
}
