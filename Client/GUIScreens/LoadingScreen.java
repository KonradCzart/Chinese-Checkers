package Client.GUIScreens;

import java.io.IOException;

import Client.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Loading Screen for user
 */
public class LoadingScreen
{
	private Stage stage;
	private Scene scene;
	private GridPane grid;
	private Button btn;
	private TextField userTextField;
	private Text connectingText;
	private String playerName;
	private String hostname;
	private int port;
	private Client myClient;

	public LoadingScreen(Stage primaryStage)
	{
		this(primaryStage, "localhost", 8189);
	}

	public LoadingScreen(Stage primaryStage, String hostname, int port)
	{
		load();
		stage = primaryStage;
		scene = new Scene(grid, 850, 450);
		stage.setScene(scene);
		this.hostname = hostname;
		this.port = port;

		stage.setOnCloseRequest(e ->
		{
			myClient.closeConnection();
			Platform.exit();
			System.exit(0);
		});

		myClient = new Client(hostname,port);
	}

	public void load()
	{
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text text = new Text("Ch!nese Checkers    ");
		text.setFont(Font.font("Edwardian Script ITC", 80));

		text.setX(155);
		text.setY(50);

		text.setFill(Color.BEIGE);
		text.setStrokeWidth(2);
		text.setStroke(Color.DARKSLATEBLUE);
		grid.add(text, 0, 2);

		Text welcomeMessage = new Text("Welcome!");
		welcomeMessage.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		welcomeMessage.setFill(Color.WHITE);
		grid.add(welcomeMessage, 1, 3, 2, 1);

		userTextField = new TextField();
		userTextField.setPromptText("login");
		userTextField.setOnKeyPressed(event ->
		{
			if(event.getCode() == KeyCode.ENTER)
				login();
		});
		grid.add(userTextField, 1, 4);

		btn = new Button("Sign in");
		btn.setOnAction(event -> login());


		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 5);

		connectingText = new Text();
		grid.add(connectingText, 1, 7);

		grid.setBackground(new Background(new BackgroundFill(Color.valueOf("#2c2f33"), CornerRadii.EMPTY, Insets.EMPTY)));
	}

	/**
	 * Try Log to game and validates player name.
	 */
	private void login()
	{
		String name = userTextField.getText();

		if (name.matches("[\\w]+"))
		{
			connectingText.setFill(Color.GREEN);
			connectingText.setText("Connecting...");
			connectingText.isVisible();
			playerName = name;

			try
			{
				myClient.connectServer();
				new GameScreen(playerName, stage, myClient);
			}
			catch (IOException e) {
				connectingText.setFill(Color.RED);
				connectingText.setText("Connection failed.");
			}

		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Your name is invalid. Use only alphanumeric characters");

			alert.showAndWait();
		}
	}

}
