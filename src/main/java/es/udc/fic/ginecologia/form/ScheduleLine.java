package es.udc.fic.ginecologia.form;

import java.time.LocalTime;

public class ScheduleLine {
	private boolean selected;

	private Integer weekday;

	private LocalTime initial_hour;

	private LocalTime final_hour;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Integer getWeekday() {
		return weekday;
	}

	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
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

}
