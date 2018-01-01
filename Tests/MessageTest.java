package Tests;

import Client.Client;
import Message.SuccessMessage;
import Server.ThreadedServer;
import org.junit.*;

import java.io.IOException;

/**
 * Created by Kacper on 2018-01-01.
 */
public class MessageTest
{
	ThreadedServer server;
	Thread serverThread;
	Client myClient;

	@Before
	public void setUp()
	{
		server = ThreadedServer.getInstance();
		if(server.isThreadedServerRun())
		{
			Runnable r = server;
			serverThread = new Thread(r);
			serverThread.start();
		}
	}

	@Test
	public void getInstance() throws Exception
	{
		SuccessMessage newMessage = new SuccessMessage(22222, "remove game");

		try
		{
			myClient.sendMessage(newMessage);
		}
		catch (IOException e) {

		}
	}

}
