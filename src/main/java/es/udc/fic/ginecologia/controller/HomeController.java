package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fic.ginecologia.service.SettingsService;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class HomeController {
	@Autowired 
	UserService userService;
	
	@Autowired
	SettingsService settingsService;
	
	@RequestMapping("/")
    public String home(Model model)
    {	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (!(authentication instanceof AnonymousAuthenticationToken)) return "index";
		
		String logo = settingsService.getLogo().getValue();
		
		model.addAttribute("logo", logo);
		
        return "login";
    }
}
