package Server;
import java.io.*;
import java.net.*;

public class ThreadedServer {

	public static void main(String[] args) {

		try {
			
			ServerSocket server = new ServerSocket(8189);
			int i = 1;
			
			while(true) {
				Socket coming = server.accept();
				
				System.out.println("Po³¹czono" + i);
				
				Runnable r = new ThreadedServerHandler(coming, "name " + i);
				Thread t = new Thread(r);
				t.start();
				i++;
				
				
				
			}
			
		}
		catch(IOException e) {			
			e.printStackTrace();
		}

	}

}
