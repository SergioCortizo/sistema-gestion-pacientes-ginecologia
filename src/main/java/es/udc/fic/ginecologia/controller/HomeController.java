package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fic.ginecologia.service.UserService;

@Controller
public class HomeController {
	@Autowired 
	UserService userService;
	
	@RequestMapping("/")
    public String home(Model model)
    {	
        model.addAttribute("users", userService.findAllUsers());
        return "index";
    }
}
