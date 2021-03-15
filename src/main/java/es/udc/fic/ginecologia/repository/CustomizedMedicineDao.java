package es.udc.fic.ginecologia.repository;

import es.udc.fic.ginecologia.model.Medicine;

public interface CustomizedMedicineDao {

	Iterable<Medicine> findMedicines(String name, boolean enabled);
	
}
