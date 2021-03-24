package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Meeting;

public interface MeetingDao extends CrudRepository<Meeting, Integer> {

}
