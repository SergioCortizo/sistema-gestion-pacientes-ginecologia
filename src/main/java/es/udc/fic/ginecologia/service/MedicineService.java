package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Medicine;

public interface MedicineService {

	Iterable<Medicine> findAllMedicines();

	void addMedicine(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

}
