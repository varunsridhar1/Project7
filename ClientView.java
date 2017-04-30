package assignment7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientView extends Application {
	private Stage primaryStage;
	private Stage stage2;
	private AnchorPane root;
	private static ChatClient chatClient;
	//private String messages = "";
	private ArrayList<String> messages;
	private String username = "";
	private String password = "";
	private ArrayList<String> chatMembers;
	
	private HashMap<Label, String> myEmojis;
	
	//@FXML
	//private Label messageLabel;
	
	@FXML
	private ListView<Label> messageList;
	
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
	
	@FXML
	private ListView<String> userList;
	
	@FXML
	private Button chatButton;
	
	@FXML
	private ComboBox<Label> selectEmoji;
	
	@FXML
	public void initialize() {
		if(userList != null) {
			userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			try {
			 	//String allmyUsers = "";
				//chatClient.setChatMembers(chatMembers);
			 	String line = "";
				FileReader fileReader = new FileReader("TopSecret_User.txt");
				@SuppressWarnings("resource")
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				ArrayList<String> userArrayList = new ArrayList<String>();
				while ((line = bufferedReader.readLine())!= null){
					if(!line.equals(chatClient.getUsername()))
						userArrayList.add(line);
				}
				ObservableList<String> userObsList = FXCollections.observableArrayList(userArrayList);
				userList.setItems(userObsList);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch(IOException ex){
				 ex.printStackTrace();
			 }
		}
		chatMembers = new ArrayList<String>();
		//messages = new ArrayList<String>();
	}
	
	public static void setClient(ChatClient client) {
		chatClient = client;
	}
	
	@Override
	public void start(Stage primaryStage) {
		username = "";
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SLIPPPPPPPP DAYYYYYYYYY");
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
		});
		
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
					System.out.println("Valid User!");
					chatClient.setUsername(username);
					Stage stage = (Stage) userButton.getScene().getWindow();
					stage.close();					
					errorMessage.setText("");
					stage2 = new Stage();
					stage2.setTitle("MyChat: " + chatClient.getUsername());
					stage2.setOnCloseRequest(new EventHandler<WindowEvent>() {
			            @Override
			            public void handle(WindowEvent t) {
			                Platform.exit();
			                System.exit(0);
			            }
					});
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
	
	
	/*@Override
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
		startButton.setDisable(true);
		try {
		 	//String allmyUsers = "";
			//chatClient.setChatMembers(chatMembers);
		 	String line = "";
			FileReader fileReader = new FileReader("TopSecret_User.txt");
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			ArrayList<String> userArrayList = new ArrayList<String>();
			while ((line = bufferedReader.readLine())!= null){
				if(!line.equals(chatClient.getUsername()))
					userArrayList.add(line);
			}
			ObservableList<String> userObsList = FXCollections.observableArrayList(userArrayList);
			userList.setItems(userObsList);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException ex){
			 ex.printStackTrace();
		 }
	}
	
	@FXML
	public void onChat(ActionEvent ae) {
		Conversation convo = new Conversation();
		chatMembers.add(chatClient.getUsername());
		chatMembers.addAll(userList.getSelectionModel().getSelectedItems());
		//chatMembers.add("sai");
		//chatMembers.add("sriram");
		convo.setMembers(chatMembers);
		messages = new ArrayList<String>();
		chatClient.writeMessage(convo);
		
		initializeEmojis();
		
		ArrayList<Label> emojis = new ArrayList<Label>();
		for(Label l: myEmojis.keySet())
			emojis.add(l);
		ObservableList<Label> obsEmojis = FXCollections.observableArrayList(emojis);
		selectEmoji.setItems(obsEmojis);
		
		chatClient.setView(this);
	}
	
	@FXML
	public void onEnter(ActionEvent ae) {
		Message message = new Message(messageBox.getText(), chatClient.getUsername(), chatMembers);
		chatClient.writeMessage(message);
		messageBox.clear();	
	}
	
	@FXML
	public void sendEmoji(ActionEvent ae) {
		String path = myEmojis.get(selectEmoji.getValue());
		Message message = new Message(path, chatClient.getUsername(), chatMembers);
		chatClient.writeMessage(message);
		selectEmoji.setPromptText("Emoji:");
	}
	
	public void addText(Message message) {
		if(chatMembers.contains(message.getUsername()) && chatMembers.containsAll(message.getReceivers())) {
			if(message.getImage() != null)
				messages.add(message.getUsername() + ": " + message.getImage());
			else
				messages.add(message.getUsername() + ": " + message.getText() + "\n");
			ArrayList<Label> labels = new ArrayList<Label>();
			for(String s: messages) {
				if(s.contains(".png")) {
					Label label = new Label();
					int fileIndex = s.indexOf(':') + 2;
					label.setContentDisplay(ContentDisplay.RIGHT);
					label.setText(s.substring(0, fileIndex));
					Image image = new Image(new File(s.substring(fileIndex, s.length() - 1)).toURI().toString());
					ImageView iv = new ImageView(image);
					label.setGraphic(iv); 
					labels.add(label);
				}
				else {
					Label label = new Label();
					label.setText(s);
					labels.add(label);
				}
			}
			ObservableList<Label> obsMessages = FXCollections.observableArrayList(labels);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					messageList.setItems(obsMessages);
					messageList.scrollTo(messageList.getItems().size() - 1);
					ArrayList<String> sounds = new ArrayList<String>();
					sounds.add("EHHH.mp4");
					sounds.add("SlipDay.mp4");
					sounds.add("YaKnow.mp4");
					sounds.add("Method.mp4");
					if(!chatClient.getUsername().equals(message.getUsername())) {
						Random rand = new Random();
						int soundChoice = rand.nextInt(4);
						String pling = sounds.get(soundChoice);
						Media hit = new Media(new File(pling).toURI().toString());
						MediaPlayer mediaPlayer = new MediaPlayer(hit);
						mediaPlayer.play();
					}
				}
			});
		}
	}
	
	public void addHistory(Conversation convo){
		if(chatMembers.containsAll(convo.members)) {
			messages = convo.getConversation();
			ObservableList<String> obsMessages = FXCollections.observableArrayList(messages);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					//messageList.setItems(obsMessages);
					messageList.scrollTo(messageList.getItems().size() - 1);
				}
			});
		}
	}
	
	private void initializeEmojis() {
		myEmojis = new HashMap<Label, String>();
		Label label = new Label();
		Image image = new Image(new File("1f60a.png").toURI().toString());
		ImageView iv = new ImageView(image);
		label.setGraphic(iv); 
		myEmojis.put(label, "1f60a.png");
		
		label = new Label();
		image = new Image(new File("1f60b.png").toURI().toString());
		iv = new ImageView(image);
		label.setGraphic(iv); 
		myEmojis.put(label, "1f60b.png");
		
		label = new Label();
		image = new Image(new File("1f60c.png").toURI().toString());
		iv = new ImageView(image);
		label.setGraphic(iv); 
		myEmojis.put(label, "1f60c.png");
		
		label = new Label();
		image = new Image(new File("1f60d.png").toURI().toString());
		iv = new ImageView(image);
		label.setGraphic(iv); 
		myEmojis.put(label, "1f60d.png");
		
		label = new Label();
		image = new Image(new File("1f60e.png").toURI().toString());
		iv = new ImageView(image);
		label.setGraphic(iv); 
		myEmojis.put(label, "1f60e.png");
		
		label = new Label();
		image = new Image(new File("1f60f.png").toURI().toString());
		iv = new ImageView(image);
		label.setGraphic(iv); 
		myEmojis.put(label, "1f60f.png");
		
	}
	
	
}