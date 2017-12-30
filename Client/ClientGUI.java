package Client;

import Client.GUIScreens.LoadingScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 *  Class runs GUI and switch to LoadingScreen
 */
public class ClientGUI extends Application
{
	private static String hostname;
	private static int port;
	private static boolean defaultConnection;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("Chinese Checkers Game");
		primaryStage.setMinHeight(450);
		primaryStage.setMinWidth(850);

		if(!defaultConnection)
			new LoadingScreen(primaryStage, hostname, port);
		else
			new LoadingScreen(primaryStage);

		primaryStage.show();
	}

	public static void main(String args[])
	{
		defaultConnection = true;

		if(args.length == 2)
		{
			defaultConnection = false;
			hostname = args[0];
			try
			{
				port = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e)
			{
				defaultConnection = true;
			}
		}
		launch(args);
	}
}

