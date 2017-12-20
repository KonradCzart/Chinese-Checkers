package Server;

import java.io.*;
import java.net.*;
import java.util.*;


public class ThreadedServer  implements Runnable
{
	private ArrayList<ClientHandler> client;
	private static ThreadedServer instance;
	
	
	private ThreadedServer()
	{
		client = new ArrayList<ClientHandler>();
	}
	
	public static ThreadedServer getInstance()
	{
		 if(instance == null) 
		 {
			 instance = new ThreadedServer();
	     }     
		 return instance;
	}
	
	public ArrayList getClientName()
	{
		return client;
	}
	
	@Override
	public void run() 
	{
		try 
		{
			ServerSocket server = new ServerSocket(8189);
			int i = 1;
			
			while(true) 
			{
				Socket coming = server.accept();
				
				ClientHandler newClient = new ClientHandler(coming, "usr " + i);
				client.add(newClient);
				Runnable r = newClient;
				Thread t = new Thread(r);
				t.start();
				i++;
			}
			
		}
		catch(IOException e) 
		{			
			e.printStackTrace();
		}		
	}
	
	private class ClientHandler implements Runnable 
	{
		
		private Socket coming;
		private String name;

		public ClientHandler(Socket coming, String name) 
		{
			
			this.coming = coming;
			this.name = name;
			
		}
		public Socket getSocket()
		{
			return coming;
		}
		
		public OutputStream getOutputStream() throws IOException
		{
			return coming.getOutputStream();
		}

		@Override
		public String toString()
		{
			return name;
		}
		
		@Override
		public void run() {
			
			
			try 
			{
				InputStream inStream = coming.getInputStream();
				OutputStream outStream = coming.getOutputStream();
				
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);
				
				out.println("Witaj " + name + " co u ciebie ?");

				while(in.hasNextLine()) 
				{
					String line = in.nextLine();
					for (ClientHandler tmp : client) 
					{
						OutputStream outStreamTmp = tmp.getOutputStream();
						PrintWriter out2 = new PrintWriter(outStreamTmp, true);
						out2.println( name + " napisal do wszystkich : >" + line + "\n");
					}
					
				}	
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}


}
