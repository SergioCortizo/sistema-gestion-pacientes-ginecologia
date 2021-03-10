package es.udc.fic.ginecologia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class SchedulePK implements Serializable {
	
	private static final long serialVersionUID = 2613802114425427055L;

	@Column(name="user_id")
	private Integer user_id;
	
	@Column(columnDefinition = "ENUM('monday', 'tuesday', 'wednesday', 'thursday', 'friday')")
    @Enumerated(EnumType.STRING)
	private Weekday weekday;

	public SchedulePK() {
		
	}

	public SchedulePK(Integer user_id, Weekday weekday) {
		this.user_id = user_id;
		this.weekday = weekday;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Weekday getWeekday() {
		return weekday;
	}

	public void setWeekday(Weekday weekday) {
		this.weekday = weekday;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		result = prime * result + ((weekday == null) ? 0 : weekday.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedulePK other = (SchedulePK) obj;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		if (weekday != other.weekday)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SchedulePK [user_id=" + user_id + ", weekday=" + weekday + "]";
	}
	
}
