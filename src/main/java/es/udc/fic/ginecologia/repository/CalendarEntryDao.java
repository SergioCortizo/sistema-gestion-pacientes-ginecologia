package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.CalendarEntry;

public interface CalendarEntryDao extends CrudRepository<CalendarEntry, Integer>{

	public Iterable<CalendarEntry> findByUserIdOrderByEntryDateAsc(Integer id);
	
	public Iterable<CalendarEntry> findAllByOrderByEntryDateAsc();
}
