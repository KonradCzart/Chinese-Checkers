package Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import Message.*;
import Server.ThreadedServer;

public class Client implements Observer
{
	private Socket socket;
	private ServerListener serverLisener;
	private ObjectOutputStream outStream;
	private Observable observable;
	
	public Client(Observable observable)
	{
		this.observable = observable;
		observable.addObserver(this);
	}
	
	public void connectServer()
	{
		try {
			socket = new Socket("localhost", 8189);
			serverLisener = new ServerListener(socket);
			
			Runnable r = serverLisener;
			Thread t = new Thread(r);
			t.start();
			
			outStream = new ObjectOutputStream ( socket.getOutputStream());
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message newMessage) throws IOException
	{
		outStream.writeObject(newMessage);
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		if(o instanceof ThreadedServer)
		{
			ThreadedServer th = (ThreadedServer) o;
			//th.getState()
			//do sth..
		}
	}
}
