package Client.GUIScreens;

import java.io.IOException;

import Client.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Loading Screen for user
 * It allows to connect to the server, change the server and input name
 */
public class LoadingScreen
{
	private static final int DEFAULT_PORT = 8189;
	private static final String DEFAULT_HOSTNAME = "localhost";
	private static final int WINDOW_WIDTH = 850;
	private static final int WINDOW_HEIGHT = 450;

	private String hostname;
	private int port;
	private Client myClient;
	private GridPane grid;
	private Label connectingText;
	private Scene scene;
	private Stage stage;
	private String playerName;
	private TextField userTextField;

	/**
	 * Creates Loading screen GUI with default port and hostname
	 * @param primaryStage javaFX Stage
	 */
	public LoadingScreen(Stage primaryStage)
	{
		this(primaryStage, DEFAULT_HOSTNAME, DEFAULT_PORT);
	}

	/**
	 * Creates Loading screen GUI with given port and hostname
	 * @param primaryStage javaFX Stage
	 * @param hostname address of server
	 * @param port server's port
	 */
	public LoadingScreen(Stage primaryStage, String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
		this.stage = primaryStage;

		load();

		scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
		stage.setScene(scene);

		stage.setOnCloseRequest(e ->
		{
			Platform.exit();
			System.exit(0);
		});

		myClient = new Client(hostname, port);
	}

	/**
	 * This method creates a content: container, buttons, textfield
	 */
	private void load()
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

		Text welcomeMessage = new Text("Welcome!");
		welcomeMessage.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		welcomeMessage.setFill(Color.WHITE);

		userTextField = new TextField();
		userTextField.setPromptText("login");
		userTextField.setOnKeyPressed(event ->
		{
			if(event.getCode() == KeyCode.ENTER)
				login();
		});

		Button btn = new Button("Sign in");
		btn.setOnAction(event -> login());

		Button serverChangeButton = new Button("Options");
		serverChangeButton.setOnAction(event -> new ChangeServer(stage));
		serverChangeButton.setAlignment(Pos.BOTTOM_RIGHT);

		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().addAll(serverChangeButton, btn);

		connectingText = new Label();

		grid.add(text, 0, 2);
		grid.add(welcomeMessage, 1, 3, 2, 1);
		grid.add(userTextField, 1, 4);
		grid.add(hbBtn, 1, 5);
		grid.add(connectingText, 1, 9);
		grid.setBackground(new Background(new BackgroundFill(Color.valueOf("#2c2f33"), CornerRadii.EMPTY, Insets.EMPTY)));
	}

	/**
	 * Tries log to game, updates connection label and validates player name.
	 */
	private void login()
	{
		String name = userTextField.getText();

		if (name.matches("[\\w]+"))
		{
			Thread thread = new Thread(() ->
			{
				try
				{
					Platform.runLater(() ->
					{
						connectingText.setTextFill(Color.GREEN);
						connectingText.setText("Connecting...");
					});
					Thread.sleep(300);
					Platform.runLater(() ->
					{
						playerName = name;
						try
						{
							myClient.connectServer();
							new GameScreen(playerName, stage, myClient);
						} catch (IOException e) {
							connectingText.setTextFill(Color.RED);
							connectingText.setText("Connection failed.");
						}
					});
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			});
			thread.start();
		}
		else
		{
			connectingText.setText("");

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Your name is invalid. Use only alphanumeric characters");

			alert.showAndWait();
		}
	}
}
