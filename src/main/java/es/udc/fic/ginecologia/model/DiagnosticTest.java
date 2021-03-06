package es.udc.fic.ginecologia.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "diagnostic_test")
public class DiagnosticTest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private boolean enabled;

	public DiagnosticTest() {

	}

	public DiagnosticTest(String name) {
		this.name = name;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "DiagnosticTest [id=" + id + ", name=" + name + ", enabled=" + enabled + "]";
	}

}
