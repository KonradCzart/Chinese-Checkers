package Client;

import java.io.*;
import java.net.*;
import java.util.*;

import Message.ChatMessage;

public class SocketClient {

	/*
	 * 
	 * klasa do usuniecia musia�em stworzy� typowe klase klienta aby �atwiej mo�a by�o z nia popracowa�
	 * komentarze b�d� dodane dzisiaj nie mam czasu 
	 * zrobione przesylanie obiekt�w message w kt�rych b�d� znajdowa�y si� konkretne pola dla konkretnej wiadomo�ci np wiadomo��
	 * move b�dzie do ruchu a chat do przesy�ania wiadomo�ci mi�dzy klientem a serverem
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