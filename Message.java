package assignment7;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private String username;
	private ArrayList<String> receivers;
	private String image;
	
	public Message(){
		this.text = "";
		this.username = "";
	}
	
	public Message(String text, String username, ArrayList<String> receivers) {
		this.text = text;
		this.username = username;
		this.receivers = receivers;
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
	public void setReceivers(ArrayList<String> receivers) {
		this.receivers = receivers;
	}
	public ArrayList<String> getReceivers() {
		return receivers;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}
}	
