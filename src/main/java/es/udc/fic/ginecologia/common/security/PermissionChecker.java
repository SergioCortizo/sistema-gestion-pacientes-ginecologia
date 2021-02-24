package es.udc.fic.ginecologia.common.security;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.model.User;

public interface PermissionChecker {
	public void checkUserExists(Long userId) throws InstanceNotFoundException;
	
	public User checkUser(Long userId) throws InstanceNotFoundException;
}