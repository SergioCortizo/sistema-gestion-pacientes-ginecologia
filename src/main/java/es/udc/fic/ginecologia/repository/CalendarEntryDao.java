package es.udc.fic.ginecologia.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.CalendarEntry;

public interface CalendarEntryDao extends CrudRepository<CalendarEntry, Integer>{

	public Iterable<CalendarEntry> findByUserIdOrderByEntryDateAsc(Integer id);
	
	public Iterable<CalendarEntry> findAllByOrderByEntryDateAsc();
	
	@Query("SELECT count(ce) FROM CalendarEntry ce WHERE ce.user.id=?1 AND ce.state='opened' AND DATE(entryDate) = CURRENT_DATE()")
	public long countMeetingsForToday(Integer userId);
}
