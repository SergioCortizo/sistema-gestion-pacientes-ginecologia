package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.Schedule;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.RoleDao;
import es.udc.fic.ginecologia.repository.ScheduleDao;
import es.udc.fic.ginecologia.repository.UserDao;

@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserDao userRepo;

	@Autowired
	private RoleDao roleRepo;

	@Autowired
	private ScheduleDao scheduleRepo;

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private BCryptPasswordEncoder encrypter;

	@Override
	public Iterable<User> findAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.getUserByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}

		CustomUserDetails result = new CustomUserDetails(user);

		return result;
	}

	@Override
	public Integer registerUser(User user, Iterable<Integer> roles) throws DuplicateInstanceException {

		if (userRepo.existsByUsername(user.getUsername())) {
			throw new DuplicateInstanceException("entities.user", user.getUsername());
		}

		Set<Role> foundRoles = new HashSet<Role>();
		roleRepo.findAllById(roles).forEach(foundRoles::add);

		user.setPassword(encrypter.encode(user.getPassword()));
		user.setRoles(foundRoles);

		userRepo.save(user);

		return user.getId();
	}

	@Override
	public Iterable<Role> findAllRoles() {
		return roleRepo.findAll();
	}

	@Override
	public User updateProfile(Integer id, String name, String email, String postalAddress, String location, String DNI,
			String phoneNumber, String collegiateNumber, Iterable<Integer> roles) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(id);

		user.setName(name);
		user.setEmail(email);
		user.setPostal_address(postalAddress);
		user.setLocation(location);
		user.setDni(DNI);
		user.setPhone_number(phoneNumber);
		user.setCollegiate_number(collegiateNumber);

		if (roles != null) {
			Set<Role> foundRoles = new HashSet<Role>();
			roleRepo.findAllById(roles).forEach(foundRoles::add);
			user.setRoles(foundRoles);
		}

		return user;

	}

	@Override
	public User updateProfile(Integer adminId, Integer id, String name, String email, String postalAddress,
			String location, String DNI, String phoneNumber, String collegiateNumber, Iterable<Integer> roles)
			throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(id);

		user.setName(name);
		user.setEmail(email);
		user.setPostal_address(postalAddress);
		user.setLocation(location);
		user.setDni(DNI);
		user.setPhone_number(phoneNumber);
		user.setCollegiate_number(collegiateNumber);

		Set<Role> foundRoles = new HashSet<Role>();
		roleRepo.findAllById(roles).forEach(foundRoles::add);

		user.setRoles(foundRoles);

		return user;

	}

	@Override
	public void changePassword(Integer id, String newPassword) throws InstanceNotFoundException {

		User user = permissionChecker.checkUser(id);

		user.setPassword(encrypter.encode(newPassword));

	}

	@Override
	public void changePassword(Integer adminId, Integer id, String newPassword)
			throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(id);

		user.setPassword(encrypter.encode(newPassword));

	}

	@Override
	public User findUserById(Integer id) throws InstanceNotFoundException {
		return permissionChecker.checkUser(id);
	}

	@Override
	public boolean existsUserById(Integer id) {
		return userRepo.existsById(id);
	}

	@Override
	public void changeUserState(Integer adminId, Integer userId) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(userId);

		user.setEnabled(!user.isEnabled());

	}

	@Override
	public Iterable<User> findUsers(Integer adminId, String login, String name, String email,
			LocalDateTime firstDischargeDate, LocalDateTime lastDischargeDate, boolean enabled, Integer roleId)
			throws PermissionException, InstanceNotFoundException {

		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		return userRepo.findUsers(login, name, email, firstDischargeDate, lastDischargeDate, enabled, roleId);
	}

	@Override
	public void changeSchedule(Integer adminId, Integer userId, Set<Schedule> schedules)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(adminId)) {
			throw new PermissionException();
		}

		User user = permissionChecker.checkUser(userId);

		Set<Schedule> actualSchedules = user.getSchedules();

		scheduleRepo.deleteAll(actualSchedules);

		for (Schedule schedule : schedules) {
			scheduleRepo.save(schedule);
		}
	}

	@Override
	public Iterable<User> findAllFacultatives() {
		return userRepo.findFacultatives();
	}
}
