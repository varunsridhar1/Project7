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
	private BufferedReader reader;
	private PrintWriter writer;
	private ClientView view;
	
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
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
	
	public void writeMessage(String message) {
		writer.println(message);
		writer.flush();
	}
	
	
	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					if(view != null)
						view.addText(message + "\n");;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void setView(ClientView view) {
		this.view = view;
	}
	
}
