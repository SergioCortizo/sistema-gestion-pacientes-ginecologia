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

import es.udc.fic.ginecologia.common.LoggingUtility;
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
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/diagnostic-test/diagnostic-test-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/diagnostic-test/diagnostic-test-list");
			return "/error/403";
		}

		Iterable<DiagnosticTest> diagnosticTests = diagnosticTestService.findAllDiagnosticTests();

		prepareModel(model, diagnosticTests);

		LoggingUtility.logGetResource(username, "GET", "/diagnostic-test/diagnostic-test-list");

		return "diagnostic-test/diagnostic-test-list";
	}

	// Diagnostic tests list
	@GetMapping("/diagnostic-test/diagnostic-test-list-error")
	public String goToDiagnosticTestsManagementError(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/diagnostic-test/diagnostic-test-list-error");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/diagnostic-test/diagnostic-test-list-error");
			return "/error/403";
		}

		Iterable<DiagnosticTest> diagnosticTests = diagnosticTestService.findAllDiagnosticTests();

		prepareModel(model, diagnosticTests);
		model.addAttribute("duplicateDiagnosticTest", true);

		LoggingUtility.logGetResource(username, "GET", "/diagnostic-test/diagnostic-test-list-error");

		return "diagnostic-test/diagnostic-test-list";
	}

	// Add diagnostic test
	@PostMapping("/diagnostic-test/add-diagnostic-test")
	public String addDiagnosticTest(@ModelAttribute DiagnosticTestForm addDiagnosticTestForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/add-diagnostic-test");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/add-diagnostic-test");
			return "/error/403";
		}

		try {
			diagnosticTestService.addDiagnosticTest(userId, addDiagnosticTestForm.getName());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/add-diagnostic-test");
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicateDiagnosticTest(username, addDiagnosticTestForm);
			return "redirect:/diagnostic-test/diagnostic-test-list-error";
		}

		LoggingUtility.logAddedDiagnosticTest(username, addDiagnosticTestForm);

		return "redirect:/diagnostic-test/diagnostic-test-list";
	}

	// Update diagnostic test
	@PostMapping("/diagnostic-test/update/{id}")
	public String updateDiagnosticTest(@PathVariable Integer id,
			@ModelAttribute DiagnosticTestForm updateDiagnosticTestForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/update/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/update/" + id);
			return "/error/403";
		}

		try {
			diagnosticTestService.updateDiagnosticTest(userId, id, updateDiagnosticTestForm.getName());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, DiagnosticTest.class.getSimpleName(), id, "POST",
					"/diagnostic-test/update/" + id);
			return "/error/404";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicateDiagnosticTest(username, updateDiagnosticTestForm);
			return "redirect:/diagnostic-test/diagnostic-test-list-error";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/update/" + id);
			return "/error/403";
		}

		LoggingUtility.logUpdatedDiagnosticTest(username, id, updateDiagnosticTestForm);

		return "redirect:/diagnostic-test/diagnostic-test-list";
	}

	// Change diagnostic test state
	@PostMapping("/diagnostic-test/change-diagnostic-test-state/{id}")
	public String changeDiagnosticTestState(@PathVariable Integer id, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/change-diagnostic-test-state/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/change-diagnostic-test-state/" + id);
			return "/error/403";
		}

		try {
			diagnosticTestService.changeEnablingDiagnosticTest(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, DiagnosticTest.class.getSimpleName(), id, "POST",
					"/diagnostic-test/change-diagnostic-test-state/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/change-diagnostic-test-state/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangeEnablingState(username, DiagnosticTest.class.getSimpleName(), id);

		return "redirect:/diagnostic-test/diagnostic-test-list";
	}

	// Update diagnostic test
	@PostMapping("/diagnostic-test/search-diagnostic-tests")
	public String searchDiagnosticTests(@ModelAttribute DiagnosticTestForm searchDiagnosticTestsForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/search-diagnostic-tests");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/search-diagnostic-tests");
			return "/error/403";
		}

		Iterable<DiagnosticTest> diagnosticTests;

		try {
			diagnosticTests = diagnosticTestService.findDiagnosticTests(userId, searchDiagnosticTestsForm.getName(),
					searchDiagnosticTestsForm.isEnabled());
		} catch (PermissionException | InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/diagnostic-test/search-diagnostic-tests");
			return "/error/403";
		}

		prepareModel(model, diagnosticTests);
		
		LoggingUtility.logSearchDiagnosticTests(username, searchDiagnosticTestsForm, diagnosticTests);

		return "diagnostic-test/diagnostic-test-list";
	}

	private void prepareModel(Model model, Iterable<DiagnosticTest> diagnosticTests) {
		model.addAttribute("diagnosticTests", diagnosticTests);
		model.addAttribute("addDiagnosticTestForm", new DiagnosticTestForm());
		model.addAttribute("searchDiagnosticTestsForm", new DiagnosticTestForm());
		model.addAttribute("updateDiagnosticTestForm", new DiagnosticTestForm());
	}
}
