package Server;

import java.awt.BorderLayout;
import java.awt.List;
import java.util.ArrayList;

import javax.swing.*;

public class ServerFrame extends JFrame
{
	private JButton exitButton;
	private JButton startButton;
	private JButton clientButton;
	private ThreadedServer myServer;
	private Thread serverThread;
	private JTextArea messages;
	
	
	public ServerFrame()
	{
		myServer = ThreadedServer.getInstance();
		exitButton = new JButton("Exit");
		startButton = new JButton("Start");
		clientButton = new JButton("Client name");
		
		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);
		
		northPanel.add(clientButton);
		northPanel.add(startButton);
		northPanel.add(exitButton);
		
		startButton.setEnabled(true);
		exitButton.setEnabled(false);
		clientButton.setEnabled(false);
		
		messages = new JTextArea(20 , 60);
		add(new JScrollPane(messages));
		
		startButton.addActionListener(event -> 
		{
			startButton.setEnabled(false);
			exitButton.setEnabled(true);
			clientButton.setEnabled(true);
			
			messages.append("Uruchomiono server!");
			
			Runnable r = myServer;
			serverThread = new Thread(r);
			serverThread.start();
		});
		
		clientButton.addActionListener(event ->
		{
			ArrayList<String> myList = myServer.getClientName();
			
			for (String tmp : myList) 
			{
				messages.append(tmp);
			}
		});
		
		
		
		pack();
	}

}
