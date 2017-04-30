package assignment7;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private int Sender;
	private int Receiver; 
	private String username;
	
	public Message(String text, String username) {
		this.text = text;
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSender() {
		return Sender;
	}
	public void setSender(int sender) {
		Sender = sender;
	}
	public int getReceiver() {
		return Receiver;
	}
	public void setReceiver(int receiver) {
		Receiver = receiver;
	}

}	
