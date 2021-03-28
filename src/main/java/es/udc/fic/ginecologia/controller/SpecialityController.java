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

import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.SpecialitiesToAddForm;
import es.udc.fic.ginecologia.form.SpecialityConversor;
import es.udc.fic.ginecologia.form.SpecialityForm;
import es.udc.fic.ginecologia.form.SpecialityLine;
import es.udc.fic.ginecologia.model.CustomUserDetails;
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

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		List<SpecialityLine> specialities = SpecialityConversor
				.convertToSpecialityLine(specialityService.findAllSpecialities());

		prepareModel(model, specialities);

		return "speciality/speciality-list";
	}

	// Specialities list
	@GetMapping("/speciality/speciality-list-error")
	public String goToSpecialitiesManagementErrorDuplicateSpeciality(Model model) {
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

		List<SpecialityLine> specialities = SpecialityConversor
				.convertToSpecialityLine(specialityService.findAllSpecialities());

		prepareModel(model, specialities);
		model.addAttribute("duplicateSpeciality", true);

		return "speciality/speciality-list";
	}

	// Add speciality
	@PostMapping("/speciality/add-speciality")
	public String addSpeciality(@ModelAttribute SpecialityForm addSpecialityForm, Model model) {
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
			specialityService.addSpeciality(userId, addSpecialityForm.getName().trim());
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			return "redirect:/speciality/speciality-list-error";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/speciality/speciality-list";
	}

	// Search specialities
	@PostMapping("/speciality/search-specialities")
	public String searchSpecialities(@ModelAttribute SpecialityForm searchSpecialitiesForm, Model model) {
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

		List<SpecialityLine> specialities;

		try {
			specialities = SpecialityConversor.convertToSpecialityLine(specialityService.findSpecialities(userId,
					searchSpecialitiesForm.getName(), searchSpecialitiesForm.isEnabled()));
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/403";
		}

		prepareModel(model, specialities);

		return "speciality/speciality-list";
	}

	// Change speciality state
	@PostMapping("/speciality/change-speciality-state/{id}")
	public String changeSpecialityState(@PathVariable Integer id, Model model) {
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
			specialityService.changeEnablingSpeciality(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/speciality/speciality-list";
	}

	// Update speciality
	@PostMapping("/speciality/update/{id}")
	public String updateSpeciality(@PathVariable Integer id, @ModelAttribute SpecialityForm updateSpecialityForm,
			Model model) {

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
			specialityService.updateSpeciality(userId, id, updateSpecialityForm.getName().trim());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (DuplicateInstanceException e) {
			return "redirect:/speciality/speciality-list-error";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/speciality/speciality-list";
	}

	// Change specialities to user
	@PostMapping("/speciality/change-specialities/{id}")
	public String changeSpecialitiesToUser(@PathVariable Integer id,
			@ModelAttribute SpecialitiesToAddForm specialitiesToAddForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			userService.findUserById(id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/403";
			}
			return "/error/404";
		}

		try {
			specialityService.changeSpecialities(userId, id, specialitiesToAddForm.getSpecialitiesToAdd());
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/403";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/403";
			}
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/user/update/" + id;
	}

	private void prepareModel(Model model, List<SpecialityLine> specialities) {
		model.addAttribute("specialities", specialities);
		model.addAttribute("addSpecialityForm", new SpecialityForm());
		model.addAttribute("searchSpecialitiesForm", new SpecialityForm());
		model.addAttribute("updateSpecialityForm", new SpecialityForm());
	}
}
