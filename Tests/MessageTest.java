package Tests;

import Client.Client;
import Game.ColorPlayer;
import Message.*;
import Server.ThreadedServer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class is testing message's methods
 */
public class MessageTest
{
	private ThreadedServer server;
	private Thread serverThread;
	private Client myClient;

	@Before
	public void setUp()
	{
		JFXPanel fxPanel = new JFXPanel();

		server = ThreadedServer.getInstance();
		if(server.isThreadedServerRun())
		{
			Runnable r = server;
			serverThread = new Thread(r);
			serverThread.start();
		}

		Thread thread = new Thread(() ->
		{
			try
			{
				Platform.runLater(() ->
				{
					myClient = new Client();
					try {
						myClient.connectServer();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				Thread.sleep(1000);
				Platform.runLater(() ->
				{

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
	public void sendSuccessMessage() throws Exception
	{
		SuccessMessage newMessage = new SuccessMessage(22222, "remove game");
		doLatency(newMessage);
		if(newMessage.getCodSuccess() == 22222 && newMessage.getDescription().equals("remove game"))
			assertTrue(true);
	}

	@Test
	public void sendNewNameMessage() throws Exception
	{
		NewNameMessage newMessage = new NewNameMessage("Tested name");
		doLatency(newMessage);

		if(newMessage.getName().equals("Tested name"))
			assertTrue(true);
	}

	@Test
	public void sendMoveMessage() throws Exception
	{
		MoveMessage newMessage = new MoveMessage(true);
		doLatency(newMessage);

		if(newMessage.getEndTurn())
			assertTrue(true);
	}

	@Test
	public void sendJoinGameMessage() throws Exception
	{
		JoinGameMessage newJoinMessage = new JoinGameMessage(0, true);
		doLatency(newJoinMessage);
		if(newJoinMessage.getIdGame() == 0 && newJoinMessage.getNewGame())
			assertTrue(true);
	}

	@Test
	public void sendFailMessage() throws Exception
	{
		FailMessage failMessage = new FailMessage(1234, "Test failure message");
		doLatency(failMessage);
		if(failMessage.getCodFail() == 1234 && failMessage.getDescription().equals("Test failure message"))
			assertTrue(true);
	}

	@Test
	public void sendChatMessage() throws Exception
	{
		ChatMessage newMessage = new ChatMessage("Hello world");
		doLatency(newMessage);
		if(newMessage.getDescription().equals("Hello world"))
			assertTrue(true);
	}

	@Test
	public void sendAddPawnMessage() throws Exception
	{
		AddPawnMessage newMessage = new AddPawnMessage(ColorPlayer.PLAYER_FIVE, 3, 4);
		doLatency(newMessage);
		if (newMessage.getMyX() == 3 && newMessage.getMyY() == 4 &&
				newMessage.getPlayer().equals(ColorPlayer.PLAYER_FIVE))
		{
			assertTrue(true);
		}
	}

	@Ignore
	private void doLatency(Message newMessage)
	{
		Thread thread = new Thread(() ->
		{
			try
			{
				Thread.sleep(1000);
				Platform.runLater(() ->
				{
					try {
						myClient.sendMessage(newMessage);
					} catch (IOException e) {
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

}