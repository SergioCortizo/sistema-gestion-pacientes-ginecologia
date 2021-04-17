package es.udc.fic.ginecologia.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Message;

public interface MessageDao extends CrudRepository<Message, Integer> {

	@Query("SELECT m FROM Message m WHERE m.sender.id=?1 OR m.receiver.id=?1 ORDER BY m.message_date DESC")
	public Iterable<Message> findByUserId(Integer id);
}
