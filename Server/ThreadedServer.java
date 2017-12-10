package Server;

import java.io.*;
import java.net.*;
import java.util.*;


public class ThreadedServer  implements Runnable
{
	private ArrayList<String> client;
	private static ThreadedServer instance;
	
	
	private ThreadedServer()
	{
		client = new ArrayList<String>();
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
				
				//System.out.println("Po³¹czono" + i);
				
				Runnable r = new ThreadedServerHandler(coming, "usr " + i);
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
	
	private class ThreadedServerHandler implements Runnable 
	{
		
		private Socket coming;
		private String name;

		public ThreadedServerHandler(Socket coming, String name) 
		{
			
			this.coming = coming;
			this.name = name;
			client.add(name);
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
					out.println(name + "powiedzia³: " + line);
					
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
