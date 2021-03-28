package es.udc.fic.ginecologia.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.SettingsForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.service.SettingsService;

@Controller
public class SettingsController {

	@Autowired
	PermissionChecker permissionChecker;

	@Autowired
	SettingsService settingsService;

	// Name and logo settings
	@GetMapping("/settings/name-and-logo")
	public String goToNameAndLogoSettings(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		SettingsForm settingsForm = new SettingsForm();

		settingsForm.setEnterpriseName(settingsService.getEnterpriseName().getValue());

		model.addAttribute("nameAndLogoForm", settingsForm);

		return "settings/name-and-logo";
	}

	// Change name and logo
	@PostMapping("/settings/change-name-and-logo")
	public String changeNameAndLogo(@ModelAttribute SettingsForm nameAndLogoForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		try {
			settingsService.setSettings(userId, nameAndLogoForm.getEnterpriseName(), nameAndLogoForm.getLogo());
		} catch (InstanceNotFoundException | IOException e) {
			return "/error/500";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/settings/name-and-logo";
	}
}
