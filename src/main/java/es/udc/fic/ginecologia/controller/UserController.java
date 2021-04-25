package es.udc.fic.ginecologia.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.IncorrectPasswordException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.ChangePasswordForm;
import es.udc.fic.ginecologia.form.ScheduleConversor;
import es.udc.fic.ginecologia.form.ScheduleForm;
import es.udc.fic.ginecologia.form.SignUpForm;
import es.udc.fic.ginecologia.form.SpecialitiesToAddForm;
import es.udc.fic.ginecologia.form.UpdateForm;
import es.udc.fic.ginecologia.form.UserListElem;
import es.udc.fic.ginecologia.form.UserListElemConversor;
import es.udc.fic.ginecologia.form.UserSearchForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.Schedule;
import es.udc.fic.ginecologia.model.Speciality;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.service.SpecialityService;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	SpecialityService specialityService;

	@Autowired
	PermissionChecker permissionChecker;
	
	// Login form
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	// Login error
	@GetMapping("/login-error")
	public String loginErrorPage(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	// Register form
	@GetMapping("/user/register-error")
	public String registerPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/user/register-error");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/user/register-error");
			return "/error/403";
		}
		
		model.addAttribute("roles", userService.findAllRoles());
		model.addAttribute("signUpForm", new SignUpForm());
		model.addAttribute("duplicateUser", true);
		
		LoggingUtility.logGetResource(username, "GET", "/user/register");

		return "user/register";
	}

	// Register form
	@GetMapping("/user/register")
	public String registerPageError(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/user/register");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/user/register");
			return "/error/403";
		}
		
		model.addAttribute("roles", userService.findAllRoles());
		model.addAttribute("signUpForm", new SignUpForm());
		
		LoggingUtility.logGetResource(username, "GET", "/user/register");

		return "user/register";
	}

	// Endpoint to save new user
	@PostMapping("/user/register")
	public String saveUser(@ModelAttribute SignUpForm signUpForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/user/register");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/user/register");
			return "/error/403";
		}
		
		User newUser = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(),
				signUpForm.getPostalAddress(), signUpForm.getLocation(), signUpForm.getDNI(),
				signUpForm.getPhoneNumber(), signUpForm.getCollegiateNumber());

		newUser.setPassword(signUpForm.getPassword());
		Iterable<Integer> roleIds = Arrays.asList(signUpForm.getRoles());

		try {
			userService.registerUser(newUser, roleIds);
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicateUser(username, signUpForm);
			return "redirect:/user/register-error";
		}
		
		LoggingUtility.logRegisteredUser(username, signUpForm);

		return "redirect:/user/user-list";
	}

	// Users management
	@GetMapping("/user/user-list")
	public String goToUsersManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();
		
		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/user/user-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/user/user-list");
			return "/error/403";
		}

		List<UserListElem> userList = UserListElemConversor.generateUserList(userService.findAllUsers(), userId);

		List<Role> roles = prepareRoleSelectorElements();

		model.addAttribute("users", userList);
		model.addAttribute("roles", roles);
		model.addAttribute("userSearchForm", new UserSearchForm());
		
		LoggingUtility.logGetResource(username, "GET", "/user/user-list");

		return "user/user-list";
	}

	// Update own data form
	@GetMapping("/user/update-own-data")
	public String updateOwnDataForm(Model model) throws InstanceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		User user;

		try {
			user = userService.findUserById(userId);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/user/update-own-data");
			return "/error/403";
		}

		prepareModelUpdateTemplate(model, user);
		
		LoggingUtility.logGetResource(username, "GET", "/user/update-own-data");

		return "user/update-own-data";
	}

	// Update own data form (wrong password)
	@GetMapping("/user/update-own-data-wrong-password")
	public String updateOwnDataFormWrongPassword(Model model) throws InstanceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		User user;

		try {
			user = userService.findUserById(userId);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/user/update-own-data-wrong-password");
			return "/error/403";
		}

		prepareModelUpdateTemplate(model, user);
		model.addAttribute("wrongPassword", true);
		
		LoggingUtility.logGetResource(username, "GET", "/user/update-own-data-wrong-password");

		return "user/update-own-data";
	}

	// Update data for another user form
	@GetMapping("/user/update/{id}")
	public String updateDataFormForAnotherUser(@PathVariable Integer id, Model model) throws InstanceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		User user;

		try {
			user = userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "GET", "/user/update/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "GET", "/user/update/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), id, "GET", "/user/update/" + id);
			return "/error/404";
		}

		prepareModelUpdateTemplate(model, user);
		model.addAttribute("userId", id);
		
		LoggingUtility.logGetResource(username, "GET", "/user/update/" + id);

		return "user/update";
	}

	// Update data for another user form
	@GetMapping("/user/update/{id}/wrong-password")
	public String updateDataFormForAnotherUserWrongPassword(@PathVariable Integer id, Model model)
			throws InstanceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		User user;

		try {
			user = userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "GET", "/user/update/" + id + "/wrong-password");
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "GET", "/user/update/" + id + "/wrong-password");
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), id, "GET", "/user/update/" + id + "/wrong-password");
			return "/error/404";
		}

		prepareModelUpdateTemplate(model, user);
		model.addAttribute("userId", id);
		model.addAttribute("wrongPassword", true);
		
		LoggingUtility.logGetResource(username, "GET", "/user/update/" + id + "/wrong-password");

		return "user/update";
	}

	// Endpoint to update user
	@PostMapping("/user/update/{id}")
	public String saveUser(@PathVariable Integer id, @ModelAttribute UpdateForm updateForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "POST", "/user/update/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "POST", "/user/update/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), id, "POST", "/user/update/" + id);
			return "/error/404";
		}

		try {
			userService.updateProfile(userId, id, updateForm.getName(), updateForm.getEmail(),
					updateForm.getPostalAddress(), updateForm.getLocation(), updateForm.getDNI(),
					updateForm.getPhoneNumber(), updateForm.getCollegiateNumber(), updateForm.getRoles());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), id, "POST", "/user/update/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/user/update/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logUpdatedUser(username, id, updateForm);

		return "redirect:/user/update/" + id;
	}

	// Endpoint to update user
	@PostMapping("/user/update-own-data")
	public String saveUser(@ModelAttribute UpdateForm updateForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		if (!userService.existsUserById(userId)) {
			LoggingUtility.logDeniedAccess(username, "POST", "/user/update-own-data");
			return "/error/403";
		}

		User newUser;

		try {
			newUser = userService.updateProfile(userId, updateForm.getName(), updateForm.getEmail(),
					updateForm.getPostalAddress(), updateForm.getLocation(), updateForm.getDNI(),
					updateForm.getPhoneNumber(), updateForm.getCollegiateNumber(), updateForm.getRoles());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), userId, "POST", "/user/update-own-data");
			return "/error/404";
		}

		List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
		for (Role role : newUser.getRoles()) {
			updatedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		Authentication newAuth = new UsernamePasswordAuthenticationToken(new CustomUserDetails(newUser),
				authentication.getCredentials(), updatedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		
		LoggingUtility.logUpdatedUserOwnData(username, updateForm);

		return "redirect:/user/update-own-data";
	}

	// Endpoint to change your password
	@PostMapping("/user/change-password")
	public String changeOwnPassword(@ModelAttribute ChangePasswordForm changePasswordForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		if (!userService.existsUserById(userId)) {
			LoggingUtility.logDeniedAccess(username, "POST", "/user/change-password");
			return "/error/403";
		}

		try {
			userService.changePassword(userId, changePasswordForm.getOldPassword(),
					changePasswordForm.getNewPassword());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), userId, "POST", "/user/update-own-data");
			return "/error/404";
		} catch (IncorrectPasswordException e) {
			LoggingUtility.logWrongPasword(username, changePasswordForm.getOldPassword(), "POST", "/user/update-own-data");
			return "redirect:/user/update-own-data-wrong-password";
		}
		
		LoggingUtility.logChangePassword(username);

		return "redirect:/user/update-own-data";
	}

	// Endpoint to change another user's password
	@PostMapping("/user/change-password/{id}")
	public String changePassword(@PathVariable Integer id, @ModelAttribute ChangePasswordForm changePasswordForm,
			Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "POST", "/user/change-password/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "POST", "/user/change-password/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), userId, "POST", "/user/change-password/" + id);
			return "/error/404";
		}

		try {
			userService.changePassword(userId, id, changePasswordForm.getOldPassword(),
					changePasswordForm.getNewPassword());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), userId, "POST", "/user/change-password/" + id);
			return "/error/404";
		} catch (IncorrectPasswordException e) {
			LoggingUtility.logWrongPasword(username, id, changePasswordForm.getOldPassword(), "POST", "/user/update-own-data");
			return "redirect:/user/update/" + id + "/wrong-password";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/user/change-password/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangePassword(username, id);

		return "redirect:/user/update/" + id;
	}

	// Endpoint to change another user's password
	@PostMapping("/user/change-enabling-state/{id}")
	public String changeEnablingState(@PathVariable Integer id, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "POST", "/user/change-enabling-state/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "POST", "/user/change-enabling-state/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), userId, "POST", "/user/change-enabling-state/" + id);
			return "/error/404";
		}

		try {
			userService.changeUserState(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), userId, "POST", "/user/change-enabling-state/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/user/change-enabling-state/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangeEnablingState(username, User.class.getName(), id);

		return "redirect:/user/user-list";
	}

	// Endpoint to search users
	@PostMapping("/user/search")
	public String searchUsers(@ModelAttribute UserSearchForm userSearchForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String ownUsername = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(ownUsername, "POST", "/user/search");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e1) {
			LoggingUtility.logDeniedAccess(ownUsername, "POST", "/user/search");
			return "/error/403";
		}

		String username = userSearchForm.getLogin().trim().equals("") ? null : userSearchForm.getLogin().trim();
		String name = userSearchForm.getName().trim().equals("") ? null : userSearchForm.getName().trim();
		String email = userSearchForm.getEmail().trim().equals("") ? null : userSearchForm.getEmail().trim();
		LocalDateTime dateFrom = userSearchForm.getDateFrom() == null ? null
				: userSearchForm.getDateFrom().atStartOfDay();
		LocalDateTime dateTo = userSearchForm.getDateTo() == null ? null
				: userSearchForm.getDateTo().atTime(23, 59, 59);
		boolean enabled = userSearchForm.isEnabled();
		Integer roleId = userSearchForm.getRoleId() == null ? null : userSearchForm.getRoleId();

		Iterable<UserListElem> userList = new ArrayList<>();

		try {
			userList = UserListElemConversor.generateUserList(
					userService.findUsers(userId, username, name, email, dateFrom, dateTo, enabled, roleId), userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(ownUsername, "POST", "/user/search");
			return "/error/403";
		}

		List<Role> roles = prepareRoleSelectorElements();

		model.addAttribute("users", userList);
		model.addAttribute("roles", roles);
		model.addAttribute("userSearchForm", new UserSearchForm());
		
		LoggingUtility.logSearchUsers(ownUsername, userSearchForm, userList);

		return "/user/user-list";
	}

	// Endpoint to change another user's password
	@PostMapping("/user/change-schedule/{id}")
	public String changeSchedule(@PathVariable Integer id, @ModelAttribute ScheduleForm scheduleForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		User user = null;

		try {
			user = userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "POST", "/user/change-schedule/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "POST", "/user/change-schedule/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), id, "POST", "/user/change-schedule/" + id);
			return "/error/404";
		}

		Set<Schedule> schedules = ScheduleConversor.convertToScheduleSet(scheduleForm.getSchedules(), user);

		try {
			userService.changeSchedule(userId, id, schedules);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "POST", "/user/change-schedule/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "POST", "/user/change-schedule/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getName(), id, "POST", "/user/change-schedule/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/user/change-schedule/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangeSchedule(username, id, schedules);

		return "redirect:/user/update/" + id;
	}

	private List<Role> prepareRoleSelectorElements() {
		List<Role> roles = new ArrayList<>();

		Role roleAll = new Role("-");
		roles.add(roleAll);

		for (Role role : userService.findAllRoles()) {
			roles.add(role);
		}

		return roles;
	}

	private void prepareModelUpdateTemplate(Model model, User user) throws InstanceNotFoundException {
		UpdateForm form = new UpdateForm();
		form.setName(user.getName());
		form.setEmail(user.getEmail());
		form.setPostalAddress(user.getPostal_address());
		form.setLocation(user.getLocation());
		form.setDNI(user.getDni());
		form.setPhoneNumber(user.getPhone_number());
		form.setCollegiateNumber(user.getCollegiate_number());

		Iterable<Role> roles = user.getRoles();

		List<Integer> roleIds = new ArrayList<>();

		for (Role role : roles) {
			roleIds.add(role.getId());
		}

		form.setRoles(roleIds);

		List<Schedule> schedules = new ArrayList<>(user.getSchedules());
		ScheduleForm scheduleForm = ScheduleConversor.prepareScheduleForm(schedules);

		List<Speciality> specialitiesFromUser = StreamSupport.stream(user.getSpecialities().spliterator(), false)
				.filter(s -> s.isEnabled()).collect(Collectors.toList());
		List<Speciality> specialities = StreamSupport
				.stream(specialityService.findAllSpecialities().spliterator(), false)
				.filter(s -> !specialitiesFromUser.contains(s) && s.isEnabled()).collect(Collectors.toList());

		model.addAttribute("roles", userService.findAllRoles());
		model.addAttribute("specialities", specialities);
		model.addAttribute("specialitiesFromUser", specialitiesFromUser);
		model.addAttribute("isFacultative", permissionChecker.checkIsFacultative(user.getId()));
		model.addAttribute("updateForm", form);
		model.addAttribute("scheduleForm", scheduleForm);
		model.addAttribute("changePasswordForm", new ChangePasswordForm());
		model.addAttribute("specialitiesToAddForm", new SpecialitiesToAddForm());

	}
	
	
}
