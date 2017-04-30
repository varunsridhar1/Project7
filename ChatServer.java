package assignment7;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ChatServer extends Observable {
	private static Observer remove;
	public ArrayList<Conversation> allConvos = new ArrayList<Conversation>();
	
	public void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		//observerList = new ArrayList<Observer>();
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			System.out.println("got a connection");
		}
	}
	
	public static void setRemove(Observer o) {
		remove = o;
	}
	
	class ClientHandler implements Runnable {
		private ObjectInputStream reader;

		public ClientHandler(Socket clientSocket) {
			Socket sock = clientSocket;
			try {
				reader = new ObjectInputStream(sock.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			Message message;
			try {
				while ((message = (Message) reader.readObject()) != null) {
					if(!(message instanceof Conversation)){
						String text = message.getUsername() + ": " + message.getText() + "\n";
						ArrayList<String> members = message.getReceivers();
						boolean isIncluded = false;
						for(Conversation convo : allConvos){
							if(convo.checkMembers(members)){
								convo.addtoConversation(text);
								isIncluded = true;
							}
						}
						if(!isIncluded){
							ArrayList<String> newConvoList = new ArrayList<String>();
							newConvoList.add(text);
							Conversation newConvo = new Conversation(newConvoList);
							newConvo.setMembers(members);
							allConvos.add(newConvo);
						}
						notifyObservers(message);
					}
					else{
						Conversation thisConvo = (Conversation) message;
						if(thisConvo.display){
							boolean isIncluded = false;
							for(Conversation convo : allConvos){
								if(convo.checkMembers(thisConvo.members))
									thisConvo = convo;
									isIncluded = true;
							}
							if(!isIncluded){
								allConvos.add(thisConvo);
							}
							notifyObservers(thisConvo);
						}
					}
					System.out.println("server read "+message.getText());
					setChanged();
				}
			} catch (IOException e) {
				if(e instanceof SocketException) {
					ChatServer.this.deleteObserver(remove);
					if(ChatServer.this.countObservers() == 1) {
						System.exit(0);
					}
				}
				else
					e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
