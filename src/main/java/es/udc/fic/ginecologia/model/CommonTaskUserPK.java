package es.udc.fic.ginecologia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CommonTaskUserPK implements Serializable {

	private static final long serialVersionUID = -5725957395153099947L;

	@Column(name = "user_id")
	private Integer user_id;
	
	@Column(name = "common_task_id")
	private Integer common_task_id;

	public CommonTaskUserPK() {

	}

	public CommonTaskUserPK(Integer user_id, Integer common_task_id) {
		this.user_id = user_id;
		this.common_task_id = common_task_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getCommon_task_id() {
		return common_task_id;
	}

	public void setCommon_task_id(Integer common_task_id) {
		this.common_task_id = common_task_id;
	}

	@Override
	public String toString() {
		return "CommonTaskUserPK [user_id=" + user_id + ", common_task_id=" + common_task_id + "]";
	}
	
}
