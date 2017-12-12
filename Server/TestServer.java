package Server;

import java.awt.*;

import javax.swing.*;

public class TestServer {

	public static void main(String[] args) 
	{
		EventQueue.invokeLater( () ->     
			{           
			              
				JFrame frame = new ServerFrame();               
				frame.setTitle("SizedFrame");               
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);              
				frame.setVisible(true);                   
			});

	}

}
