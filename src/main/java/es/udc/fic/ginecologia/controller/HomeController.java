package es.udc.fic.ginecologia.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.service.MessageService;
import es.udc.fic.ginecologia.service.SettingsService;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class HomeController {
	@Autowired 
	UserService userService;
	
	@Autowired
	SettingsService settingsService;
	
	@Autowired
	MessageService messageService;
	
	@RequestMapping("/")
    public String home(Model model)
    {	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			Integer userId = userDetails.getId();
			
			try {
				LocalDateTime datetime = userService.findUserById(userId).getLast_time_seen_notices();
				model.addAttribute("newNotices", messageService.countNewNotices(datetime));
			} catch (InstanceNotFoundException e) {
				return "/error/403";
			}
			
			return "index";
		}
		
		String logo = settingsService.getLogo().getValue();
		
		model.addAttribute("logo", logo);
		
        return "login";
    }
}
