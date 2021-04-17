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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message {
	@Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
	
	private String subject;
	
	private String message_body;
	
	private LocalDateTime message_date;
	
	private boolean message_read;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="sender_id")
	private User sender;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="receiver_id")
	private User receiver;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "replied_message_id", referencedColumnName = "id")
    private Message messageReplied;
	
	@ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="interconsultation_meeting_id")
	private Meeting interconsultationMeeting;

	public Message() {
		
	}

	public Message(String subject, String message_body) {
		this.subject = subject;
		this.message_body = message_body;
		
		this.message_date = LocalDateTime.now();
		this.message_read = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage_body() {
		return message_body;
	}

	public void setMessage_body(String message_body) {
		this.message_body = message_body;
	}

	public LocalDateTime getMessage_date() {
		return message_date;
	}

	public void setMessage_date(LocalDateTime message_date) {
		this.message_date = message_date;
	}

	public boolean isMessage_read() {
		return message_read;
	}

	public void setMessage_read(boolean message_read) {
		this.message_read = message_read;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Message getMessageReplied() {
		return messageReplied;
	}

	public void setMessageReplied(Message messageReplied) {
		this.messageReplied = messageReplied;
	}

	public Meeting getInterconsultationMeeting() {
		return interconsultationMeeting;
	}

	public void setInterconsultationMeeting(Meeting interconsultationMeeting) {
		this.interconsultationMeeting = interconsultationMeeting;
	}
	
}
