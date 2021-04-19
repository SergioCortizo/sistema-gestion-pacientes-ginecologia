package es.udc.fic.ginecologia.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "common_task")
public class CommonTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String title;

	private String description;
	
	@OneToMany
	@JoinColumn(name = "common_task_id", referencedColumnName = "id")
	private Set<CommonTaskUser> commonTaskUsers = new HashSet<>();
	
	@OneToMany
	@JoinColumn(name = "common_task_id", referencedColumnName = "id")
	private Set<GrupalMessage> messages = new HashSet<>();	

	public CommonTask() {

	}

	public CommonTask(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<CommonTaskUser> getCommonTaskUsers() {
		return commonTaskUsers;
	}

	public void setCommonTaskUsers(Set<CommonTaskUser> commonTaskUsers) {
		this.commonTaskUsers = commonTaskUsers;
	}

	public Set<GrupalMessage> getMessages() {
		return messages;
	}

	public void setMessages(Set<GrupalMessage> messages) {
		this.messages = messages;
	}
	
}
