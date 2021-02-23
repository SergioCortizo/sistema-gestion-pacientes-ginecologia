package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
}
