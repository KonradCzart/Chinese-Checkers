package Tests;

import Client.SocketClient;
import Server.ThreadedServer;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Kacper on 2017-12-20.
 */
public class ThreadedServerTest {

	ThreadedServer server;

	@Before
	public void setUp()
	{
		Thread serverThread;
		server = ThreadedServer.getInstance();
		Runnable r = server;
		serverThread = new Thread(r);
		serverThread.start();
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
	public void getClientName() throws Exception {
		new SocketClient();
		Assert.assertNotNull(server.getClientName().get(0));
	}

	@Test
	public void run() throws Exception {
	}

}