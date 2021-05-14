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

import es.udc.fic.ginecologia.common.LoggingUtility;
import es.udc.fic.ginecologia.common.MeetingByDateDescendingComparator;
import es.udc.fic.ginecologia.common.exception.DuplicateInstanceException;
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.MeetingForm;
import es.udc.fic.ginecologia.form.MessageForm;
import es.udc.fic.ginecologia.form.PatientConversor;
import es.udc.fic.ginecologia.form.PatientDetails;
import es.udc.fic.ginecologia.form.PatientForm;
import es.udc.fic.ginecologia.form.SearchPatientsForm;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.DiagnosticTest;
import es.udc.fic.ginecologia.model.Medicine;
import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.Question;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.service.ContraceptiveService;
import es.udc.fic.ginecologia.service.DiagnosticTestService;
import es.udc.fic.ginecologia.service.MedicineService;
import es.udc.fic.ginecologia.service.PatientService;
import es.udc.fic.ginecologia.service.QuestionService;
import es.udc.fic.ginecologia.service.UserService;

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

	@Autowired
	MedicineService medicineService;

	@Autowired
	UserService userService;

	// Patients list
	@GetMapping("/patient/patient-list")
	public String goToPatientsManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/patient-list");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/patient-list");
			return "/error/403";
		}

		Iterable<Patient> patients;

		try {
			patients = patientService.findAllPatients(userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/patient-list");
			return "/error/403";
		}

		prepareModel(model, patients, userId);

		LoggingUtility.logGetResource(username, "GET", "/patient/patient-list");

		return "patient/patient-list";
	}

	// Find patients
	@PostMapping("/patient/search-patients")
	public String findPatients(@ModelAttribute SearchPatientsForm searchPatientsForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/patient/search-patients");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/search-patients");
			return "/error/403";
		}

		Iterable<Patient> patients;

		try {
			patients = patientService.findPatients(userId, searchPatientsForm.getName(),
					searchPatientsForm.getDNI_NIF(), searchPatientsForm.getHist_numsergas(),
					searchPatientsForm.isEnabled());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/search-patients");
			return "/error/403";
		}

		prepareModel(model, patients, userId);

		LoggingUtility.logSearchPatients(username, searchPatientsForm, patients);

		return "patient/patient-list";
	}

	// Find patient
	@GetMapping("/patient/patient-details/{id}")
	public String findPatient(@PathVariable Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/patient-details/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/patient-details/" + id);
			return "/error/403";
		}

		PatientDetails patient;

		try {
			patient = new PatientDetails(patientService.findPatient(userId, id));
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Patient.class.getSimpleName(), id, "GET",
					"/patient/patient-details/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/patient-details/" + id);
			return "/error/403";
		}

		model.addAttribute("patient", patient);
		model.addAttribute("messageForm", new MessageForm());
		model.addAttribute("facultatives", userService.findAllFacultatives());

		LoggingUtility.logGetResource(username, "GET", "/patient/patient-details/" + id);

		return "patient/patient-details";
	}

	// Add patient view
	@GetMapping("/patient/add-patient")
	public String goToAddPatientView(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/add-patient");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/add-patient");
			return "/error/403";
		}

		model.addAttribute("addPatientForm", new PatientForm());
		model.addAttribute("contraceptives", contraceptiveService.findAllActiveContraceptives());

		LoggingUtility.logGetResource(username, "GET", "/patient/add-patient");

		return "patient/add-patient";
	}

	// Add patient view error
	@GetMapping("/patient/add-patient-error")
	public String goToAddPatientViewError(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/add-patient-error");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/add-patient-error");
			return "/error/403";
		}

		model.addAttribute("addPatientForm", new PatientForm());
		model.addAttribute("contraceptives", contraceptiveService.findAllActiveContraceptives());
		model.addAttribute("duplicatePatient", true);

		LoggingUtility.logGetResource(username, "GET", "/patient/add-patient-error");

		return "patient/add-patient";
	}

	// Add patient
	@PostMapping("/patient/add-patient")
	public String addPatient(@ModelAttribute PatientForm addPatientForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/patient/add-patient");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/add-patient");
			return "/error/403";
		}

		Patient patient = PatientConversor.convertToPatient(addPatientForm);

		try {
			patientService.addPatient(userId, patient, addPatientForm.getContraceptives());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/add-patient");
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicatePatient(username, addPatientForm);
			return "redirect:/patient/add-patient-error";
		}

		LoggingUtility.logAddedPatient(username, addPatientForm);

		return "redirect:/patient/patient-list";
	}

	// Update patient view
	@GetMapping("/patient/update-patient/{id}")
	public String goToUpdatePatientView(@PathVariable Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/update-patient/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/update-patient/" + id);
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
			LoggingUtility.logInstanceNotFound(username, Patient.class.getSimpleName(), id, "GET",
					"/patient/update-patient/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/update-patient/" + id);
			return "/error/403";
		}

		Iterable<DiagnosticTest> diagnosticTests = () -> StreamSupport
				.stream(diagnosticTestService.findAllDiagnosticTests().spliterator(), false)
				.filter(dt -> dt.isEnabled()).iterator();

		List<Meeting> meetings = new ArrayList<>(patient.getMeetings());
		Collections.sort(meetings, new MeetingByDateDescendingComparator());

		Iterable<Medicine> medicines = () -> StreamSupport
				.stream(medicineService.findAllMedicines().spliterator(), false).filter(m -> m.isEnabled()).iterator();

		model.addAttribute("updatePatientForm", patientForm);
		model.addAttribute("patientId", id);
		model.addAttribute("meetings", meetings);
		model.addAttribute("questions", questions);
		model.addAttribute("diagnosticTests", diagnosticTests);
		model.addAttribute("medicines", medicines);
		model.addAttribute("addMeetingForm", new MeetingForm());
		model.addAttribute("messageForm", new MessageForm());
		model.addAttribute("facultatives", userService.findAllFacultatives());

		model.addAttribute("contraceptives", contraceptiveService.findAllActiveContraceptives());

		LoggingUtility.logGetResource(username, "GET", "/patient/update-patient/" + id);

		return "patient/update-patient";
	}

	// Update patient view error
	@GetMapping("/patient/update-patient-error/{id}")
	public String goToUpdatePatientViewError(@PathVariable Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/update-patient-error/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/update-patient-error/" + id);
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
			LoggingUtility.logInstanceNotFound(username, Patient.class.getSimpleName(), id, "GET",
					"/patient/update-patient-error/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/update-patient-error/" + id);
			return "/error/403";
		}

		Iterable<DiagnosticTest> diagnosticTests = () -> StreamSupport
				.stream(diagnosticTestService.findAllDiagnosticTests().spliterator(), false)
				.filter(dt -> dt.isEnabled()).iterator();

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

		LoggingUtility.logGetResource(username, "GET", "/patient/update-patient-error/" + id);

		return "patient/update-patient";
	}

	// update patient
	@PostMapping("/patient/update-patient/{id}")
	public String updatePatient(@PathVariable Long id, @ModelAttribute PatientForm updatePatientForm, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/patient/update-patient/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/update-patient/" + id);
			return "/error/403";
		}

		Patient patient = PatientConversor.convertToPatient(updatePatientForm);

		try {
			patientService.updatePatient(userId, id, patient, updatePatientForm.getContraceptives());
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/update-patient/" + id);
			return "/error/403";
		} catch (DuplicateInstanceException e) {
			LoggingUtility.logDuplicatePatient(username, updatePatientForm);
			return "redirect:/patient/update-patient-error/" + id;
		}

		LoggingUtility.logUpdatePatient(username, id, updatePatientForm);

		return "redirect:/patient/patient-list";
	}

	// Change patient state
	@PostMapping("/patient/change-patient-state/{id}")
	public String changePatientState(@PathVariable Long id, @ModelAttribute PatientForm updatePatientForm,
			Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/patient/change-patient-state/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/change-patient-state/" + id);
			return "/error/403";
		}

		try {
			patientService.changePatientEnablingState(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Patient.class.getSimpleName(), id, "POST",
					"/patient/change-patient-state/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/change-patient-state/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangeEnablingPatientState(username, Patient.class.getSimpleName(), id);

		return "redirect:/patient/patient-list";
	}

	// Set/unset patient as patient of interest
	@PostMapping("/patient/change-patient-of-interest/{id}")
	public String changePatientAsatientOfInterest(@PathVariable Long id, @ModelAttribute PatientForm updatePatientForm,
			Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/patient/change-patient-of-interest/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/change-patient-of-interest/" + id);
			return "/error/403";
		}

		try {
			patientService.changeAsPatientOfInterest(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, Patient.class.getSimpleName(), id, "POST",
					"/patient/change-patient-of-interest/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/patient/change-patient-of-interest/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logChangePatientOfInterest(username, id);

		return "redirect:/patient/patient-list";
	}

	// Patients of interest
	@GetMapping("/patient/patients-of-interest")
	public String goToPatientsOfInterest(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		User user;

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/patients-of-interest");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/patients-of-interest");
			return "/error/403";
		}

		try {
			user = permissionChecker.checkUser(userId);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/patients-of-interest");
			return "/error/403";
		}

		prepareModel(model, user.getPatientsOfInterest(), userId);
		
		LoggingUtility.logGetResource(username, "GET", "/patient/patients-of-interest");

		return "patient/patients-of-interest";
	}

	// Last seen patients
	@GetMapping("/patient/last-seen-patients")
	public String goToLastSeenPatients(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/patient/last-seen-patients");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/last-seen-patients");
			return "/error/403";
		}

		Iterable<Meeting> meetings;

		try {
			meetings = patientService.findLastSeenPatients(userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/patient/last-seen-patients");
			return "/error/403";
		}

		prepareModelForLastSeenPatients(model, meetings);
		
		LoggingUtility.logGetResource(username, "GET", "/patient/last-seen-patients");

		return "patient/last-seen-patients";
	}

	private void prepareModel(Model model, Iterable<Patient> patients, Integer userId) {
		model.addAttribute("patients", PatientConversor.createPatientElemList(patients, userId));
		model.addAttribute("searchPatientsForm", new SearchPatientsForm());
	}
	
	private void prepareModelForLastSeenPatients(Model model, Iterable<Meeting> meetings) {
		model.addAttribute("patients", PatientConversor.createPatientElemListForLastSeenPatients(meetings));
		model.addAttribute("searchPatientsForm", new SearchPatientsForm());
	}
}
