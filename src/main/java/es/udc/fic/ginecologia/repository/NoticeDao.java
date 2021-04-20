package es.udc.fic.ginecologia.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Notice;

public interface NoticeDao extends CrudRepository<Notice, Integer> {

	@Query("SELECT count(n) FROM Notice n WHERE CONVERT(datetime, DATETIME) > ?1")
	public long countNewNotices(LocalDateTime datetime);
	
	public Optional<Notice> findByNotice(String notice);
}
