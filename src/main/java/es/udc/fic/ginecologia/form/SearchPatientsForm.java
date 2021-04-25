package es.udc.fic.ginecologia.form;

import es.udc.fic.ginecologia.model.Patient;

public class SearchPatientsForm {
	private String name;

	private String DNI_NIF;

	private String hist_numsergas;

	private boolean enabled;

	public SearchPatientsForm() {
		
	}

	public SearchPatientsForm(Patient patient) {
		this.name = patient.getName();
		this.DNI_NIF = patient.getDNI_NIF();
		this.hist_numsergas = patient.getHist_numsergas();
		this.enabled = patient.isEnabled();
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

	public String getHist_numsergas() {
		return hist_numsergas;
	}

	public void setHist_numsergas(String hist_numsergas) {
		this.hist_numsergas = hist_numsergas;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "SearchPatientsForm [name=" + name + ", DNI_NIF=" + DNI_NIF + ", hist_numsergas=" + hist_numsergas
				+ ", enabled=" + enabled + "]";
	}
	
}
