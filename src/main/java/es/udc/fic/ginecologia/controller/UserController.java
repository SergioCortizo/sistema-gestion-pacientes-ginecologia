package es.udc.fic.ginecologia.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.IncorrectPasswordException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.ChangePasswordForm;
import es.udc.fic.ginecologia.form.ScheduleConversor;
import es.udc.fic.ginecologia.form.ScheduleForm;
import es.udc.fic.ginecologia.form.SignUpForm;
import es.udc.fic.ginecologia.form.UpdateForm;
import es.udc.fic.ginecologia.form.UserListElem;
import es.udc.fic.ginecologia.form.UserListElemConversor;
import es.udc.fic.ginecologia.form.UserSearchForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Role;
import es.udc.fic.ginecologia.model.Schedule;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserService userService;

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
		model.addAttribute("roles", userService.findAllRoles());
		model.addAttribute("signUpForm", new SignUpForm());
		model.addAttribute("duplicateUser", true);

		return "user/register";
	}

	// Register form
	@GetMapping("/user/register")
	public String registerPageError(Model model) {
		model.addAttribute("roles", userService.findAllRoles());
		model.addAttribute("signUpForm", new SignUpForm());

		return "user/register";
	}

	// Endpoint to save new user
	@PostMapping("/user/register")
	public String saveUser(@ModelAttribute SignUpForm signUpForm, Model model) {
		User newUser = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(),
				signUpForm.getPostalAddress(), signUpForm.getLocation(), signUpForm.getDNI(),
				signUpForm.getPhoneNumber(), signUpForm.getCollegiateNumber());

		newUser.setPassword(signUpForm.getPassword());
		Iterable<Integer> roleIds = Arrays.asList(signUpForm.getRoles());

		try {
			userService.registerUser(newUser, roleIds);
		} catch (DuplicateInstanceException e) {
			return "redirect:/user/register-error";
		}

		return "redirect:/user/user-list";
	}

	// Users management
	@GetMapping("/user/user-list")
	public String goToUsersManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		List<UserListElem> userList = UserListElemConversor.generateUserList(userService.findAllUsers(), userId);

		List<Role> roles = prepareRoleSelectorElements();

		model.addAttribute("users", userList);
		model.addAttribute("roles", roles);
		model.addAttribute("userSearchForm", new UserSearchForm());

		return "user/user-list";
	}

	// Update own data form
	@GetMapping("/user/update-own-data")
	public String updateOwnDataForm(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		User user;

		try {
			user = userService.findUserById(userId);
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		prepareModelUpdateTemplate(model, user);

		return "user/update-own-data";
	}

	// Update own data form (wrong password)
	@GetMapping("/user/update-own-data-wrong-password")
	public String updateOwnDataFormWrongPassword(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		User user;

		try {
			user = userService.findUserById(userId);
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		prepareModelUpdateTemplate(model, user);
		model.addAttribute("wrongPassword", true);

		return "user/update-own-data";
	}

	// Update data for another user form
	@GetMapping("/user/update/{id}")
	public String updateDataFormForAnotherUser(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		User user;

		try {
			user = userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/401";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/401";
			}
			return "/error/404";
		}

		List<Schedule> schedules = new ArrayList<>(user.getSchedules());
		ScheduleForm scheduleForm = ScheduleConversor.prepareScheduleForm(schedules);
		
		prepareModelUpdateTemplate(model, user);
		model.addAttribute("userId", id);
		model.addAttribute("scheduleForm", scheduleForm);

		return "user/update";
	}

	// Update data for another user form
	@GetMapping("/user/update/{id}/wrong-password")
	public String updateDataFormForAnotherUserWrongPassword(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		User user;

		try {
			user = userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/401";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/401";
			}
			return "/error/404";
		}

		prepareModelUpdateTemplate(model, user);
		model.addAttribute("userId", id);
		model.addAttribute("wrongPassword", true);

		return "user/update";
	}

	// Endpoint to update user
	@PostMapping("/user/update/{id}")
	public String saveUser(@PathVariable Integer id, @ModelAttribute UpdateForm updateForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/401";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/401";
			}
			return "/error/404";
		}

		try {
			userService.updateProfile(userId, id, updateForm.getName(), updateForm.getEmail(),
					updateForm.getPostalAddress(), updateForm.getLocation(), updateForm.getDNI(),
					updateForm.getPhoneNumber(), updateForm.getCollegiateNumber(), updateForm.getRoles());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/401";
		}

		return "redirect:/user/update/" + id;
	}

	// Endpoint to update user
	@PostMapping("/user/update-own-data")
	public String saveUser(@ModelAttribute UpdateForm updateForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		if (!userService.existsUserById(userId))
			return "/error/401";

		User newUser;

		try {
			newUser = userService.updateProfile(userId, updateForm.getName(), updateForm.getEmail(),
					updateForm.getPostalAddress(), updateForm.getLocation(), updateForm.getDNI(),
					updateForm.getPhoneNumber(), updateForm.getCollegiateNumber(), updateForm.getRoles());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		}

		List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
		for (Role role : newUser.getRoles()) {
			updatedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		Authentication newAuth = new UsernamePasswordAuthenticationToken(new CustomUserDetails(newUser),
				authentication.getCredentials(), updatedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(newAuth);

		return "redirect:/user/update-own-data";
	}

	// Endpoint to change your password
	@PostMapping("/user/change-password")
	public String changeOwnPassword(@ModelAttribute ChangePasswordForm changePasswordForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		if (!userService.existsUserById(userId))
			return "/error/401";

		try {
			userService.changePassword(userId, changePasswordForm.getOldPassword(),
					changePasswordForm.getNewPassword());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (IncorrectPasswordException e) {
			return "redirect:/user/update-own-data-wrong-password";
		}

		return "redirect:/user/update-own-data";
	}

	// Endpoint to change another user's password
	@PostMapping("/user/change-password/{id}")
	public String changePassword(@PathVariable Integer id, @ModelAttribute ChangePasswordForm changePasswordForm,
			Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/401";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/401";
			}
			return "/error/404";
		}

		try {
			userService.changePassword(userId, id, changePasswordForm.getOldPassword(),
					changePasswordForm.getNewPassword());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (IncorrectPasswordException e) {
			return "redirect:/user/update/" + id + "/wrong-password";
		} catch (PermissionException e) {
			return "/error/401";
		}

		return "redirect:/user/update/" + id;
	}

	// Endpoint to change another user's password
	@PostMapping("/user/change-enabling-state/{id}")
	public String changeEnablingState(@PathVariable Integer id, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/401";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/401";
			}
			return "/error/404";
		}

		try {
			userService.changeUserState(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/401";
		}

		return "redirect:/user/user-list";
	}

	// Endpoint to search users
	@PostMapping("/user/search")
	public String searchUsers(@ModelAttribute UserSearchForm userSearchForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e1) {
			return "/error/401";
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
			return "/error/401";
		}

		List<Role> roles = prepareRoleSelectorElements();

		model.addAttribute("users", userList);
		model.addAttribute("roles", roles);
		model.addAttribute("userSearchForm", new UserSearchForm());

		return "/user/user-list";
	}

	// Endpoint to change another user's password
	@PostMapping("/user/change-schedule/{id}")
	public String changeSchedule(@PathVariable Integer id, @ModelAttribute ScheduleForm scheduleForm,
			Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		User user = null;
		
		try {
			user = userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/401";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/401";
			}
			return "/error/404";
		}
		
		Set<Schedule> schedules = ScheduleConversor.convertToScheduleSet(scheduleForm.getSchedules(), user);
		
		try {
			userService.changeSchedule(userId, id, schedules);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/401";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/401";
			}
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/401";
		}

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

	private void prepareModelUpdateTemplate(Model model, User user) {
		UpdateForm form = new UpdateForm();
		form.setName(user.getName());
		form.setEmail(user.getEmail());
		form.setPostalAddress(user.getPostalAddress());
		form.setLocation(user.getLocation());
		form.setDNI(user.getDNI());
		form.setPhoneNumber(user.getPhoneNumber());
		form.setCollegiateNumber(user.getCollegiateNumber());

		Iterable<Role> roles = user.getRoles();

		List<Integer> roleIds = new ArrayList<>();

		for (Role role : roles) {
			roleIds.add(role.getId());
		}

		form.setRoles(roleIds);

		model.addAttribute("roles", userService.findAllRoles());
		model.addAttribute("updateForm", form);
		model.addAttribute("changePasswordForm", new ChangePasswordForm());
	}
}
