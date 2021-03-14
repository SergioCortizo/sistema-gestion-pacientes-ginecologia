package es.udc.fic.ginecologia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
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
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
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
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}
		
		try {
			medicineService.addMedicine(userId, addMedicineForm.getName().trim());
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		} catch (DuplicateInstanceException e) {
			return "redirect:/medicine/medicine-list-error";
		} catch (PermissionException e) {
			return "/error/401";
		}
		
		return "redirect:/medicine/medicine-list";
	}

	private void prepareModel(Model model, Iterable<Medicine> medicines) {
		model.addAttribute("medicines", medicines);
		model.addAttribute("addMedicineForm", new MedicineForm());
	}
}
