package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Contraceptive;

public interface ContraceptiveService {

	Iterable<Contraceptive> findAllContraceptives();

	void addContraceptive(Integer adminId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void updateContraceptive(Integer adminId, Integer contraceptiveId, String name)
			throws InstanceNotFoundException, PermissionException, DuplicateInstanceException;

	void changeEnablingContraceptive(Integer adminId, Integer contraceptiveId)
			throws InstanceNotFoundException, PermissionException;

	Iterable<Contraceptive> findContraceptives(Integer adminId, String name, boolean enabled)
			throws InstanceNotFoundException, PermissionException;

}
