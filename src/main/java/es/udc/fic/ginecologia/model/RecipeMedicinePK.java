package es.udc.fic.ginecologia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RecipeMedicinePK implements Serializable {

	private static final long serialVersionUID = 4765068114865669679L;

	@Column(name = "recipe_id")
	private Integer recipeId;

	@Column(name = "medicine_id")
	private Integer medicineId;

	public RecipeMedicinePK() {

	}

	public RecipeMedicinePK(Integer recipeId, Integer medicineId) {
		this.recipeId = recipeId;
		this.medicineId = medicineId;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	public Integer getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(Integer medicineId) {
		this.medicineId = medicineId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((medicineId == null) ? 0 : medicineId.hashCode());
		result = prime * result + ((recipeId == null) ? 0 : recipeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecipeMedicinePK other = (RecipeMedicinePK) obj;
		if (medicineId == null) {
			if (other.medicineId != null)
				return false;
		} else if (!medicineId.equals(other.medicineId))
			return false;
		if (recipeId == null) {
			if (other.recipeId != null)
				return false;
		} else if (!recipeId.equals(other.recipeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RecipeMedicinePK [recipeId=" + recipeId + ", medicineId=" + medicineId + "]";
	}

}
