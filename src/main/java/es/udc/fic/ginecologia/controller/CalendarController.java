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

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.form.CalendarEntryForm;
import es.udc.fic.ginecologia.form.FacultativeDiaryLine;
import es.udc.fic.ginecologia.form.FacultativeDiaryLineConversor;
import es.udc.fic.ginecologia.model.CalendarEntry;
import es.udc.fic.ginecologia.model.CustomUserDetails;
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

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		Iterable<CalendarEntry> calendarEntries;

		try {
			calendarEntries = calendarService.findCalendarEntries(userId);
			prepareModel(model, calendarEntries, userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/403";
		}

		return "calendar/check-agenda";
	}

	// Calendar management
	@GetMapping("/calendar/meetings-management")
	public String goToCalendarManagement(Model model) {

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

		Iterable<CalendarEntry> calendarEntries;

		try {
			calendarEntries = calendarService.findAllCalendarEntries(userId);
			prepareModel(model, calendarEntries, userId);
		} catch (InstanceNotFoundException | PermissionException e) {
			return "/error/403";
		}

		return "calendar/meetings-management";
	}

	// Go to update meeting
	@GetMapping("/calendar/update-meeting/{id}")
	public String goToCalendarManagement(@PathVariable Integer id, Model model) {

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
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		model.addAttribute("updateCalendarEntryForm", updateCalendarEntryForm);
		model.addAttribute("lineId", id);
		model.addAttribute("facultatives", userService.findAllFacultatives());

		return "calendar/update-meeting";
	}

	// Add calendar entry
	@PostMapping("/calendar/add-calendar-entry")
	public String addCalendarEntry(@ModelAttribute CalendarEntryForm addCalendarEntryForm, Model model) {

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

		LocalDateTime date = LocalDateTime.of(addCalendarEntryForm.getDateEntry(), addCalendarEntryForm.getHourEntry());

		try {
			calendarService.addCalendarEntry(userId, addCalendarEntryForm.getFacultative(),
					addCalendarEntryForm.getPatient(), date, addCalendarEntryForm.getReason());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/calendar/meetings-management";
	}

	// Update calendar entry
	@PostMapping("/calendar/update-calendar-entry/{id}")
	public String updateCalendarEntry(@PathVariable Integer id,
			@ModelAttribute CalendarEntryForm updateCalendarEntryForm, Model model) {

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

		LocalDateTime date = LocalDateTime.of(updateCalendarEntryForm.getDateEntry(),
				updateCalendarEntryForm.getHourEntry());

		try {
			calendarService.updateCalendarEntry(userId, id, updateCalendarEntryForm.getFacultative(),
					updateCalendarEntryForm.getPatient(), date, updateCalendarEntryForm.getReason());
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/calendar/meetings-management";
	}

	// Cancel meeting
	@PostMapping("/calendar/cancel-meeting/{id}")
	public String addCalendarEntry(@PathVariable Integer id, Model model) {

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
			calendarService.cancelEntry(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/calendar/meetings-management";
	}

	// Set entry as closed
	@PostMapping("/calendar/set-entry-as-closed/{id}")
	public String setEntryAsClosed(@PathVariable Integer id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		try {
			if (!permissionChecker.checkIsFacultative(userId)) {
				return "/error/403";
			}
		} catch (InstanceNotFoundException e) {
			return "/error/403";
		}

		try {
			calendarService.setEntryAsClosed(userId, id);
		} catch (InstanceNotFoundException e) {
			return "/error/404";
		} catch (PermissionException e) {
			return "/error/403";
		}

		return "redirect:/calendar/check-agenda";
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

		model.addAttribute("today", LocalDate.now());
		model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
		model.addAttribute("addCalendarEntryForm", new CalendarEntryForm());
		model.addAttribute("facultatives", userService.findAllFacultatives());
		model.addAttribute("patients", patientService.findAllPatients(userId));

	}
}
