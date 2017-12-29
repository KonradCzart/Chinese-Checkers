package Client;

import Client.GUIScreens.LoadingScreen;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *  Class runs GUI and switch to LoadingScreen
 */
public class ClientGUI extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("Chinese Checkers Game");
		primaryStage.setMinHeight(450);
		primaryStage.setMinWidth(850);

		new LoadingScreen(primaryStage);

		primaryStage.show();
	}

	public static void main(String args[])
	{
		launch(args);
	}
}

