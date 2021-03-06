package es.udc.fic.ginecologia.service;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.IncorrectPasswordException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.User;

public interface UserService {
	Iterable<User> findAllUsers();
	
	void registerUser(User user, Iterable<Integer> roles) throws DuplicateInstanceException;
	
	Iterable<Role> findAllRoles();

	User updateProfile(Integer id, String name, String email, String postalAddress, String location,
			String dNI, String phoneNumber, String collegiateNumber, Iterable<Integer> roles) throws InstanceNotFoundException;

	User findUserById(Integer id) throws InstanceNotFoundException;

	boolean existsUserById(Integer id);

	void changePassword(Integer id, String oldPassword, String newPassword)
			throws InstanceNotFoundException, IncorrectPasswordException;

	User updateProfile(Integer adminId, Integer id, String name, String email, String postalAddress,
			String location, String DNI, String phoneNumber, String collegiateNumber, Iterable<Integer> roles)
			throws InstanceNotFoundException, PermissionException;

	void changePassword(Integer adminId, Integer id, String oldPassword, String newPassword)
			throws InstanceNotFoundException, IncorrectPasswordException, PermissionException;
	

}
