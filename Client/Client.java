package Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import Client.GUIScreens.GameScreen;
import Message.*;
import Server.ThreadedServer;

public class Client
{

	private Socket socket;
	private ServerListener serverLisener;
	private ObjectOutputStream outStream;
	// pppp

	
	public Client()
	{

	}
	
	public void connectServer() throws UnknownHostException, IOException
	{
		
			socket = new Socket("localhost", 8189);
		
			outStream = new ObjectOutputStream ( socket.getOutputStream());
	}
	
	public void startServerLisener(GameScreen game)
	{
		serverLisener = new ServerListener(socket, game);
		
		Runnable r = serverLisener;
		Thread t = new Thread(r);
		t.start();
		
	}
	
	public void sendMessage(Message newMessage) throws IOException
	{
		outStream.writeObject(newMessage);
	}
}
