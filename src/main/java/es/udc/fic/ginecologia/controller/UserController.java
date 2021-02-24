package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	@GetMapping("/register")
	public String registerPage() {
	    return "register";
	}
	
}
