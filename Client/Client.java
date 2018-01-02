package Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import Client.GUIScreens.GameScreen;
import Message.*;

public class Client
{
	private Socket socket;
	private ServerListener serverLisener;
	private ObjectOutputStream outStream;
	private String localhost;
	private int port;

	public Client()
	{
		localhost = "localhost";
		port = 8189;
	}

	public Client(String localhost, int adress)
	{
		this.localhost = localhost;
		this.port = adress;
	}
	public void connectServer() throws UnknownHostException, IOException
	{
		socket = new Socket(localhost, port);
		outStream = new ObjectOutputStream ( socket.getOutputStream());
	}

	public void startServerListener(GameScreen game)
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

	public String getLocalhost()
	{
		return localhost;
	}

	public int getPort()
	{
		return port;
	}
}
