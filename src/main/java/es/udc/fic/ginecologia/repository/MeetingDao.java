package es.udc.fic.ginecologia.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Meeting;
import es.udc.fic.ginecologia.model.Patient;

public interface MeetingDao extends CrudRepository<Meeting, Integer> {

	@Query("SELECT DISTINCT m.patient, m.meeting_date FROM Meeting m WHERE m.user.id=?1 AND m.patient.enabled=1 ORDER BY m.meeting_date DESC")
	public Iterable<Patient> findLastSeenPatients(Integer userId);
}
