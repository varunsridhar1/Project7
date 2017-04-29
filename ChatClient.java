package assignment7;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	//private BufferedReader reader;
	//private PrintWriter writer;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private ClientView view;
	private String username;
	private ArrayList<String> chatMembers;
	
	public void run() throws Exception {
		setUpNetworking();
		ClientView.setClient(this);
		javafx.application.Application.launch(ClientView.class);
	}
	
	/*private void initView() {
		JFrame frame = new JFrame("Ludicrously Simple Chat Client");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(650, 500);
		frame.setVisible(true);

	}*/

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		//InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		//reader = new BufferedReader(streamReader);
		//writer = new PrintWriter(sock.getOutputStream());
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
					if(view != null) {
						view.addText(message);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
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
		this.username = username;;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setChatMembers(ArrayList<String> chatMembers) {
		this.chatMembers = chatMembers;
	}
	
	public ArrayList<String> getChatMembers() {
		return chatMembers;
	}
}
