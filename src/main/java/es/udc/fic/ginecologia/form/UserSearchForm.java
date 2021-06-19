package es.udc.fic.ginecologia.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class UserSearchForm {
	private String login;

	private String name;

	private String email;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dateFrom;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dateTo;

	private boolean enabled;

	private Integer roleId;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

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

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "UserSearchForm [login=" + login + ", name=" + name + ", email=" + email + ", dateFrom=" + (dateFrom != null ? dateFrom.toString() : "")
				+ ", dateTo=" + (dateTo != null ? dateTo.toString() : "") + ", enabled=" + enabled + ", roleId=" + roleId + "]";
	}
	
}
