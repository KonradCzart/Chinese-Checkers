package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Scanner;



import Message.*;

public class ServerListener implements Runnable
{
	Socket currentSocket;
	Object tmp;
	
	public ServerListener(Socket socket)
	{
		currentSocket = socket;
	}
	@Override
	public void run() {
		
		
		try {
			
			
			ObjectInputStream in = new ObjectInputStream(currentSocket.getInputStream());
			
			while((tmp = in.readObject()) != null)
			{
				
				if(tmp instanceof ChatMessage)
				{
					ChatMessage chat = (ChatMessage) tmp;
					String line = chat.getDescription();			
					System.out.println(line);
				}
				
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Koniec lisenera");
	}

}
