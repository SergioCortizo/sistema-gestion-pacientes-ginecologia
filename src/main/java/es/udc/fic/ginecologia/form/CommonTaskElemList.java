package es.udc.fic.ginecologia.form;

import java.time.LocalDateTime;

public class CommonTaskElemList {

	private Integer id;

	private String title;

	private long newMessages;

	private LocalDateTime lastTimeRead;
	
	private boolean isNew;

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

	public long getNewMessages() {
		return newMessages;
	}

	public void setNewMessages(long newMessages) {
		this.newMessages = newMessages;
	}

	public LocalDateTime getLastTimeRead() {
		return lastTimeRead;
	}

	public void setLastTimeRead(LocalDateTime lastTimeRead) {
		this.lastTimeRead = lastTimeRead;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}
