package es.udc.fic.ginecologia.service;

import java.util.List;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Medicine;

public interface MedicineService {

	Iterable<Medicine> findAllMedicines();

	void addMedicine(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void updateMedicine(Integer adminId, Integer medicineId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void changeEnablingMedicine(Integer adminId, Integer medicineId)
			throws InstanceNotFoundException, PermissionException;

	Iterable<Medicine> findMedicines(Integer adminId, String name, boolean enabled)
			throws InstanceNotFoundException, PermissionException;

	Medicine findMedicineById(Integer id) throws InstanceNotFoundException;

	Iterable<Medicine> findMedicinesById(List<Integer> ids);
}
