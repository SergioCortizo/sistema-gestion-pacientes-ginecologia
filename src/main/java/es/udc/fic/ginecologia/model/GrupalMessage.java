package es.udc.fic.ginecologia.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "grupal_message")
public class GrupalMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String message_body;

	private LocalDateTime datetime;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", insertable = true, updatable = false)
	private User user;

	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "common_task_id", insertable = true, updatable = false)
	private CommonTask commonTask;

	public GrupalMessage() {

	}

	public GrupalMessage(String message_body) {
		this.message_body = message_body;
		this.datetime = LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage_body() {
		return message_body;
	}

	public void setMessage_body(String message_body) {
		this.message_body = message_body;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CommonTask getCommonTask() {
		return commonTask;
	}

	public void setCommonTask(CommonTask commonTask) {
		this.commonTask = commonTask;
	}

	@Override
	public String toString() {
		return "GrupalMessage [id=" + id + ", message_body=" + message_body + ", datetime=" + datetime.toString() + "]";
	}

}
