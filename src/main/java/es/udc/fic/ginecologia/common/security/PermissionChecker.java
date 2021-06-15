package es.udc.fic.ginecologia.common.security;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.model.User;

public interface PermissionChecker {
	public void checkUserExists(Integer userId) throws InstanceNotFoundException;
	
	public User checkUser(Integer id) throws InstanceNotFoundException;

	boolean checkIsAdmin(Integer userId) throws InstanceNotFoundException;

	boolean checkIsFacultative(Integer userId) throws InstanceNotFoundException;

	boolean checkIsCitations(Integer userId) throws InstanceNotFoundException;
}
