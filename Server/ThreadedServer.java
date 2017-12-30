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
	
	//coment
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
			
			String line = "Success to create the game. Your game code: " + gameID;
			SuccessMessage newMessage = new SuccessMessage(2567, line);
			try {
				outStream.writeObject(newMessage);
			} catch (IOException e) {

			}
			countIdGame++;
		}
		
		private Boolean joinGame(int idGame)
		{
			Boolean join = false;
			Game newGame = null;
			
			for (Game tmpGame : tabGame) 
			{
				int currentIdGame = tmpGame.getID();
				
				if(currentIdGame == idGame)
				{
					if(!tmpGame.getStartStatus())
					{
						newGame = tmpGame;
						this.myPlayer = newGame.addPalyer();
						this.myGame = newGame;
						this.gameID = idGame;
						join = true;
					}
				}
				
			}
			
			return join;
			
			
			
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
								String line2 = name + ": " + line;
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
							String line = "Your new name is " + name;
							SuccessMessage newMessage = new SuccessMessage(3689, line);
							outStream.writeObject(newMessage);
						}
						else
						{
							FailMessage newMessage = new FailMessage(3689, "Your name is reserve");
							outStream.writeObject(newMessage);
						}
					}
					else if(objectMessage instanceof JoinGameMessage)
					{
						JoinGameMessage gameMessage = (JoinGameMessage) objectMessage;
						Boolean joinStatus = false;
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
									joinStatus = this.joinGame(tmpID);
							}

							if(joinStatus)
							{
								String line = "Success to join the game. Your game code: " + gameID;
								SuccessMessage newMessage = new SuccessMessage(2567, line);
								outStream.writeObject(newMessage);
							}
							else
							{
								FailMessage newMessage = new FailMessage(2567, "Fail to join the game");
								outStream.writeObject(newMessage);
							}
						}
					}
					else if(objectMessage instanceof MoveMessage)
					{
						MoveMessage newMove = (MoveMessage) objectMessage;
						if(newMove.getEndTurn())
						{
							try 
							{
								myGame.endMove(myPlayer);
							} catch (BadPlayerException e) 
							{
								// TODO Auto-generated catch block for later
							}
						}
						else
						{
							int oldX = newMove.getOldX();
							int oldY = newMove.getOldY();
							int newX = newMove.getNewX();
							int newY = newMove.getNewY();
							
							try {
								myGame.move(myPlayer, oldX, oldY, newX, newY);
								MoveMessage okMove = new MoveMessage(oldX, oldY, newX, newY);
								
								for (ClientHandler tmp : client)
								{
									if(tmp.getGameID() == this.gameID)
									{
										outStream.writeObject(okMove);
									}
								}
							} catch (BadPlayerException e) {
								
							} catch (IncorrectMoveException e) {
								FailMessage newFail = new FailMessage(1235,"Incorrect move!");
								outStream.writeObject(newFail);
							}
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
