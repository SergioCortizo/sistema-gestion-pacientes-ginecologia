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
import es.udc.fic.ginecologia.form.DiagnosticTestForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.DiagnosticTest;
import es.udc.fic.ginecologia.service.DiagnosticTestService;

@Controller
public class DiagnosticTestController {

	@Autowired
	DiagnosticTestService diagnosticTestService;

	@Autowired
	PermissionChecker permissionChecker;

	// Diagnostic tests list
	@GetMapping("/diagnostic-test/diagnostic-test-list")
	public String goToDiagnosticTestsManagement(Model model) {

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

		Iterable<DiagnosticTest> diagnosticTests = diagnosticTestService.findAllDiagnosticTests();

		prepareModel(model, diagnosticTests);

		return "diagnostic-test/diagnostic-test-list";
	}

	// Diagnostic tests list
	@GetMapping("/diagnostic-test/diagnostic-test-list-error")
	public String goToDiagnosticTestsManagementError(Model model) {

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

		Iterable<DiagnosticTest> diagnosticTests = diagnosticTestService.findAllDiagnosticTests();

		prepareModel(model, diagnosticTests);
		model.addAttribute("duplicateDiagnosticTest", true);

		return "diagnostic-test/diagnostic-test-list";
	}

	// Add diagnostic test
	@PostMapping("/diagnostic-test/add-diagnostic-test")
	public String addDiagnosticTest(@ModelAttribute DiagnosticTestForm addDiagnosticTestForm, Model model) {
		
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
			diagnosticTestService.addDiagnosticTest(userId, addDiagnosticTestForm.getName());
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		} catch (DuplicateInstanceException e) {
			return "redirect:/diagnostic-test/diagnostic-test-list-error";
		} catch (PermissionException e) {
			return "/error/401";
		}
		
		return "redirect:/diagnostic-test/diagnostic-test-list";
	}

	private void prepareModel(Model model, Iterable<DiagnosticTest> diagnosticTests) {
		model.addAttribute("diagnosticTests", diagnosticTests);
		model.addAttribute("addDiagnosticTestForm", new DiagnosticTestForm());
	}
}
