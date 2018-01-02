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

	private static final int DEFAULT_PORT = 8189;
	private static final String DEFAULT_HOSTNAME = "localhost";

	public Client()
	{
		localhost = DEFAULT_HOSTNAME;
		port = DEFAULT_PORT;
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
