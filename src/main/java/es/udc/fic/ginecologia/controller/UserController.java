package es.udc.fic.ginecologia.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.form.SignUpForm;
import es.udc.fic.ginecologia.form.UserListElem;
import es.udc.fic.ginecologia.form.UserListElemConversor;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class UserController {
	@Autowired
	UserService userService;

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
		List<UserListElem> userList = UserListElemConversor.generateUserList(userService.findAllUsers());

		model.addAttribute("users", userList);

		return "user/user-list";
	}

}
