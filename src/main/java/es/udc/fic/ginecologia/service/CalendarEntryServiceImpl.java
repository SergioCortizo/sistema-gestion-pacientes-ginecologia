package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.common.security.PermissionChecker;
import es.udc.fic.ginecologia.model.CalendarEntry;
import es.udc.fic.ginecologia.model.MeetingState;
import es.udc.fic.ginecologia.model.Patient;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.repository.CalendarEntryDao;
import es.udc.fic.ginecologia.repository.PatientDao;

@Transactional
@Service
public class CalendarEntryServiceImpl implements CalendarEntryService {

	@Autowired
	private PermissionChecker permissionChecker;

	@Autowired
	private CalendarEntryDao calendarEntryDao;

	@Autowired
	private PatientDao patientDao;

	@Override
	public Iterable<CalendarEntry> findCalendarEntries(Integer userId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId)) {
			throw new PermissionException();
		}

		return calendarEntryDao.findByUserIdOrderByEntryDateAsc(userId);
	}

	@Override
	public Iterable<CalendarEntry> findAllCalendarEntries(Integer userId)
			throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		return calendarEntryDao.findAllByOrderByEntryDateAsc();
	}

	@Override
	public CalendarEntry findById(Integer userId, Integer calendarEntryId)
			throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<CalendarEntry> foundCalendarEntry = calendarEntryDao.findById(calendarEntryId);

		if (!foundCalendarEntry.isPresent()) {
			throw new InstanceNotFoundException("entities.calendarEntry", calendarEntryId);
		}

		return foundCalendarEntry.get();
	}

	@Override
	public void addCalendarEntry(Integer userId, Integer facultativeId, Long patientId, LocalDateTime entryDate,
			String reason) throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsAdmin(userId) || !permissionChecker.checkIsFacultative(facultativeId)) {
			throw new PermissionException();
		}

		User facultative = permissionChecker.checkUser(facultativeId);

		Optional<Patient> patientFound = patientDao.findById(patientId);

		if (!patientFound.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		CalendarEntry calendarEntry = new CalendarEntry(entryDate, reason, facultative, patientFound.get());

		calendarEntryDao.save(calendarEntry);
	}

	@Override
	public void updateCalendarEntry(Integer userId, Integer calendarEntryId, Integer facultativeId, Long patientId,
			LocalDateTime entryDate, String reason) throws InstanceNotFoundException, PermissionException {

		if (!permissionChecker.checkIsAdmin(userId) || !permissionChecker.checkIsFacultative(facultativeId)) {
			throw new PermissionException();
		}

		User facultative = permissionChecker.checkUser(facultativeId);

		Optional<Patient> patientFound = patientDao.findById(patientId);

		if (!patientFound.isPresent()) {
			throw new InstanceNotFoundException("entities.patient", patientId);
		}

		Patient patient = patientFound.get();

		Optional<CalendarEntry> calendarEntryFound = calendarEntryDao.findById(calendarEntryId);

		if (!calendarEntryFound.isPresent()) {
			throw new InstanceNotFoundException("entities.calendarEntry", calendarEntryId);
		}

		CalendarEntry calendarEntry = calendarEntryFound.get();

		calendarEntry.setEntryDate(entryDate);
		calendarEntry.setUser(facultative);
		calendarEntry.setPatient(patient);
		calendarEntry.setReason(reason);

	}

	@Override
	public void cancelEntry(Integer userId, Integer calendarEntryId)
			throws PermissionException, InstanceNotFoundException {

		if (!permissionChecker.checkIsAdmin(userId)) {
			throw new PermissionException();
		}

		Optional<CalendarEntry> calendarEntryFound = calendarEntryDao.findById(calendarEntryId);

		if (!calendarEntryFound.isPresent()) {
			throw new InstanceNotFoundException("entities.calendarEntry", calendarEntryId);
		}

		CalendarEntry calendarEntry = calendarEntryFound.get();

		calendarEntry.setState(MeetingState.cancelled);
	}

	@Override
	public void setEntryAsClosed(Integer facultativeId, Integer calendarEntryId)
			throws PermissionException, InstanceNotFoundException {

		if (!permissionChecker.checkIsFacultative(facultativeId)) {
			throw new PermissionException();
		}

		Optional<CalendarEntry> calendarEntryFound = calendarEntryDao.findById(calendarEntryId);

		if (!calendarEntryFound.isPresent()) {
			throw new InstanceNotFoundException("entities.calendarEntry", calendarEntryId);
		}

		CalendarEntry calendarEntry = calendarEntryFound.get();

		if (calendarEntry.getUser().getId() != facultativeId) {
			throw new PermissionException();
		}

		calendarEntry.setState(MeetingState.closed);
	}

	@Override
	public long countMeetingsForToday(Integer userId) throws InstanceNotFoundException, PermissionException {
		if (!permissionChecker.checkIsFacultative(userId)) {
			throw new PermissionException();
		}
		
		return calendarEntryDao.countMeetingsForToday(userId);
	}

}
