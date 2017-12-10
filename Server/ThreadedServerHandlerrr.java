package Server;
import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedServerHandler implements Runnable {
	
	private Socket coming;
	private String name;

	public ThreadedServerHandler(Socket coming, String name) {
		
		this.coming = coming;
		this.name = name;
	}
	
	
	@Override
	public void run() {
		
		
		try {
			InputStream inStream = coming.getInputStream();
			OutputStream outStream = coming.getOutputStream();
			
			Scanner in = new Scanner(inStream);
			PrintWriter out = new PrintWriter(outStream, true);
			
			out.println("Witaj " + name + " co u ciebie ?");

			while(in.hasNextLine()) {
				String line = in.nextLine();
				out.println(name + "powiedzia³: " + line);
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	

}
