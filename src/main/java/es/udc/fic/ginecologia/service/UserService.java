package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;
import java.util.Set;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.Schedule;
import es.udc.fic.ginecologia.model.User;

public interface UserService {
	Iterable<User> findAllUsers();
	
	Iterable<User> findAllFacultatives();
	
	void registerUser(User user, Iterable<Integer> roles) throws DuplicateInstanceException;
	
	Iterable<Role> findAllRoles();

	User updateProfile(Integer id, String name, String email, String postalAddress, String location,
			String dNI, String phoneNumber, String collegiateNumber, Iterable<Integer> roles) throws InstanceNotFoundException;

	User findUserById(Integer id) throws InstanceNotFoundException;

	boolean existsUserById(Integer id);

	User updateProfile(Integer adminId, Integer id, String name, String email, String postalAddress,
			String location, String DNI, String phoneNumber, String collegiateNumber, Iterable<Integer> roles)
			throws InstanceNotFoundException, PermissionException;

	void changeUserState(Integer adminId, Integer userId) throws InstanceNotFoundException, PermissionException;

	Iterable<User> findUsers(Integer adminId, String login, String name, String email, LocalDateTime firstDischargeDate,
			LocalDateTime lastDischargeDate, boolean enabled, Integer roleId) throws PermissionException, InstanceNotFoundException;

	void changeSchedule(Integer adminId, Integer userId, Set<Schedule> schedules) throws InstanceNotFoundException, PermissionException;

	void changePassword(Integer adminId, Integer id, String newPassword)
			throws InstanceNotFoundException, PermissionException;

	void changePassword(Integer id, String newPassword) throws InstanceNotFoundException;
	

}
