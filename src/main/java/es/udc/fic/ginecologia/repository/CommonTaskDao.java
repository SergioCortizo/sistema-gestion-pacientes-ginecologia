package es.udc.fic.ginecologia.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.CommonTask;

public interface CommonTaskDao extends CrudRepository<CommonTask, Integer> {

	@Query("SELECT cm FROM CommonTask cm JOIN cm.commonTaskUsers cmu WHERE cmu.user.id=?1 ORDER BY cmu.last_time_read")
	public Iterable<CommonTask> findByUserId(Integer id);
}
