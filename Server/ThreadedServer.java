package Server;

import java.io.*;
import java.net.*;
import java.util.*;

import Message.*;



public class ThreadedServer  implements Runnable
{
	private ArrayList<ClientHandler> client;
	private static ThreadedServer instance;
	
	
	private ThreadedServer()
	{
		client = new ArrayList<ClientHandler>();
	}
	
	public static ThreadedServer getInstance()
	{
		 if(instance == null) 
		 {
			 instance = new ThreadedServer();
	     }     
		 return instance;
	}
	
	public ArrayList getClientName()
	{
		return client;
	}
	
	@Override
	public void run() 
	{
		try 
		{
			ServerSocket server = new ServerSocket(8189);
			int i = 1;
			
			while(true) 
			{
				Socket coming = server.accept();
				
				ClientHandler newClient = new ClientHandler(coming, "usr " + i);
				client.add(newClient);
				Runnable r = newClient;
				Thread t = new Thread(r);
				t.start();
				i++;
			}
			
		}
		catch(IOException e) 
		{			
			e.printStackTrace();
		}		
	}
	
	private class ClientHandler implements Runnable 
	{
		
		private Socket coming;
		private String name;
		private ObjectOutputStream outStream ;
		private ObjectInputStream inStream;
		
		
		public ClientHandler(Socket coming, String name) 
		{
			
			this.coming = coming;
			this.name = name;
			try {
				outStream = new ObjectOutputStream ( coming.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public ObjectOutputStream getObjectOutputStream()
		{
			return outStream;
		}
		public Socket getSocket()
		{
			return coming;
		}
		
		public String getName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}
		
		@Override
		public void run() {
			
			
			try 
			{
				
				Object objectMessage;
				inStream = new ObjectInputStream(coming.getInputStream());
				
				
				while((objectMessage = inStream.readObject()) != null) 
				{
					if(objectMessage instanceof ChatMessage)
					{
						ChatMessage chatMessage = (ChatMessage) objectMessage;
						String line = chatMessage.getDescription();
						
						for (ClientHandler tmp : client) 
						{
							String line2 = name + " >>> " + line;
							ObjectOutputStream outStream;
							outStream = tmp.getObjectOutputStream();
							ChatMessage newMessage = new ChatMessage(line2);
							outStream.writeObject(newMessage);
						}
					}
					else if(objectMessage instanceof NewNameMessage)
					{
						NewNameMessage nameMessage = (NewNameMessage) objectMessage;
						String newName = nameMessage.getName();
						Boolean isName = false;
						
						for (ClientHandler tmp : client) 
						{
							String oldName = tmp.getName();
							
							if(oldName.equals(newName))
							{
								isName = true;
								break;
							}
						}
						
						if(!isName)
						{
							name = newName;
							ChatMessage newMessage = new ChatMessage("Your new name is" + name);
							outStream.writeObject(newMessage);
						}
					}

					
				}	
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}


}
