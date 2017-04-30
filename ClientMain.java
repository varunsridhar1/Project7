package assignment7;

public class ClientMain {
	public static void main(String[] args)  {
		try {
			new ChatClient().run(args);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
