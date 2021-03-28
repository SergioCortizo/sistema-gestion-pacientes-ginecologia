package es.udc.fic.ginecologia.controller;

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
import es.udc.fic.ginecologia.form.MedicineForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Medicine;
import es.udc.fic.ginecologia.service.MedicineService;

@Controller
public class MedicineController {

	@Autowired
	MedicineService medicineService;

	@Autowired
	PermissionChecker permissionChecker;

	// Medicines list
	@GetMapping("/medicine/medicine-list")
	public String goToMedicinesManagement(Model model) {

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

		Iterable<Medicine> medicines = medicineService.findAllMedicines();

		prepareModel(model, medicines);

		return "medicine/medicine-list";
	}

	// Medicines list
	@GetMapping("/medicine/medicine-list-error")
	public String goToMedicinesManagementError(Model model) {

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

		Iterable<Medicine> medicines = medicineService.findAllMedicines();

		prepareModel(model, medicines);
		model.addAttribute("duplicateMedicine", true);

		return "medicine/medicine-list";
	}

	// Add medicine
	@PostMapping("/medicine/add-medicine")
	public String addMedicine(@ModelAttribute MedicineForm addMedicineForm, Model model) {
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
			medicineService.addMedicine(userId, addMedicineForm.getName().trim());
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			return "redirect:/medicine/medicine-list-error";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/medicine/medicine-list";
	}

	// Search medicines
	@PostMapping("/medicine/search-medicines")
	public String searchSpecialities(@ModelAttribute MedicineForm searchMedicinesForm, Model model) {
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

		Iterable<Medicine> medicines;

		try {
			medicines = medicineService.findMedicines(userId, searchMedicinesForm.getName(),
					searchMedicinesForm.isEnabled());
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/403";
		}

		prepareModel(model, medicines);

		return "medicine/medicine-list";
	}

	// Change medicine state
	@PostMapping("/medicine/change-medicine-state/{id}")
	public String changeMedicineState(@PathVariable Integer id, Model model) {
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
			medicineService.changeEnablingMedicine(userId, id);
		} catch (InstanceNotFoundException e) {
			try {
				if (!permissionChecker.checkIsAdmin(userId)) {
					return "/error/403";
				} else {
					return "/error/404";
				}
			} catch (InstanceNotFoundException e1) {
				return "/error/403";
			}
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/medicine/medicine-list";
	}

	// Update speciality
	@PostMapping("/medicine/update/{id}")
	public String updateMedicine(@PathVariable Integer id, @ModelAttribute MedicineForm updateMedicineForm,
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
			medicineService.updateMedicine(userId, id, updateMedicineForm.getName());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (DuplicateInstanceException e) {
			return "redirect:/medicine/medicine-list-error";
		} catch (PermissionException e) {
			return "/error/403";
		}
		
		return "redirect:/medicine/medicine-list";
	}

	private void prepareModel(Model model, Iterable<Medicine> medicines) {
		model.addAttribute("medicines", medicines);
		model.addAttribute("addMedicineForm", new MedicineForm());
		model.addAttribute("searchMedicinesForm", new MedicineForm());
		model.addAttribute("updateMedicineForm", new MedicineForm());
	}
}
