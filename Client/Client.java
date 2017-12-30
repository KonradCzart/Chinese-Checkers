package Client;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import Client.GUIScreens.GameScreen;
import Message.*;
import Server.ThreadedServer;
import javafx.scene.shape.Circle;

public class Client
{
	private Socket socket;
	private ServerListener serverLisener;
	private ObjectOutputStream outStream;
	private String localHost;
	private int adress;

	
	public Client()
	{
		localHost = "localhost";
		adress = 8189;
	}
	public Client(String localHost, int adress)
	{
		this.localHost = localHost;
		this.adress = adress;
	}
	
	public void connectServer() throws UnknownHostException, IOException
	{
		socket = new Socket(localHost, adress);
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

	public void closeConnection()
	{
//		try
//		{
//			outStream.close();
//			socket.close();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
	}
}
