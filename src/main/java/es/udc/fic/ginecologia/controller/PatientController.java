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

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.PatientConversor;
import es.udc.fic.ginecologia.form.PatientDetails;
import es.udc.fic.ginecologia.form.SearchPatientsForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.service.PatientService;

@Controller
public class PatientController {

	@Autowired
	PatientService patientService;

	@Autowired
	PermissionChecker permissionChecker;

	// Contraceptives list
	@GetMapping("/patient/patient-list")
	public String goToPatientsManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		Iterable<Patient> patients;

		try {
			patients = patientService.findAllPatients(userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/401";
		}

		prepareModel(model, patients);

		return "patient/patient-list";
	}

	// Find patients
	@PostMapping("/patient/search-patients")
	public String findPatients(@ModelAttribute SearchPatientsForm searchPatientsForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		Iterable<Patient> patients;

		try {
			patients = patientService.findPatients(userId, searchPatientsForm.getName(),
					searchPatientsForm.getDNI_NIF(), searchPatientsForm.getHist_numsergas(),
					searchPatientsForm.isEnabled());
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/401";
		}

		prepareModel(model, patients);

		return "patient/patient-list";
	}

	// Find patient
	@GetMapping("/patient/patient-details/{id}")
	public String findPatient(@PathVariable Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				return "/error/401";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/401";
		}

		PatientDetails patient;

		try {
			patient = new PatientDetails(patientService.findPatient(userId, id));
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/401";
		}

		model.addAttribute("patient", patient);

		return "patient/patient-details";
	}

	private void prepareModel(Model model, Iterable<Patient> patients) {
		model.addAttribute("patients", PatientConversor.createPatientElemList(patients));
		model.addAttribute("searchPatientsForm", new SearchPatientsForm());
	}
}
