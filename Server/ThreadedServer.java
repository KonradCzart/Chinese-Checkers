package Server;

import java.io.*;
import java.net.*;
import java.util.*;

import Client.*;
import Game.*;
import Message.*;

/** 
* @author Konrad Czart
* Server class with multi thread connection in client
*/ 

public class ThreadedServer implements Runnable {
	private ArrayList<ClientHandler> client;
	private ArrayList<Game> tabGame;
	private static ThreadedServer instance;
	private boolean serverRun;
	private static int countIdGame;
	
	
	private ThreadedServer()
	{
		client = new ArrayList<ClientHandler>();
		tabGame = new ArrayList<Game>();
		countIdGame = 1;
	}
	
	/**
	 * @return instance ThreadedServer (Singleton)
	 */
	public static ThreadedServer getInstance()
	{
		
		 if(instance == null) 
		 {
			 instance = new ThreadedServer();
	     }     
		 return instance;
	}

	public boolean isThreadedServerRun()
	{
		return serverRun;
	}
	
	
	/**
	 * @return ArrayList<String> with name client connect with server
	 */
	public ArrayList getClientName()
	{
		ArrayList<String> clientName = new ArrayList<String>();
		
		for (ClientHandler tmp : client)
		{
			clientName.add(tmp.getName());
		}
		return clientName;
	}
	
	@Override
	public void run() 
	{
		try 
		{
			ServerSocket server = new ServerSocket(8189);
			serverRun = true;
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

	// internal class client
	private class ClientHandler implements Runnable 
	{
		
		private Socket coming;
		private String name;
		private ObjectOutputStream outStream;
		private ObjectInputStream inStream;
		private int gameID;
		private ColorPlayer myPlayer;
		private Game myGame;
		
		
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
			
			gameID = 0;
			myPlayer = ColorPlayer.PLAYER_EMPTY;
			myGame = null;
			
		}
		
		public ColorPlayer getMyplayer()
		{
			return myPlayer;
		}
		
		public int getGameID()
		{
			return gameID;
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
		
		private void createGame()
		{
			Game newGame = new Game(countIdGame);
			this.gameID = countIdGame;
			tabGame.add(newGame);
			this.myGame = newGame;
			this.myPlayer = newGame.addPalyer();
			
			countIdGame++;
		}
		
		private void joinGame(int idGame)
		{
			Game newGame = null;
			
			for (Game tmpGame : tabGame) 
			{
				int currentIdGame = tmpGame.getID();
				
				if(currentIdGame == idGame)
				{
					newGame = tmpGame;
					break;
				}
				
			}
			
			this.myPlayer = newGame.addPalyer();
			this.myGame = newGame;
			this.gameID = idGame;
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
							if(tmp.getGameID() == this.gameID)
							{
								String line2 = name + " >>> " + line;
								ObjectOutputStream outStream;
								outStream = tmp.getObjectOutputStream();
								ChatMessage newMessage = new ChatMessage(line2);
								outStream.writeObject(newMessage);
							}
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
					else if(objectMessage instanceof JoinGameMessage)
					{
						JoinGameMessage gameMessage = (JoinGameMessage) objectMessage;
						
						if(gameMessage.getNewGame())
						{
							this.createGame();
						}
						else
						{
							int tmpID;
							
							for (Game tmp : tabGame) 
							{
								tmpID = tmp.getID();
								
								if(gameMessage.getIdGame() == tmpID)
									this.joinGame(tmpID);
							}
							
							// dodac wysylanie bledu jesli nie ma gry o takim id
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
