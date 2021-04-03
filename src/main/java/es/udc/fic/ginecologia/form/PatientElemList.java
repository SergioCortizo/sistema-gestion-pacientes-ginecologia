package es.udc.fic.ginecologia.form;

import es.udc.fic.ginecologia.model.Patient;

public class PatientElemList {
	private Long id;

	private String name;
	
	private String DNI_NIF;
	
	private String hist_numsergas;
	
	private boolean enabled;
	
	private boolean patientOfInterest;
	
	public PatientElemList() {
		
	}

	public PatientElemList(Patient patient) {
		this.id = patient.getId();
		this.name = patient.getName();
		this.DNI_NIF = patient.getDNI_NIF();
		this.hist_numsergas = patient.getHist_numsergas();
		this.enabled = patient.isEnabled();
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

	public boolean isPatientOfInterest() {
		return patientOfInterest;
	}

	public void setPatientOfInterest(boolean patientOfInterest) {
		this.patientOfInterest = patientOfInterest;
	}
	
}
