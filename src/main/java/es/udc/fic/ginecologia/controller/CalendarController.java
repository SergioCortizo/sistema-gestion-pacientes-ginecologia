package es.udc.fic.ginecologia.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.CalendarEntryForm;
import es.udc.fic.ginecologia.form.FacultativeDiaryLine;
import es.udc.fic.ginecologia.form.FacultativeDiaryLineConversor;
import es.udc.fic.ginecologia.model.CalendarEntry;
import es.udc.fic.ginecologia.model.CustomUserDetails;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.service.CalendarEntryService;
import es.udc.fic.ginecologia.service.PatientService;
import es.udc.fic.ginecologia.service.UserService;

@Controller
public class CalendarController {

	@Autowired
	PermissionChecker permissionChecker;

	@Autowired
	CalendarEntryService calendarService;

	@Autowired
	UserService userService;

	@Autowired
	PatientService patientService;

	// Facultative calendar
	@GetMapping("/calendar/check-agenda")
	public String goToFacultativeCalendar(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/calendar/check-agenda");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/calendar/check-agenda");
			return "/error/403";
		}

		Iterable<CalendarEntry> calendarEntries;

		try {
			calendarEntries = calendarService.findCalendarEntries(userId);
			prepareModel(model, calendarEntries, userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/calendar/check-agenda");
			return "/error/403";
		}

		LoggingUtility.logGetResource(username, "GET", "/calendar/check-agenda");

		return "calendar/check-agenda";
	}

	// Calendar management
	@GetMapping("/calendar/meetings-management")
	public String goToCalendarManagement(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/calendar/meetings-management");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/calendar/meetings-management");
			return "/error/403";
		}

		Iterable<CalendarEntry> calendarEntries;

		try {
			calendarEntries = calendarService.findAllCalendarEntries(userId);
			prepareModel(model, calendarEntries, userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/calendar/meetings-management");
			return "/error/403";
		}

		LoggingUtility.logGetResource(username, "GET", "/calendar/meetings-management");

		return "calendar/meetings-management";
	}

	// Go to update meeting
	@GetMapping("/calendar/update-meeting/{id}")
	public String goToCalendarManagement(@PathVariable Integer id, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "GET", "/calendar/update-meeting/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/calendar/update-meeting/" + id);
			return "/error/403";
		}

		CalendarEntryForm updateCalendarEntryForm = new CalendarEntryForm();

		try {
			CalendarEntry calendarEntry = calendarService.findById(userId, id);

			updateCalendarEntryForm.setFacultative(calendarEntry.getUser().getId());
			updateCalendarEntryForm.setPatient(calendarEntry.getPatient().getId());
			updateCalendarEntryForm.setReason(calendarEntry.getReason());
			updateCalendarEntryForm.setDateEntry(calendarEntry.getEntryDate().toLocalDate());
			updateCalendarEntryForm.setHourEntry(calendarEntry.getEntryDate().toLocalTime());

			model.addAttribute("patients", patientService.findAllPatients(userId));

		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, CalendarEntry.class.getName(), id, "GET",
					"/calendar/update-meeting/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "GET", "/calendar/update-meeting/" + id);
			return "/error/403";
		}

		model.addAttribute("updateCalendarEntryForm", updateCalendarEntryForm);
		model.addAttribute("lineId", id);
		model.addAttribute("facultatives", userService.findAllFacultatives());
		model.addAttribute("actualDate", LocalDate.now());

		LoggingUtility.updatedCalendarEntry(username, id, updateCalendarEntryForm);

		return "calendar/update-meeting";
	}

	// Add calendar entry
	@PostMapping("/calendar/add-calendar-entry")
	public String addCalendarEntry(@ModelAttribute CalendarEntryForm addCalendarEntryForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/calendar/add-calendar-entry");
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/add-calendar-entry");
			return "/error/403";
		}

		LocalDateTime date = LocalDateTime.of(addCalendarEntryForm.getDateEntry(), addCalendarEntryForm.getHourEntry());

		try {
			calendarService.addCalendarEntry(userId, addCalendarEntryForm.getFacultative(),
					addCalendarEntryForm.getPatient(), date, addCalendarEntryForm.getReason());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, "POST", "/calendar/add-calendar-entry");
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/add-calendar-entry");
			return "/error/403";
		}

		LoggingUtility.logAddedCalendarEntry(username, addCalendarEntryForm);

		return "redirect:/calendar/meetings-management";
	}

	// Update calendar entry
	@PostMapping("/calendar/update-calendar-entry/{id}")
	public String updateCalendarEntry(@PathVariable Integer id,
			@ModelAttribute CalendarEntryForm updateCalendarEntryForm, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/calendar/update-calendar-entry/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/update-calendar-entry/" + id);
			return "/error/403";
		}

		LocalDateTime date = LocalDateTime.of(updateCalendarEntryForm.getDateEntry(),
				updateCalendarEntryForm.getHourEntry());

		try {
			calendarService.updateCalendarEntry(userId, id, updateCalendarEntryForm.getFacultative(),
					updateCalendarEntryForm.getPatient(), date, updateCalendarEntryForm.getReason());
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, "POST", "/calendar/update-calendar-entry/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/update-calendar-entry/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logUpdatedCalendarEntry(username, id, updateCalendarEntryForm);

		return "redirect:/calendar/meetings-management";
	}

	// Cancel meeting
	@PostMapping("/calendar/cancel-meeting/{id}")
	public String addCalendarEntry(@PathVariable Integer id, Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsAdmin(userId) && !permissionChecker.checkIsCitations(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/calendar/cancel-meeting/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/cancel-meeting/" + id);
			return "/error/403";
		}

		try {
			calendarService.cancelEntry(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, CalendarEntry.class.getName(), id, "POST",
					"/calendar/cancel-meeting/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/cancel-meeting/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logCancelledMeeting(username, id);

		return "redirect:/calendar/meetings-management";
	}

	// Set entry as closed
	@PostMapping("/calendar/set-entry-as-closed/{id}")
	public String setEntryAsClosed(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();
		String username = userDetails.getUsername();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				LoggingUtility.logDeniedAccess(username, "POST", "/calendar/set-entry-as-closed/" + id);
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/set-entry-as-closed/" + id);
			return "/error/403";
		}
		
		CalendarEntry cEntry = null;

		try {
			calendarService.setEntryAsClosed(userId, id);
			cEntry = calendarService.findById(userId, id);
		} catch (InstanceNotFoundException e) {
			LoggingUtility.logInstanceNotFound(username, CalendarEntry.class.getName(), id, "POST", "/calendar/set-entry-as-closed/" + id);
			return "/error/404";
		} catch (PermissionException e) {
			LoggingUtility.logDeniedAccess(username, "POST", "/calendar/set-entry-as-closed/" + id);
			return "/error/403";
		}
		
		LoggingUtility.logSetEntryAsClosed(username, id);

		return "redirect:/patient/update-patient/" + cEntry.getPatient().getId();
	}

	private void prepareModel(Model model, Iterable<CalendarEntry> calendarEntries, Integer userId)
			throws InstanceNotFoundException, PermissionException {

		List<FacultativeDiaryLine> todayLines = StreamSupport.stream(calendarEntries.spliterator(), false)
				.filter(ce -> ce.getEntryDate().toLocalDate().equals(LocalDate.now()))
				.map(ce -> FacultativeDiaryLineConversor.convertToFacultativeDiaryLine(ce))
				.collect(Collectors.toList());

		model.addAttribute("todayLines", todayLines);

		List<FacultativeDiaryLine> tomorrowLines = StreamSupport.stream(calendarEntries.spliterator(), false)
				.filter(ce -> ce.getEntryDate().toLocalDate().equals(LocalDate.now().plusDays(1)))
				.map(ce -> FacultativeDiaryLineConversor.convertToFacultativeDiaryLine(ce))
				.collect(Collectors.toList());

		model.addAttribute("tomorrowLines", tomorrowLines);

		List<FacultativeDiaryLine> nextDays = StreamSupport.stream(calendarEntries.spliterator(), false)
				.filter(ce -> ce.getEntryDate().toLocalDate().isAfter(LocalDate.now().plusDays(1)))
				.map(ce -> FacultativeDiaryLineConversor.convertToFacultativeDiaryLine(ce))
				.collect(Collectors.toList());

		model.addAttribute("nextDays", nextDays);

		List<FacultativeDiaryLine> previousDays = StreamSupport.stream(calendarEntries.spliterator(), false)
				.filter(ce -> ce.getEntryDate().toLocalDate().isBefore(LocalDate.now()))
				.map(ce -> FacultativeDiaryLineConversor.convertToFacultativeDiaryLine(ce))
				.collect(Collectors.toList());

		model.addAttribute("previousDays", previousDays);
		
		Iterable<User> facultatives = userService.findAllFacultatives();

		model.addAttribute("today", LocalDate.now());
		model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
		model.addAttribute("addCalendarEntryForm", new CalendarEntryForm());
		model.addAttribute("facultatives", facultatives);
		model.addAttribute("actualDate", LocalDate.now());
		model.addAttribute("patients", patientService.findAllPatients(userId));

	}
}
