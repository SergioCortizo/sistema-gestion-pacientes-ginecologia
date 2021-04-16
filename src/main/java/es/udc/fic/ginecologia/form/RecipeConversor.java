package es.udc.fic.ginecologia.form;

import java.util.HashSet;
import java.util.Set;

import es.udc.fic.ginecologia.model.Medicine;
import es.udc.fic.ginecologia.model.Recipe;
import es.udc.fic.ginecologia.model.RecipeMedicine;

public class RecipeConversor {

	public static final Set<Recipe> convertToRecipeList(MeetingForm meetingForm) {
		Set<Recipe> recipes = new HashSet<>();

		meetingForm.getRecipes().forEach(r -> {

			Recipe recipe = new Recipe(r.getDispensingDate(), r.getClarifications());
			
			Set<RecipeMedicine> recipeMedicines = new HashSet<>();

			r.getMedicines().forEach(m -> {
				RecipeMedicine recipeMedicine = new RecipeMedicine(m.getDenomination(), m.getDosification(),
						m.getFormOfAdministration(), m.getFormat(), m.getUnits(), m.getPosology());
				
				Medicine medicine = new Medicine();
				medicine.setId(m.getMedicineId());
				recipeMedicine.setMedicine(medicine);
				
				recipeMedicines.add(recipeMedicine);
			});
			
			recipe.setMedicines(recipeMedicines);

			recipes.add(recipe);

		});

		return recipes;

	}
}
