package Tests;

import Client.Client;
import Server.ThreadedServer;
import javafx.application.Platform;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * This class is testing server's methods
 */
public class ThreadedServerTest {

	ThreadedServer server;
	Thread serverThread;

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

	@After
	public void tearDown()
	{
		server = null;
	}

	@Test
	public void getInstance() throws Exception
	{
		assertNotNull(server);
	}

	@Test
	public void getClientName() throws Exception
	{
		Thread thread = new Thread(() ->
		{
			try
			{
				Platform.runLater(() ->
				{
					Client me = new Client();
					try {
						me.connectServer();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				Thread.sleep(1000);
				Platform.runLater(() ->
				{
					assertNotNull(server.getClientName().get(0));
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