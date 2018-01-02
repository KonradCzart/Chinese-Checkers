package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import Client.GUIScreens.GameScreen;
import Game.ColorPlayer;
import Message.*;

public class ServerListener implements Runnable
{
	private Socket currentSocket;
	private GameScreen game;
	
	public ServerListener(Socket socket, GameScreen game)
	{
		currentSocket = socket;
		this.game = game;

	}
	@Override
	public void run() {
		
		
		try {
			
			
			ObjectInputStream in = new ObjectInputStream(currentSocket.getInputStream());

			Object tmp;
			while((tmp = in.readObject()) != null)
			{
				
				if(tmp instanceof ChatMessage)
				{
					ChatMessage chat = (ChatMessage) tmp;
					String line = chat.getDescription();			
					//System.out.println(line);
					game.sendToChat(line);

				}
				else if(tmp instanceof FailMessage)
				{
					FailMessage fail = (FailMessage) tmp;
					String line = fail.getDescription();
					game.errorDialog(line);
				}
				else if(tmp instanceof SuccessMessage)
				{
					
					SuccessMessage success = (SuccessMessage) tmp;
					
					if(success.getCodSuccess() == 22222)
					{
						game.removeGame();
					}
					else
					{
						String line = success.getDescription();
						game.successDialog(line);
					}
				}
				else if(tmp instanceof AddPawnMessage)
				{
					AddPawnMessage addPawn = (AddPawnMessage) tmp;
					game.addPawn(addPawn.getPlayer(), addPawn.getMyX(), addPawn.getMyY());
				}
				else if(tmp instanceof MoveMessage)
				{
					MoveMessage movePawn = (MoveMessage) tmp;
					
					ColorPlayer nextMovePlayer;
					String nameNextPlayer;
					
					if(movePawn.getEndTurn())
					{
						nextMovePlayer = movePawn.getNextMovePlayer();
						nameNextPlayer = movePawn.getNextTurnPlayerName();
						
						game.getPlayerNameColored(nameNextPlayer, nextMovePlayer);
					}
					else
					{
						int oldX, oldY, newX, newY;
						oldX = movePawn.getOldX();
						oldY = movePawn.getOldY();
						newX = movePawn.getNewX();
						newY = movePawn.getNewY();
						ColorPlayer movePlayer = movePawn.getMovePlayer();
					
						game.movePawn(oldX, oldY, newX, newY, movePlayer);
						
						nextMovePlayer = movePawn.getNextMovePlayer();
						nameNextPlayer = movePawn.getNextTurnPlayerName();
						
						game.getPlayerNameColored(nameNextPlayer, nextMovePlayer);
					}
				}
				
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("End of listener");
	}

}
