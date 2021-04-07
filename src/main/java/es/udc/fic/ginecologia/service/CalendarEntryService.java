package es.udc.fic.ginecologia.service;

import java.time.LocalDateTime;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.CalendarEntry;

public interface CalendarEntryService {

	Iterable<CalendarEntry> findCalendarEntries(Integer userId) throws InstanceNotFoundException, PermissionException;

	Iterable<CalendarEntry> findAllCalendarEntries(Integer userId)
			throws InstanceNotFoundException, PermissionException;

	void addCalendarEntry(Integer userId, Integer facultativeId, Long patientId, LocalDateTime entryDate, String reason)
			throws InstanceNotFoundException, PermissionException;

	void updateCalendarEntry(Integer userId, Integer calendarEntryId, Integer facultativeId, Long patientId,
			LocalDateTime entryDate, String reason) throws InstanceNotFoundException, PermissionException;

	void cancelEntry(Integer userId, Integer calendarEntryId) throws PermissionException, InstanceNotFoundException;

	void setEntryAsClosed(Integer facultativeId, Integer calendarEntryId)
			throws PermissionException, InstanceNotFoundException;

	CalendarEntry findById(Integer userId, Integer calendarEntryId)
			throws InstanceNotFoundException, PermissionException;

}
