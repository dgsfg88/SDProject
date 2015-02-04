package uni.project.sd.comunications.entity;

import java.io.Serializable;

import uni.project.sd.event.EventTime;

/**
 * TODO Aggiungere message number basato sul numero di tutti
 * @author ianni
 *
 */
public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3171887202877042484L;
	private int messageType;
	private String messageID;
	private String sender;
	private String receiver;
	private String message;
	private EventTime myTime;
	private Token token;
	
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setMyTime(EventTime myTime) {
		this.myTime = myTime;
	}
	
	public EventTime getMyTime() {
		return myTime;
	}
	
	public Token getToken() {
		return token;
	}
	
	public void setToken(Token token) {
		this.token = token;
	}
}
