package es.udc.fic.ginecologia.repository;

import org.springframework.data.repository.CrudRepository;

import es.udc.fic.ginecologia.model.Schedule;
import es.udc.fic.ginecologia.model.SchedulePK;

public interface ScheduleDao extends CrudRepository<Schedule, SchedulePK> {


}
