package es.udc.fic.ginecologia.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.LogLevel;
import es.udc.fic.ginecologia.model.LogLine;

public interface LogLineDao extends CrudRepository<LogLine, Integer> {

	public Iterable<LogLine> findAllByOrderByTimestampDesc();

	@Query("SELECT l FROM LogLine l WHERE l.level=?1 AND CONVERT(l.timestamp, DATETIME) BETWEEN ?2 AND ?3 ORDER BY l.timestamp DESC")
	public Iterable<LogLine> findByLevelAndTimestampOrderByTimestampDesc(LogLevel level, LocalDateTime date1,
			LocalDateTime date2);

	@Query("SELECT l FROM LogLine l WHERE CONVERT(l.timestamp, DATETIME) BETWEEN ?1 AND ?2 ORDER BY l.timestamp DESC")
	public Iterable<LogLine> findByTimestampRangeOrderByTimestampDesc(LocalDateTime date1, LocalDateTime date2);

}
