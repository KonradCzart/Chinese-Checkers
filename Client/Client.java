package Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

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
	
	public void connectServer()
	{
		try {
			socket = new Socket("localhost", 8189);
			serverLisener = new ServerListener(socket);
			
			Runnable r = serverLisener;
			Thread t = new Thread(r);
			t.start();
			
			outStream = new ObjectOutputStream ( socket.getOutputStream());
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message newMessage) throws IOException
	{
		outStream.writeObject(newMessage);
	}
}
