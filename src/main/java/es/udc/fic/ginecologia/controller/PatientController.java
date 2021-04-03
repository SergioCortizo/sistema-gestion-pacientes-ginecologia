package es.udc.fic.ginecologia.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fic.ginecologia.common.MeetingByDateDescendingComparator;
import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.MeetingForm;
import es.udc.fic.ginecologia.form.PatientConversor;
import es.udc.fic.ginecologia.form.PatientDetails;
import es.udc.fic.ginecologia.form.PatientForm;
import es.udc.fic.ginecologia.form.SearchPatientsForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.DiagnosticTest;
import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.Question;
import es.udc.fic.ginecologia.service.ContraceptiveService;
import es.udc.fic.ginecologia.service.DiagnosticTestService;
import es.udc.fic.ginecologia.service.PatientService;
import es.udc.fic.ginecologia.service.QuestionService;

@Controller
public class PatientController {

	@Autowired
	PatientService patientService;

	@Autowired
	PermissionChecker permissionChecker;

	@Autowired
	ContraceptiveService contraceptiveService;

	@Autowired
	QuestionService questionService;

	@Autowired
	DiagnosticTestService diagnosticTestService;

	// Patients list
	@GetMapping("/patient/patient-list")
	public String goToPatientsManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Iterable<Patient> patients;

		try {
			patients = patientService.findAllPatients(userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/403";
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
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Iterable<Patient> patients;

		try {
			patients = patientService.findPatients(userId, searchPatientsForm.getName(),
					searchPatientsForm.getDNI_NIF(), searchPatientsForm.getHist_numsergas(),
					searchPatientsForm.isEnabled());
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/403";
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
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		PatientDetails patient;

		try {
			patient = new PatientDetails(patientService.findPatient(userId, id));
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		model.addAttribute("patient", patient);

		return "patient/patient-details";
	}

	// Add patient view
	@GetMapping("/patient/add-patient")
	public String goToAddPatientView(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		model.addAttribute("addPatientForm", new PatientForm());
		model.addAttribute("contraceptives", contraceptiveService.findAllActiveContraceptives());

		return "patient/add-patient";
	}

	// Add patient view error
	@GetMapping("/patient/add-patient-error")
	public String goToAddPatientViewError(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		model.addAttribute("addPatientForm", new PatientForm());
		model.addAttribute("contraceptives", contraceptiveService.findAllActiveContraceptives());
		model.addAttribute("duplicatePatient", true);

		return "patient/add-patient";
	}

	// Add patient
	@PostMapping("/patient/add-patient")
	public String addPatient(@ModelAttribute PatientForm addPatientForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Patient patient = PatientConversor.convertToPatient(addPatientForm);

		try {
			patientService.addPatient(userId, patient, addPatientForm.getContraceptives());
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			return "redirect:/patient/add-patient-error";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/patient/patient-list";
	}

	// Update patient view
	@GetMapping("/patient/update-patient/{id}")
	public String goToUpdatePatientView(@PathVariable Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		PatientForm patientForm = new PatientForm();
		Patient patient;
		Iterable<Question> questions;

		try {
			patient = patientService.findPatient(userId, id);
			patientForm = PatientConversor.convertToPatientForm(patient);
			questions = questionService.findAllQuestions(userId);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		Iterable<DiagnosticTest> diagnosticTests = () -> StreamSupport
				.stream(diagnosticTestService.findAllDiagnosticTests().spliterator(), false)
				.filter(dt -> dt.isEnabled())
				.iterator();

		List<Meeting> meetings = new ArrayList<>(patient.getMeetings());
		Collections.sort(meetings, new MeetingByDateDescendingComparator());

		model.addAttribute("updatePatientForm", patientForm);
		model.addAttribute("patientId", id);
		model.addAttribute("meetings", meetings);
		model.addAttribute("questions", questions);
		model.addAttribute("diagnosticTests", diagnosticTests);
		model.addAttribute("addMeetingForm", new MeetingForm());
		model.addAttribute("contraceptives", contraceptiveService.findAllActiveContraceptives());

		return "patient/update-patient";
	}

	// Update patient view error
	@GetMapping("/patient/update-patient-error/{id}")
	public String goToUpdatePatientViewError(@PathVariable Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		PatientForm patientForm = new PatientForm();
		Patient patient;
		Iterable<Question> questions;

		try {
			patient = patientService.findPatient(userId, id);
			patientForm = PatientConversor.convertToPatientForm(patient);
			questions = questionService.findAllQuestions(userId);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}
		
		Iterable<DiagnosticTest> diagnosticTests = () -> StreamSupport
				.stream(diagnosticTestService.findAllDiagnosticTests().spliterator(), false)
				.filter(dt -> dt.isEnabled())
				.iterator();

		List<Meeting> meetings = new ArrayList<>(patient.getMeetings());
		Collections.sort(meetings, new MeetingByDateDescendingComparator());

		model.addAttribute("updatePatientForm", patientForm);
		model.addAttribute("patientId", id);
		model.addAttribute("meetings", meetings);
		model.addAttribute("questions", questions);
		model.addAttribute("diagnosticTests", diagnosticTests);
		model.addAttribute("addMeetingForm", new MeetingForm());
		model.addAttribute("contraceptives", contraceptiveService.findAllActiveContraceptives());
		model.addAttribute("duplicatePatient", true);

		return "patient/update-patient";
	}

	// update patient
	@PostMapping("/patient/update-patient/{id}")
	public String updatePatient(@PathVariable Long id, @ModelAttribute PatientForm updatePatientForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Patient patient = PatientConversor.convertToPatient(updatePatientForm);

		try {
			patientService.updatePatient(userId, id, patient, updatePatientForm.getContraceptives());
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			return "redirect:/patient/update-patient-error/" + id;
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/patient/patient-list";
	}

	// Change patient state
	@PostMapping("/patient/change-patient-state/{id}")
	public String changePatientState(@PathVariable Long id, @ModelAttribute PatientForm updatePatientForm,
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
			patientService.changePatientEnablingState(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/patient/patient-list";
	}

	private void prepareModel(Model model, Iterable<Patient> patients) {
		model.addAttribute("patients", PatientConversor.createPatientElemList(patients));
		model.addAttribute("searchPatientsForm", new SearchPatientsForm());
	}
}
