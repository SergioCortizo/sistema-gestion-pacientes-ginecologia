package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.LogLine;

public interface LogLineDao extends CrudRepository<LogLine, Integer> {

	public Iterable<LogLine> findAllByOrderByTimestampDesc();
}
