package es.udc.fic.ginecologia.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recipe_medicine")
public class RecipeMedicine implements Serializable {

	private static final long serialVersionUID = -4913922422603786307L;

	@EmbeddedId
	private RecipeMedicinePK pk;

	private String denomination;

	private String dosification;

	private String formOfAdministration;

	private Integer format;

	private Integer units;

	private String posology;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "medicine_id", insertable = false, updatable = false)
	private Medicine medicine;

	public RecipeMedicine() {

	}

	public RecipeMedicine(String denomination, String dosification, String formOfAdministration, Integer format,
			Integer units, String posology) {
		this.denomination = denomination;
		this.dosification = dosification;
		this.formOfAdministration = formOfAdministration;
		this.format = format;
		this.units = units;
		this.posology = posology;
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

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public RecipeMedicinePK getPk() {
		return pk;
	}

	public void setPk(RecipeMedicinePK pk) {
		this.pk = pk;
	}

}
