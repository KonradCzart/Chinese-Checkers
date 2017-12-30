package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Scanner;

import Client.GUIScreens.GameScreen;
import Game.ColorPlayer;
import Message.*;

public class ServerListener implements Runnable
{
	Socket currentSocket;
	Object tmp;
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
					String line = success.getDescription();
					game.successDialog(line);
				}
				else if(tmp instanceof AddPawnMessage)
				{
					AddPawnMessage addPawn = (AddPawnMessage) tmp;
					game.addPawn(addPawn.getPlayer(), addPawn.getMyX(), addPawn.getMyY());
				}
				else if(tmp instanceof MoveMessage)
				{
					MoveMessage movePawn = (MoveMessage) tmp;
					int oldX, oldY, newX, newY;
					oldX = movePawn.getOldX();
					oldY = movePawn.getOldY();
					newX = movePawn.getNewX();
					newY = movePawn.getNewY();
					ColorPlayer movePlayer = movePawn.getMovePlayer();
					
					game.movePawn(oldX, oldY, newX, newY, movePlayer);
				}
				
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Koniec lisenera");
	}

}
