package es.udc.fic.ginecologia.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.LogForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.LogLevel;
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
			LogLevel[] logLevels = LogLevel.class.getEnumConstants();

			model.addAttribute("logs", logLines);
			model.addAttribute("logForm", new LogForm());
			model.addAttribute("logLevels", logLevels);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/log/log-list");
			return "/error/403";
		}

		LoggingUtility.logGetResource(username, "GET", "/log/log-list");

		return "log/log-list";
	}

	@GetMapping("/log/log-list-error")
	public String goToLogError(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/log/log-list-error");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/log/log-list-error");
			return "/error/403";
		}

		try {
			Iterable<LogLine> logLines = logService.findLogs(userId);
			LogLevel[] logLevels = LogLevel.class.getEnumConstants();

			model.addAttribute("logs", logLines);
			model.addAttribute("logForm", new LogForm());
			model.addAttribute("logLevels", logLevels);
			model.addAttribute("logSearchError", true);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/log/log-list-error");
			return "/error/403";
		}

		LoggingUtility.logGetResource(username, "GET", "/log/log-list-error");

		return "log/log-list";
	}

	@PostMapping("/log/search-logs")
	public String searchLogs(@ModelAttribute LogForm logForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/log/search-logs");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/log/search-logs");
			return "/error/403";
		}

		LocalDateTime date1 = LocalDateTime.of(
				logForm.getDate1() == null ? LocalDate.of(1970, 1, 1) : logForm.getDate1(),
				logForm.getTime1() == null ? LocalTime.of(0, 0) : logForm.getTime1());
		LocalDateTime date2 = LocalDateTime.of(logForm.getDate2() == null ? LocalDate.now() : logForm.getDate2(),
				logForm.getTime2() == null ? LocalTime.now() : logForm.getTime2());

		if (date1.isAfter(date2)) {
			return "redirect:/log/log-list-error";
		}

		try {
			Iterable<LogLine> logLines = logService.searchLogs(userId, logForm.getLevel(), date1, date2);

			LogLevel[] logLevels = LogLevel.class.getEnumConstants();

			model.addAttribute("logs", logLines);
			model.addAttribute("logForm", new LogForm());
			model.addAttribute("logLevels", logLevels);

		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/log/search-logs");
			return "/error/403";
		}
		
		LoggingUtility.logSearchLogs(username, logForm);

		return "log/log-list";
	}

}
