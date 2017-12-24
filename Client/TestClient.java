package Client;

import java.io.IOException;
import java.util.Scanner;

import Message.*;


public class TestClient {

	public static void main(String[] args) 
	{
		Client newClient = new Client();
		newClient.connectServer();
		
		Scanner inn = new Scanner(System.in);
		
		while(true)
		{
				
			String line2 = inn.nextLine();
			
			
			if(line2.equals("chat"))
			{
				line2 = inn.nextLine();
				ChatMessage newMessage = new ChatMessage(line2);
				
				try {
					newClient.sendMessage(newMessage);
				} catch (IOException e) {
					System.out.println("sending failed!");
				}
			}
			else if(line2.equals("name"))
			{
				line2 = inn.nextLine();
				NewNameMessage newMessage = new NewNameMessage(line2);
				
				try {
					newClient.sendMessage(newMessage);
				} catch (IOException e) {
					System.out.println("sending failed!");
				}
			}
			
			
		}
		
		

	}

}
