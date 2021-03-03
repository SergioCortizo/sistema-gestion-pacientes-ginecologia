package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;

public interface UserService {
	Iterable<User> findAllUsers();
	
	void registerUser(User user, Iterable<Integer> roles) throws DuplicateInstanceException;
	
	Iterable<Role> findAllRoles();

}
