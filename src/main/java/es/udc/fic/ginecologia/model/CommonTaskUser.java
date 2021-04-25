package es.udc.fic.ginecologia.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "common_task_user")
public class CommonTaskUser {

	@EmbeddedId
	private CommonTaskUserPK pk;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "common_task_id", insertable = false, updatable = false)
	private CommonTask commonTask;
	
	private LocalDateTime last_time_read;

	public CommonTaskUserPK getPk() {
		return pk;
	}

	public void setPk(CommonTaskUserPK pk) {
		this.pk = pk;
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

	public LocalDateTime getLast_time_read() {
		return last_time_read;
	}

	public void setLast_time_read(LocalDateTime last_time_read) {
		this.last_time_read = last_time_read;
	}

	@Override
	public String toString() {
		return "CommonTaskUser [pk=" + pk + ", user=" + user + ", commonTask=" + commonTask + ", last_time_read="
				+ last_time_read.toString() + "]";
	}
	
}
