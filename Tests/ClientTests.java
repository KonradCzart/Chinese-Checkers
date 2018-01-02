package Tests;

import Client.*;
import Message.*;
import Server.ThreadedServer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class is testing Client package classes methods
 */
public class ClientTests
{
	private ThreadedServer server;
	private Client firstClient;
	private Client secondClient;

	private static final int DEFAULT_PORT = 8189;
	private static final String DEFAULT_HOSTNAME = "localhost";

	@Before
	public void setUp()
	{
		JFXPanel fxPanel = new JFXPanel();

		Thread thread = new Thread(() ->
		{
			try
			{
				firstClient = new Client();
				secondClient = new Client();
				Thread.sleep(1000);
				Platform.runLater(() ->
				{
					try
					{
						secondClient.connectServer();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				});
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
		thread.start();

		server = ThreadedServer.getInstance();
		if(server.isThreadedServerRun())
		{
			Runnable r = server;
			Thread serverThread = new Thread(r);
			serverThread.start();
		}
	}

	@Test
	public void connectToServer()
	{
		Platform.runLater(() ->
		{
			try
			{
				firstClient.connectServer();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}

	@Test
	public void sendMessage()
	{
		Thread thread = new Thread(() ->
		{
			try
			{
				Thread.sleep(1000);
				Platform.runLater(() ->
				{
					try
					{
						secondClient.sendMessage(new ChatMessage("Hello"));
					}
					catch (IOException e)
					{
						fail();
					}
				});
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
		thread.start();
	}

	@Test
	public void getHostname()
	{
		Thread thread = new Thread(() ->
		{
			try
			{
				Thread.sleep(1000);
				Platform.runLater(() ->
				{
					if(secondClient.getPort() == DEFAULT_PORT)
						assertTrue(true);
				});
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
		thread.start();
	}

	@Test
	public void getPort()
	{
		Thread thread = new Thread(() ->
		{
			try
			{
				Thread.sleep(1000);
				Platform.runLater(() ->
				{
					if(secondClient.getLocalhost().equals(DEFAULT_HOSTNAME))
						assertTrue(true);
				});
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
		thread.start();

	}
}
