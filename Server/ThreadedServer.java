package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
	private ArrayList<CheckersBot> tabBot;
	private static ThreadedServer instance;
	private boolean serverRun;
	private static int countIdGame;
	

	private ThreadedServer()
	{
		client = new ArrayList<ClientHandler>();
		tabGame = new ArrayList<Game>();
		tabBot = new ArrayList<CheckersBot>();
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
	
	/**
	 * 
	 * @return Status is started server
	 */
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
	/**
	 * Run thread server
	 */
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

	/**
	 * 
	 * @author Konrad Czart
	 * Private Class for connecting with server
	 * Send special message for client with concert command
	 */
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
		
		public void exitGame()
		{
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
		
		public void sendMessageToGame(Message newMessage) throws IOException
		{
			for (ClientHandler tmp : client)
			{
				ObjectOutputStream outS;
				outS = tmp.getObjectOutputStream();
				
				if(tmp.getGameID() == this.gameID)
				{
					outS.writeObject(newMessage);
				}
			}
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
						
						String line2 = name + ": " + line;
						ChatMessage newMessage = new ChatMessage(line2);

						this.sendMessageToGame(newMessage);
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
							//SuccessMessage newMessage = new SuccessMessage(3689, line);
							//outStream.writeObject(newMessage);
						}
						else
						{
							String line = "Your name is reserved! You defaul namie is " + name;
							FailMessage newMessage = new FailMessage(3689, line);
							outStream.writeObject(newMessage);
						}
					}
					else if(objectMessage instanceof JoinGameMessage)
					{
						JoinGameMessage gameMessage = (JoinGameMessage) objectMessage;
						Boolean joinStatus = false;
						
						if(gameID != 0)
						{
							FailMessage newMessage = new FailMessage(4876, "You are already connected to the game!");
							outStream.writeObject(newMessage);
						}
						else if(gameMessage.getNewGame())
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
								
								String line2 = name + ": Join to game :" + gameID;
								ChatMessage newMessage2 = new ChatMessage(line2);
								
								this.sendMessageToGame(newMessage2);
							}
							else
							{
								FailMessage newMessage = new FailMessage(2567, "Fail to join the game");
								outStream.writeObject(newMessage);
							}
						}
					}
					else if(objectMessage instanceof MoveMessage && myGame != null)
					{
						MoveMessage newMove = (MoveMessage) objectMessage;
						MoveMessage okMove = null;
						ColorPlayer nextColorPlayer = null;
						String nextNamePlayer = "Bot";
						
						if(newMove.getEndTurn())
						{
							try 
							{
								myGame.endMove(myPlayer);
								nextColorPlayer = myGame.getQueuePlayer();
								
								for (ClientHandler tmp : client)
								{
									
									if(tmp.getGameID() == this.gameID && tmp.getMyplayer() == nextColorPlayer)
									{
										nextNamePlayer = tmp.getName();
										break;
									}
								}
								
								okMove = new MoveMessage(true, nextColorPlayer, myPlayer, nextNamePlayer);
								
								this.sendMessageToGame(okMove);
								
								//=======================================
								
								this.moveBotQueue();
								//========================================================
								
								
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
								
								nextColorPlayer = myGame.getQueuePlayer();
								
								for (ClientHandler tmp : client)
								{
									
									if(tmp.getGameID() == this.gameID && tmp.getMyplayer() == nextColorPlayer)
									{
										nextNamePlayer = tmp.getName();
										break;
									}
								}
								okMove = new MoveMessage(oldX, oldY, newX, newY,nextColorPlayer, myPlayer, nextNamePlayer);
								
								if(myGame.winPlayer(myPlayer))
								{
									String winLine = name + ": won! congratulations :D";
									ChatMessage newWinMessage = new ChatMessage(winLine);
									this.sendMessageToGame(newWinMessage);
								}
								
								this.sendMessageToGame(okMove);
								
							} catch (BadPlayerException e) {
								FailMessage newFail = new FailMessage(1235,"Bad Player!");
								outStream.writeObject(newFail);
							} catch (IncorrectMoveException e) {
								FailMessage newFail = new FailMessage(1235,"Incorrect move!");
								outStream.writeObject(newFail);
							}
						}
						
						
					}
					else if(objectMessage instanceof SuccessMessage)
					{
						SuccessMessage succes = (SuccessMessage) objectMessage;
						if(succes.getCodSuccess() == 11111)
						{
							
							if(myGame != null)
							{
								int countPlayer = myGame.getCountPlayer();
								
								if(countPlayer != 1 && countPlayer != 5)
								{
									myGame.startGame();
									ArrayList<Pawn> tabPawn = myGame.getArrayPawn();
									for (ClientHandler tmp : client)
									{
										if(tmp.getGameID() == this.gameID)
										{
											ObjectOutputStream outS;
											outS = tmp.getObjectOutputStream();
										
											for(Pawn currentPawn : tabPawn)
											{
												AddPawnMessage addPawn = new AddPawnMessage(currentPawn.getPlayer(), currentPawn.getX(), currentPawn.getY());
												outS.writeObject(addPawn);
											}
										}
									}
									ColorPlayer nextColorPlayer = myGame.getQueuePlayer();
									String nextNamePlayer = "Bot";
								
									for (ClientHandler tmp : client)
									{
									
										if(tmp.getGameID() == this.gameID && tmp.getMyplayer() == nextColorPlayer)
										{
											nextNamePlayer = tmp.getName();
											break;
										}
									}

									MoveMessage okMove = new MoveMessage(true, nextColorPlayer, myPlayer, nextNamePlayer);
								
									this.sendMessageToGame(okMove);
									
									this.moveBotQueue();
								
								}
								else
								{
									FailMessage failMessage = new FailMessage(12589, "Bad number of player");
									outStream.writeObject(failMessage);
								}
							}
						}
						else if(succes.getCodSuccess() == 22222)
						{
							if(myGame != null)
							{
								tabGame.remove(myGame);
								String line = "The game about id: " +gameID + "  has been turned off";
								ChatMessage newMessage = new ChatMessage(line);
								SuccessMessage newSuccess = new SuccessMessage(22222, "remove game");
								this.sendMessageToGame(newSuccess);
								this.sendMessageToGame(newMessage);
								
								int exitId = gameID;
								
								for (ClientHandler tmp : client)
								{
									if(tmp.getGameID() == exitId)
									{
										tmp.exitGame();
									}
								}
							}
							
						}
						else if(succes.getCodSuccess() == 33333)
						{
							if(myGame != null)
							{
								if(!myGame.getStartStatus())
								{
									ColorPlayer botColor = myGame.addPalyer();
									CheckersBot bot = new CheckersBot(botColor, myGame, myGame.getID());
									tabBot.add(bot);
									myGame.addBotToGame(botColor);
									bot.setPawn(myGame.getConcretPawn(botColor));
								
									String line = "Add bot for game: " + gameID;
									ChatMessage newChatMessage = new ChatMessage(line);
									this.sendMessageToGame(newChatMessage);
								}
								else
								{
									FailMessage fail = new FailMessage(12589, "You can't add bot!");
									this.sendMessageToGame(fail);
								}
							}
							
						}
					}


				}
			} 
			catch (IOException e) 
			{
				client.remove(this);
				System.out.println("client disconnected");
			} catch (ClassNotFoundException e) {
				System.out.println("fail server 2");
			}			
		}
		
		private void moveBotQueue()
		{
			while(myGame.botQueue())
			{

				MoveMessage botMove = null;
				for(CheckersBot bot : tabBot)
				{


					if(bot.getGameID() == this.gameID && bot.getBotPlayer() == myGame.getQueuePlayer())
					{
						///////////
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e1) {

						}
						botMove = bot.moveBot();
						
						if(botMove.getEndTurn())
						{
							
							try {
								myGame.endMove(bot.getBotPlayer());
								ColorPlayer nextPlayer2 = myGame.getQueuePlayer();
								botMove.setNextMovePlayer(nextPlayer2);
								String name2 = "Bot";
								for (ClientHandler tmp : client)
								{
								
									if(tmp.getGameID() == this.gameID && tmp.getMyplayer() == nextPlayer2)
									{
										name2 = tmp.getName();
										break;
									}
								}
								
								botMove.setNextTurnPlayerName(name2);
								
								try {
									this.sendMessageToGame(botMove);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							} catch (BadPlayerException e) {
								System.out.println("exc1");
							}
						}
						else
						{
							try {
								myGame.endMove(bot.getBotPlayer());
								ColorPlayer nextPlayer = myGame.getQueuePlayer();
								botMove.setMovePlayer(bot.getBotPlayer());
								
								botMove.setNextMovePlayer(nextPlayer);
								
								String nextPlayerString = "Bot";
								
								for (ClientHandler tmp : client)
								{
								
									if(tmp.getGameID() == this.gameID && tmp.getMyplayer() == nextPlayer)
									{
										nextPlayerString = tmp.getName();
										break;
									}
								}
								
								botMove.setNextTurnPlayerName(nextPlayerString);
								try {
									this.sendMessageToGame(botMove);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							} catch (BadPlayerException e) {
								System.out.println("exc2");
							}
						}
					}
				}
			}
		}
	}


}
