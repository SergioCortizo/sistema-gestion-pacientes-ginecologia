package es.udc.fic.ginecologia.form;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class RecipeItem {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dispensingDate;

	private String clarifications;
	
	private List<RecipeMedicineItem> medicines;

	public LocalDate getDispensingDate() {
		return dispensingDate;
	}

	public void setDispensingDate(LocalDate dispensingDate) {
		this.dispensingDate = dispensingDate;
	}

	public String getClarifications() {
		return clarifications;
	}

	public void setClarifications(String clarifications) {
		this.clarifications = clarifications;
	}

	public List<RecipeMedicineItem> getMedicines() {
		return medicines;
	}

	public void setMedicines(List<RecipeMedicineItem> medicines) {
		this.medicines = medicines;
	}

	@Override
	public String toString() {
		return "RecipeItem [dispensingDate=" + dispensingDate.toString() + ", clarifications=" + clarifications + ", medicines="
				+ medicines + "]";
	}
	
}
