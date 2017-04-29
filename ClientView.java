package assignment7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class ClientView extends Application {
	private Stage primaryStage;
	private Stage stage2;
	private AnchorPane root;
	private static ChatClient chatClient;
	private static ArrayList<ClientView> viewList = new ArrayList<ClientView>();
	private String messages = "";
	private String username = "";
	private String password = "";
	
	@FXML
	private Label messageLabel;
	
	@FXML
	private TextField messageBox;
	
	@FXML
	private Button startButton;
	
	@FXML
	private TextField UserNameBox;
	
	@FXML
	private TextField PasswordBox;
	
	@FXML
	private Label errorMessage;
	
	@FXML
	private Button userButton;
	
	public static void setClient(ChatClient client) {
		chatClient = client;
	}
	
	@Override
	public void start(Stage primaryStage) {
		username = "";
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SLIPPPPPPPP DAYYYYYYYYY");
		
		LoginLoader();
		
	}
	
	public void LoginLoader() {
		try {
			  // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientView.class.getResource("ClientLoginScreen.fxml"));
            root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void onLogin(ActionEvent ae) {
		username = UserNameBox.getText();
		UserNameBox.clear();
		int user = checkUser(username);
		if(user==-1){
			PasswordBox.clear();
			errorMessage.setText("Invalid Username");
			errorMessage.setTextFill(javafx.scene.paint.Color.RED);
		}
		else{
			if(PasswordBox.getText()!= null && !PasswordBox.getText().equals("")){
				password = PasswordBox.getText();
				PasswordBox.clear();
				if(checkPassword(password, user)){
					System.out.print("Valid User!");
					chatClient.setUsername(username);
					Stage stage = (Stage) userButton.getScene().getWindow();
					stage.close();					
					errorMessage.setText("");
					messages = "";
					stage2 = new Stage();
					stage2.setTitle("MyChat");
					ClientLoader();
				}
				else{
					errorMessage.setText("Invalid Username/Password Combination");
					errorMessage.setTextFill(javafx.scene.paint.Color.RED);
				}
			}
			else{
					errorMessage.setText("Please enter a Password");
					errorMessage.setTextFill(javafx.scene.paint.Color.RED);
				}
		}
	}
	
	public void createUser(ActionEvent ae) {
		String username = UserNameBox.getText();
		String password = PasswordBox.getText();
		UserNameBox.clear();
		PasswordBox.clear();
		if(checkUser(username)!=-1){
			errorMessage.setText("Username Taken. Please input another user");
			errorMessage.setTextFill(javafx.scene.paint.Color.RED);
			UserNameBox.clear();
			PasswordBox.clear();
		}
		else{
			BufferedWriter userWrite = null;
			BufferedWriter passwordWrite = null;
		      try {
		         // APPEND MODE SET HERE
		         userWrite = new BufferedWriter(new FileWriter("TopSecret_User.txt", true));
		         passwordWrite = new BufferedWriter(new FileWriter("TopSecret_Password.txt",true));
		         userWrite.write(username);
		         userWrite.newLine();
		         userWrite.flush();
		         passwordWrite.write(password);
		         passwordWrite.newLine();
		         passwordWrite.flush();
		      } 
		      catch (IOException ioe) {
		    	  ioe.printStackTrace();
		      } 
		      finally{                       // always close the file
		    	 	if(userWrite != null){ 
		    	 		try{
		    	 			userWrite.close();
		    	 			} 
		    	 		 catch (IOException ioe2) {

		    			 }
		    	 	}
		    	 	if(passwordWrite != null){
		    	 		try{
		    	 			passwordWrite.close();
		    	 		}
		    	 		catch(IOException ioe){
		    	 			
		    	 		}
		    	 	}
		      }
		   } 
		}
	
	public int checkUser(String user){
		String line = "";
		int count = 0;
		 try {
			FileReader fileReader = new FileReader("TopSecret_User.txt");
			  @SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine())!= null){
				if(user.equals(line)){
					return count;
				}
				count++;
			}
			return -1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -2;
		}
		 catch(IOException ex){
			 ex.printStackTrace();
			 return -3;
		 }
	}
	
	public boolean checkPassword(String password, int user){
		 try {
			 	String line = "";
				FileReader fileReader = new FileReader("TopSecret_Password.txt");
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				for(int i = 0; i <= user; i++){
					line = bufferedReader.readLine();
				}
				if(line.equals(password)){
					return true;
				}
				else{
					return false;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			 catch(IOException ex){
				 ex.printStackTrace();
				 return false;
			 }
	}
	
	/*
	@Override
	public void start(Stage primaryStage) {
		messages = "";
		viewList.add(this);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SLIPPPPPPPP DAYYYYYYYYY");
		ClientLoader();
	}*/
	
	public void ClientLoader() {
		try {
			  // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientView.class.getResource("ClientView.fxml"));
            root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage2.setScene(scene);
            stage2.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void startChat(ActionEvent ae) {
		startButton.disarm();
		chatClient.setView(this);
	}
	
	@FXML
	public void onEnter(ActionEvent ae) {
		Message message = new Message(messageBox.getText(), chatClient.getUsername());
		chatClient.writeMessage(message);
		messageBox.clear();	
		String pling = "EHHH.m4a";
		Media hit = new Media(new File(pling).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}
	
	public void addText(String message) {
		System.out.println(username);
		messages += message;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				messageLabel.setText(messages);
			}
		});
	}
	
	public static ArrayList<ClientView> getViews() {
		return viewList;
	}
	
}
