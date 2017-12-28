package Tests;

import Client.SocketClient;
import Server.ThreadedServer;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Kacper on 2017-12-20.
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
			new SocketClient();
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

	@Test(timeout=1000)
	public void getClientName() throws Exception {
		assertNotNull(server.getClientName().get(0));
	}
}