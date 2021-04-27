package es.udc.fic.ginecologia.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "application_logs")
public class LogLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "ENUM('OFF', 'FATAL', 'ERROR', 'WARN', 'INFO', 'DEBUG', 'TRACE', 'ALL')")
    @Enumerated(EnumType.STRING)
	private LogLevel level;
	
	private LocalDateTime timestamp;
	
	private String thread;
	
	private String logger;
	
	private String message;

	public LogLine() {

	}

	public LogLine(LogLevel level, LocalDateTime timestamp, String thread, String logger, String message) {
		this.level = level;
		this.timestamp = timestamp;
		this.thread = thread;
		this.logger = logger;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LogLevel getLevel() {
		return level;
	}

	public void setLevel(LogLevel level) {
		this.level = level;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "LogLine [id=" + id + ", level=" + level + ", timestamp=" + timestamp + ", thread=" + thread
				+ ", logger=" + logger + "]";
	}
	
}
