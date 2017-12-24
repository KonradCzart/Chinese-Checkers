package Client;

import java.io.*;
import java.net.*;
import java.util.*;

import Message.ChatMessage;

public class SocketClient {

	/*
	 * 
	 * klasa do usuniecia musia³em stworzyæ typowe klase klienta aby ³atwiej mo¿a by³o z nia popracowaæ
	 * komentarze bêd¹ dodane dzisiaj nie mam czasu 
	 * zrobione przesylanie obiektów message w których bêd¹ znajdowa³y siê konkretne pola dla konkretnej wiadomoœci np wiadomoœæ
	 * move bêdzie do ruchu a chat do przesy³ania wiadomoœci miêdzy klientem a serverem
	 * 
	 * 
	 * 
	 */
//	public static void main(String[] args) {
//
//		new ClientGUI();
//			Socket socket;
//			ObjectOutputStream outStream ;
//			
//			try {
//				socket = new Socket("localhost", 8189);
//				//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//				Scanner inn = new Scanner(System.in);
//				
//				Runnable r = new ServerListener(socket);
//				Thread t = new Thread(r);
//				t.start();
//				
//				outStream = new ObjectOutputStream ( socket.getOutputStream());
//				
//				
//			while(true)
//			{
//					
//				String line2 = inn.nextLine();
//				
//				
//				if(line2.equals("chat"))
//				{
//					line2 = inn.nextLine();
//					ChatMessage newMessage = new ChatMessage(line2);
//					outStream.writeObject(newMessage);
//					//out.println(line2);
//				}
//				
//				
//			}
//	
//			} 
//			catch (UnknownHostException e) {
//
//			} 
//			catch (Exception e) {
//
//				System.out.println("Koniedc!");
//			}
//			
//		      
//		System.out.println("Koniec!");
//
//	}

}