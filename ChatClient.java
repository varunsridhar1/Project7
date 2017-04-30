package assignment7;

import java.io.*;
import java.net.*;

public class ChatClient {
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private ClientView view;
	private String username;
	private String ipAddress;
	
	public void run(String[] args) throws Exception {
		this.ipAddress = args[0];
		setUpNetworking();
		ClientView.setClient(this);
		javafx.application.Application.launch(ClientView.class);
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket(ipAddress, 4242);
		writer = new ObjectOutputStream(sock.getOutputStream());
		reader = new ObjectInputStream(sock.getInputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
	
	public void writeMessage(Message message) {
		try {
			writer.writeObject(message);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			Message message;
			try {
				while ((message = (Message) reader.readObject()) != null) {
					if(view != null && !(message instanceof Conversation)) {
						view.addText(message);
					}
					if(message instanceof Conversation){
						Conversation convo = (Conversation) message;
						if(view != null && convo != null)
							view.addHistory(convo);
					}
				}
			} catch (IOException ex) {
				if(ex instanceof SocketException)
					System.exit(0);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setView(ClientView view) {
		this.view = view;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
}
