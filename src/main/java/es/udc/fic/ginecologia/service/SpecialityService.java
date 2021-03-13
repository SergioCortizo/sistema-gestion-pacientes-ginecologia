package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Speciality;

public interface SpecialityService {

	Iterable<Speciality> findAllSpecialities();

	void addSpeciality(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void updateSpeciality(Integer adminId, Integer specialityId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void changeEnablingSpeciality(Integer adminId, Integer specialityId)
			throws InstanceNotFoundException, PermissionException;

	Iterable<Speciality> findSpecialities(Integer adminId, String name, boolean enabled)
			throws InstanceNotFoundException, PermissionException;

	Iterable<Speciality> findSpecialitiesFromUser(Integer adminId, Integer userId)
			throws InstanceNotFoundException, PermissionException;

	void changeSpecialities(Integer adminId, Integer userId, Iterable<Speciality> specialities)
			throws InstanceNotFoundException, PermissionException;

}
