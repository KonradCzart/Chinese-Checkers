package Client.GUIScreens;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import Client.Client;
import Message.ChatMessage;
import Message.NewNameMessage;

/**
 * Created by Kacper on 2017-12-29.
 */
public class GameScreen
{
	private Scene scene;
	private String playerName;
	private Stage stage;
	private HBox hbox;
	private TextField chatField;
	private Button sendChatButton;
	private Button joinGameButton;
	private Button createGameButton;
	private Client myClient;
	private VBox chatBox;
	private ScrollPane scrollPane;
	private boolean isChatCreated;

	GameScreen(String playerName, Stage stage, Client myClient)
	{
		this.stage = stage;
		this.playerName = playerName;
		//create client and send message with new name
		this.myClient = myClient;
		this.myClient.startServerLisener(this);
		NewNameMessage newMessage = new NewNameMessage(playerName);
		try {
			this.myClient.sendMessage(newMessage);
		} catch (IOException e) {
			System.out.println("fail!");
		}

		Platform.setImplicitExit(false);
		isChatCreated = false;
		hbox = new HBox();
		load();

	}

	private void load()
	{
		BorderPane border = new BorderPane();

		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		Menu menuInfo = new Menu("Info");
		MenuItem changeServer = new MenuItem(("Change Server"));
		changeServer.setOnAction(t -> changeServer());
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(t -> System.exit(0));
		menuFile.getItems().addAll(changeServer, new SeparatorMenuItem(), exit);
		menuBar.getMenus().addAll(menuFile, menuInfo);

		hbox.getChildren().add(menuBar);
		hbox = addHBox();

		border.setTop(hbox);
//		border.setLeft(addVBox());

		addStackPane(hbox);
		border.setCenter(addBoardPane());
		border.setRight(addBorderPane());

		scene = new Scene(border, 924, 668);
		stage.setScene(scene);
		stage.setMinWidth(1024);
		stage.setMinHeight(768);
		stage.show();
	}

	private HBox addHBox()
	{
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");

		joinGameButton = new Button("Join game");
		createGameButton = new Button("Create game");

		joinGameButton.setPrefSize(100, 20);
		createGameButton.setPrefSize(100, 20);

		joinGameButton.setOnAction(event ->
		{
			errorDialog("Jeszcze nie okodowano");
		});
		createGameButton.setOnAction(event ->
		{
			errorDialog("Przycisk createGame");
		});


		hbox.getChildren().addAll(joinGameButton, createGameButton);

		return hbox;
	}

	private void errorDialog(String errorMessage)
	{
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("");
		alert.setContentText(errorMessage);

		alert.showAndWait();
	}

	private void changeServer()
	{
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Connect to server");
		dialog.setHeaderText("");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Reconnect", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField hostname = new TextField();
		hostname.setPromptText("hostname");
		TextField port = new TextField();
		port.setPromptText("port");

		grid.add(new Label("Hostname: "), 0, 0);
		grid.add(hostname, 1, 0);
		grid.add(new Label("Port: "), 0, 1);
		grid.add(port, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		hostname.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> hostname.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(hostname.getText(), port.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> new LoadingScreen(stage));
	}

	private void addStackPane(HBox hb)
	{
		StackPane stack = new StackPane();
		Text helpText = new Text("You are connected!");
		helpText.setFill(Color.GREEN);
		helpText.setStroke(Color.web("#FFFFFF"));

		stack.getChildren().addAll(helpText);
		stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
		StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

		hb.getChildren().add(stack);            // Add to HBox from Example 1-2
		HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
	}

	private BorderPane addBorderPane()
	{
		BorderPane flow = new BorderPane();
		flow.setPadding(new Insets(5, 5, 10, 5));
		flow.setMinWidth(240);
		flow.setMaxWidth(240);
		flow.setStyle("-fx-background-color: DAE6F3;");

		chatBox = new VBox();
		isChatCreated = true;
		scrollPane = new ScrollPane();
		scrollPane.setContent(chatBox);
		scrollPane.setStyle(" -fx-background: #F0F5FA; -fx-border-color: #F0F5FA;");
		scrollPane.setMaxHeight(620);
		scrollPane.setMaxWidth(240);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.vvalueProperty().bind(chatBox.heightProperty());

		chatField = new TextField();
		sendChatButton = new Button("Send");
		sendChatButton.setOnAction(event ->
		{
			String message = chatField.getText();
			if(!message.equals(""))
			{
				String line = chatField.getText();
				ChatMessage newMessage = new ChatMessage(line);
				try
				{
					myClient.sendMessage(newMessage);
					chatField.setText("");
				}
				catch (IOException e)
				{
					System.out.println("Send message fail");
				}
			}
		});

		chatBox.setPadding(new Insets(10));
		chatBox.setSpacing(8);
		Text title = new Text("CHAT");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		chatBox.getChildren().add(title);
		flow.setTop(scrollPane);

		HBox tmpHBox= new HBox();
		tmpHBox.setSpacing(15);
		tmpHBox.setAlignment(Pos.CENTER);
		tmpHBox.getChildren().addAll(chatField, sendChatButton);

		flow.setBottom(tmpHBox);

		return flow;
	}

	public GridPane addBoardPane()
	{
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		//grid.setHgap(10);
		//grid.setVgap(10);
		//grid.setPadding(new Insets(0, 10, 0, 10));

//		Text chartTitle = new Text("Current Year");
//		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		createBoard(gridPane);
//		grid.add(chartTitle, 0, 0);

		return gridPane;
	}

	private void createBoard(GridPane grid)
	{
		Random rand = new Random();
		Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED};

		for (int row = 0; row < 19; row++)
		{
			for (int col = 0; col < 19; col++)
			{
				int n = rand.nextInt(4);
				Rectangle rec = new Rectangle();
				rec.setWidth(30);
				rec.setHeight(30);
				rec.setFill(colors[n]);
				GridPane.setRowIndex(rec, row);
				GridPane.setColumnIndex(rec, col);
				grid.getChildren().addAll(rec);
			}
		}
	}

	private void addTextToChat(Text chatMessage)
	{
		Platform.runLater(() -> {
			VBox.setMargin(chatMessage, new Insets(0, 0, 0, 8));
			chatBox.getChildren().add(chatMessage);
		});
	}

	public void sendToChat(String chat)
	{
		if(isChatCreated)
			addTextToChat(new Text(chat));
	}
}
