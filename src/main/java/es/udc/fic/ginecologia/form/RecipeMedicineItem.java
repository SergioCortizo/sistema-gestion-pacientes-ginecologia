package es.udc.fic.ginecologia.form;

public class RecipeMedicineItem {

	private Integer medicineId;
	
	private String denomination;

	private String dosification;

	private String formOfAdministration;

	private Integer format;

	private Integer units;

	private String posology;

	public Integer getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(Integer medicineId) {
		this.medicineId = medicineId;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getDosification() {
		return dosification;
	}

	public void setDosification(String dosification) {
		this.dosification = dosification;
	}

	public String getFormOfAdministration() {
		return formOfAdministration;
	}

	public void setFormOfAdministration(String formOfAdministration) {
		this.formOfAdministration = formOfAdministration;
	}

	public Integer getFormat() {
		return format;
	}

	public void setFormat(Integer format) {
		this.format = format;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public String getPosology() {
		return posology;
	}

	public void setPosology(String posology) {
		this.posology = posology;
	}

	@Override
	public String toString() {
		return "RecipeMedicineItem [medicineId=" + medicineId + ", denomination=" + denomination + ", dosification="
				+ dosification + ", formOfAdministration=" + formOfAdministration + ", format=" + format + ", units="
				+ units + ", posology=" + posology + "]";
	}

}
