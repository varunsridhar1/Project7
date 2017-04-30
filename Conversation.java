package assignment7;

import java.util.ArrayList;

public class Conversation extends Message{
	private static final long serialVersionUID = 1L;
	public boolean display;
	public ArrayList<String> members;
	public ArrayList<String> conversation;
	
	public ArrayList<String> getConversation() {
		return conversation;
	}
	
	public void addtoConversation(String message) {
		conversation.add(message);
	}
	
	public Conversation(){
		this.conversation = new ArrayList<String>();
		this.display = true;
	}
	public Conversation(ArrayList<String> conversation) {
		super();
		this.conversation = conversation;
		this.display =true;
	}
	public void setMembers(ArrayList<String> members){
		this.members = members;
	}
	public boolean checkMembers(ArrayList<String> members){
		if(members != null){
		for(String member : members){
			if(!this.members.contains(member)){
				return false;
			}
		}
		return true;
		}
		
		else{
			return false;
		}
	}
	public void setdisplay(boolean display){
		this.display = display;
	}
}
