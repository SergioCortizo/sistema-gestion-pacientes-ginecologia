package es.udc.fic.ginecologia.model;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "schedule")
public class Schedule implements Serializable{
	
	private static final long serialVersionUID = -3539953386086558292L;

	@EmbeddedId
	private SchedulePK pk;
	
	private LocalTime initial_hour;
	
	private LocalTime final_hour;

	public SchedulePK getPk() {
		return pk;
	}

	public void setPk(SchedulePK pk) {
		this.pk = pk;
	}

	public LocalTime getInitial_hour() {
		return initial_hour;
	}

	public void setInitial_hour(LocalTime initial_hour) {
		this.initial_hour = initial_hour;
	}

	public LocalTime getFinal_hour() {
		return final_hour;
	}

	public void setFinal_hour(LocalTime final_hour) {
		this.final_hour = final_hour;
	}

	@Override
	public String toString() {
		return "Schedule [pk=" + pk.toString() + ", initial_hour=" + initial_hour.toString() + ", final_hour=" + final_hour.toString() + "]";
	}
	
}
