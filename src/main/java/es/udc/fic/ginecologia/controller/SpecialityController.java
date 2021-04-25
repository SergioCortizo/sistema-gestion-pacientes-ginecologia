package es.udc.fic.ginecologia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.SpecialitiesToAddForm;
import es.udc.fic.ginecologia.form.SpecialityConversor;
import es.udc.fic.ginecologia.form.SpecialityForm;
import es.udc.fic.ginecologia.form.SpecialityLine;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Speciality;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.service.SpecialityService;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class SpecialityController {
	@Autowired
	SpecialityService specialityService;

	@Autowired
	PermissionChecker permissionChecker;

	@Autowired
	UserService userService;

	// Specialities list
	@GetMapping("/speciality/speciality-list")
	public String goToSpecialitiesManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/speciality/speciality-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/speciality/speciality-list");
			return "/error/403";
		}

		List<SpecialityLine> specialities = SpecialityConversor
				.convertToSpecialityLine(specialityService.findAllSpecialities());

		prepareModel(model, specialities);

		LoggingUtility.logGetResource(username, "GET", "/speciality/speciality-list");

		return "speciality/speciality-list";
	}

	// Specialities list
	@GetMapping("/speciality/speciality-list-error")
	public String goToSpecialitiesManagementErrorDuplicateSpeciality(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/speciality/speciality-list-error");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/speciality/speciality-list-error");
			return "/error/403";
		}

		List<SpecialityLine> specialities = SpecialityConversor
				.convertToSpecialityLine(specialityService.findAllSpecialities());

		prepareModel(model, specialities);
		model.addAttribute("duplicateSpeciality", true);

		LoggingUtility.logGetResource(username, "GET", "/speciality/speciality-list-error");

		return "speciality/speciality-list";
	}

	// Add speciality
	@PostMapping("/speciality/add-speciality")
	public String addSpeciality(@ModelAttribute SpecialityForm addSpecialityForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/speciality/add-speciality");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/add-speciality");
			return "/error/403";
		}

		try {
			specialityService.addSpeciality(userId, addSpecialityForm.getName().trim());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/add-speciality");
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicateSpeciality(username, addSpecialityForm);
			return "redirect:/speciality/speciality-list-error";
		}

		LoggingUtility.logAddedSpeciality(username, addSpecialityForm);

		return "redirect:/speciality/speciality-list";
	}

	// Search specialities
	@PostMapping("/speciality/search-specialities")
	public String searchSpecialities(@ModelAttribute SpecialityForm searchSpecialitiesForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/speciality/search-specialities");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/search-specialities");
			return "/error/403";
		}

		List<SpecialityLine> specialities;

		try {
			specialities = SpecialityConversor.convertToSpecialityLine(specialityService.findSpecialities(userId,
					searchSpecialitiesForm.getName(), searchSpecialitiesForm.isEnabled()));
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/search-specialities");
			return "/error/403";
		}

		prepareModel(model, specialities);

		LoggingUtility.logSearchSpecialities(username, searchSpecialitiesForm, specialities);

		return "speciality/speciality-list";
	}

	// Change speciality state
	@PostMapping("/speciality/change-speciality-state/{id}")
	public String changeSpecialityState(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-speciality-state/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-speciality-state/" + id);
			return "/error/403";
		}

		try {
			specialityService.changeEnablingSpeciality(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Speciality.class.getSimpleName(), id, "POST",
					"/speciality/change-speciality-state/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-speciality-state/" + id);
			return "/error/403";
		}

		LoggingUtility.logChangeEnablingState(username, Speciality.class.getSimpleName(), id);

		return "redirect:/speciality/speciality-list";
	}

	// Update speciality
	@PostMapping("/speciality/update/{id}")
	public String updateSpeciality(@PathVariable Integer id, @ModelAttribute SpecialityForm updateSpecialityForm,
			Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/speciality/update/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/update/" + id);
			return "/error/403";
		}

		try {
			specialityService.updateSpeciality(userId, id, updateSpecialityForm.getName().trim());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Speciality.class.getSimpleName(), id, "POST",
					"/speciality/update/" + id);
			return "/error/404";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicateSpeciality(username, updateSpecialityForm);
			return "redirect:/speciality/speciality-list-error";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/update/" + id);
			return "/error/403";
		}

		LoggingUtility.logUpdateSpeciality(username, id, updateSpecialityForm);

		return "redirect:/speciality/speciality-list";
	}

	// Change specialities to user
	@PostMapping("/speciality/change-specialities/{id}")
	public String changeSpecialitiesToUser(@PathVariable Integer id,
			@ModelAttribute SpecialitiesToAddForm specialitiesToAddForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-specialities/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-specialities/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getSimpleName(), id, "POST",
					"/speciality/change-specialities/" + id);
			return "/error/404";
		}

		try {
			specialityService.changeSpecialities(userId, id, specialitiesToAddForm.getSpecialitiesToAdd());
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-specialities/" + id);
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-specialities/" + id);
				return "/error/403";
			}
			LoggingUtility.logInstanceNotFound(username, User.class.getSimpleName(), id, "POST",
					"/speciality/change-specialities/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/speciality/change-specialities/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangedSpecialities(username, id, specialitiesToAddForm);

		return "redirect:/user/update/" + id;
	}

	private void prepareModel(Model model, List<SpecialityLine> specialities) {
		model.addAttribute("specialities", specialities);
		model.addAttribute("addSpecialityForm", new SpecialityForm());
		model.addAttribute("searchSpecialitiesForm", new SpecialityForm());
		model.addAttribute("updateSpecialityForm", new SpecialityForm());
	}
}
