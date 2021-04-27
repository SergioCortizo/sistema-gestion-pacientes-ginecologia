package es.udc.fic.ginecologia.form;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import es.udc.fic.ginecologia.model.LogLevel;

public class LogForm {

	private LogLevel level;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate date1;
	
	private LocalTime time1;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate date2;
	
	private LocalTime time2;

	public LogLevel getLevel() {
		return level;
	}

	public void setLevel(LogLevel level) {
		this.level = level;
	}

	public LocalDate getDate1() {
		return date1;
	}

	public void setDate1(LocalDate date1) {
		this.date1 = date1;
	}

	public LocalTime getTime1() {
		return time1;
	}

	public void setTime1(LocalTime time1) {
		this.time1 = time1;
	}

	public LocalDate getDate2() {
		return date2;
	}

	public void setDate2(LocalDate date2) {
		this.date2 = date2;
	}

	public LocalTime getTime2() {
		return time2;
	}

	public void setTime2(LocalTime time2) {
		this.time2 = time2;
	}

	@Override
	public String toString() {
		return "LogForm [level=" + level + ", date1=" + date1 + ", time1=" + time1 + ", date2=" + date2 + ", time2="
				+ time2 + "]";
	}
	
}
