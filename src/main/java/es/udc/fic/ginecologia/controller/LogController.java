package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.LogLine;
import es.udc.fic.ginecologia.service.LogService;

@Controller
public class LogController {
	
	@Autowired
	LogService logService;

	@Autowired
	PermissionChecker permissionChecker;
	
	@GetMapping("/log/log-list")
	public String goToLog(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/log/log-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/log/log-list");
			return "/error/403";
		}
		
		try {
			Iterable<LogLine> logLines = logService.findLogs(userId);
			model.addAttribute("logs", logLines);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/log/log-list");
			return "/error/403";
		}
		
		LoggingUtility.logGetResource(username, "GET", "/log/log-list");
	
		return "log/log-list";
	}
}
