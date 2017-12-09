package Client;

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketClient {

	public static void main(String[] args) {
		
		
		
		      Socket socket;
			try {
				socket = new Socket("localhost", 8189);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				Scanner in = new Scanner(socket.getInputStream());
				Scanner inn = new Scanner(System.in);
				while(in.hasNextLine()){
					String line = in.nextLine();
					System.out.println(line);
					String line2 = inn.nextLine();
					out.println(line2);
					
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      

	}

}