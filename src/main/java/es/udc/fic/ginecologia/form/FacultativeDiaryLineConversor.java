package es.udc.fic.ginecologia.form;

import es.udc.fic.ginecologia.model.CalendarEntry;

public class FacultativeDiaryLineConversor {

	public static final FacultativeDiaryLine convertToFacultativeDiaryLine(CalendarEntry ce) {

		FacultativeDiaryLine result = new FacultativeDiaryLine();

		result.setId(ce.getId());
		result.setDate(ce.getEntryDate().toLocalDate());
		result.setHour(ce.getEntryDate().toLocalTime());
		result.setState(ce.getState());
		result.setReason(ce.getReason());
		result.setPatient(ce.getPatient().getName());
		result.setUser(ce.getUser().getName());

		return result;
	}
}
