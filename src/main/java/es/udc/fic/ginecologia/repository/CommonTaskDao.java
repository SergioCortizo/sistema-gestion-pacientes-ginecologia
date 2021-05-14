package es.udc.fic.ginecologia.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.CommonTask;

public interface CommonTaskDao extends CrudRepository<CommonTask, Integer> {

	@Query("SELECT cm FROM CommonTask cm JOIN cm.commonTaskUsers cmu WHERE cmu.user.id=?1 ORDER BY cmu.last_time_read")
	public Iterable<CommonTask> findByUserId(Integer id);
	
	@Query("SELECT count(cm) FROM CommonTask cm JOIN cm.commonTaskUsers cmu JOIN cm.messages gm "
			+ "WHERE cmu.user.id=?1 "
			+ "AND CONVERT(gm.datetime, DATETIME) > CONVERT(cmu.last_time_read, DATETIME)")
	public long countNewGrupalMessages(Integer userId);
	
	@Query("SELECT count(cm) FROM CommonTask cm WHERE cm.id NOT IN (SELECT t.id FROM CommonTask t JOIN t.messages)")
	public long countNewCommonTasks(Integer userId);
	
}
