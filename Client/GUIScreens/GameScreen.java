package Client.GUIScreens;

import Game.BoardCircle;
import Game.ColorPlayer;
import Game.FieldStatus;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

import Client.Client;
import Message.*;


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
	private Button startGameButton;
	private Button endRoundButton;
	private Client myClient;
	private VBox chatBox;
	private ScrollPane scrollPane;
	private BoardCircle[][] tabField;
	private BoardCircle clickedBoardCircle;
	private Paint lastClickedColor;
	private Text playerNameText;
	private boolean isChatCreated;
	private boolean arePawnsAdded;
	private BorderPane border;

	GameScreen(String playerName, Stage stage, Client myClient)
	{
		this.stage = stage;
		this.playerName = playerName;
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
		arePawnsAdded = false;
		hbox = new HBox();
		tabField = new BoardCircle[19][19];
		createBoard();
		load();

	}

	private void load()
	{
		border = new BorderPane();

		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		Menu menuInfo = new Menu("Info");

		MenuItem createGame = new MenuItem(("Create the game"));
		createGame.setOnAction(event ->
		{
			JoinGameMessage newJoinMessage = new JoinGameMessage(0, true);
			try {
				myClient.sendMessage(newJoinMessage);
			} catch (IOException e) {
				errorDialog("no send message!");
			}
		});

		MenuItem joinGame = new MenuItem(("Join the game"));
		joinGame.setOnAction(event ->
		{
			gameIdDialog();
		});
		
		MenuItem removeGame = new MenuItem(("Remove game"));
		removeGame.setOnAction(event ->
		{
			SuccessMessage newMessage = new SuccessMessage(22222, "remove game");
			try {
				myClient.sendMessage(newMessage);
			} catch (IOException e) {
				
			}
			
			//this.paintBoard(grid);
		});
		
		MenuItem setName = new MenuItem(("Set name"));
		setName.setOnAction(event ->
		{
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Set your name!");
			dialog.setHeaderText("");
			dialog.setContentText("New name:");

			Optional<String> result = dialog.showAndWait();

			result.ifPresent(name ->
			{
				
				
				String lineName = result.get();

				if (lineName.matches("[\\w]+"))
				{
					NewNameMessage newMessage = new NewNameMessage(lineName);
					try {
						myClient.sendMessage(newMessage);
					} catch (IOException e) {
						errorDialog("no send message!");
					}
				}
				else
				{
					String errorLine = "Your name is invalid. Use only alphanumeric characters";
					errorDialog(errorLine);
				}
		
			});
		});
		
		MenuItem changeServer = new MenuItem(("Change Server"));
		changeServer.setOnAction(t -> changeServer());

		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(t -> System.exit(0));

		menuFile.getItems().addAll(joinGame, createGame, changeServer, setName, new SeparatorMenuItem(),removeGame, exit);
		menuBar.getMenus().addAll(menuFile, menuInfo);

		hbox.getChildren().add(menuBar);
		hbox = addHBox();

		border.setTop(hbox);
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

		playerNameText = new Text("Player");

		hbox.getChildren().addAll(playerNameText);

		return hbox;
	}

	public void removeGame()
	{
		
		Platform.runLater(() -> {
			this.createBoard();
			border.setCenter(addBoardPane());
		});

	}
	public void errorDialog(String errorMessage)
	{
		
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText(errorMessage);

			alert.showAndWait();
		});
		
	}
	
	public void successDialog(String successMessage)
	{
		
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Succes");
			alert.setHeaderText("");
			alert.setContentText(successMessage);

			alert.showAndWait();
		});
		
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

		result.ifPresent(connectionData ->
		{
			//TODO jakis try catch i errorMess
			int portData = Integer.parseInt(connectionData.getValue());
			new LoadingScreen(stage, connectionData.getKey(), portData);
		});
	}

	public void gameIdDialog()
	{
		TextInputDialog dialog = new TextInputDialog("1");
		dialog.setTitle("Change game ID");
		dialog.setHeaderText("");
		dialog.setContentText("Please enter game ID:");

		Optional<String> result = dialog.showAndWait();

		result.ifPresent(name ->
		{
			int value = Integer.parseInt(result.get());
			
			JoinGameMessage newJoinMessage = new JoinGameMessage(value, false);
			try {
				myClient.sendMessage(newJoinMessage);
			} catch (IOException e) {
				errorDialog("no send message!");
			}
		});
	}

	private void addStackPane(HBox hb)
	{
		HBox stack = new HBox();

		startGameButton = new Button("Start");
		endRoundButton = new Button("EndRound");

		startGameButton.setPrefSize(100, 20);
		endRoundButton.setPrefSize(100, 20);

		startGameButton.setOnAction(event ->
		{
			SuccessMessage newMessage = new SuccessMessage(11111, "start game");
			try {
				myClient.sendMessage(newMessage);
			} catch (IOException e) {
				
			}

		});
		endRoundButton.setOnAction(event ->
		{
			MoveMessage newMessage = new MoveMessage(true);
			try {
				myClient.sendMessage(newMessage);
			} catch (IOException e) {
				errorDialog("no send message!");
			}
		});

		stack.setSpacing(8);
		stack.getChildren().addAll(endRoundButton, startGameButton);
		stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
		StackPane.setMargin(startGameButton, new Insets(0, 10, 0, 0)); // Center "?"

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
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.vvalueProperty().bind(chatBox.heightProperty());

		chatField = new TextField();
		chatField.setOnKeyPressed(event ->
		{
			if(event.getCode() == KeyCode.ENTER)
				sendMessageToChat();
		});
		sendChatButton = new Button("Send");
		sendChatButton.setOnAction(event ->
		{
			sendMessageToChat();
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

	private void sendMessageToChat()
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
	}

	public GridPane addBoardPane()
	{
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setRotate(45);

		paintBoard(gridPane);
		//gridPane.setPadding(new Insets(10, 10, 10, 10));

//		Text chartTitle = new Text("Current Year");
//		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

//		grid.add(chartTitle, 0, 0);

		return gridPane;
	}

	private void paintBoard(GridPane grid)
	{
		FieldStatus currentStatus;

		for(int i=0; i<19; i++)
		{
			for(int j=0; j<19; j++)
			{
				currentStatus = tabField[i][j].getFieldStatus();

				if(currentStatus == FieldStatus.AVAILABLE)
				{
					BoardCircle c = tabField[i][j];
					c.setRadius(13);
					c.setX(i);
					c.setY(j);
					c.setFill(Color.valueOf("#FFFFFF"));
					GridPane.setRowIndex(c, i);
					GridPane.setColumnIndex(c, j);
					GridPane.setMargin(c, new Insets(7, 7, 7, 7));
					grid.getChildren().addAll(c);
					c.setOnMouseClicked(event -> onBoardCircleClick(c));
				}
				else if(currentStatus == FieldStatus.CLOSED)
				{
					//System.out.printf("_");
				}
				else if(currentStatus == FieldStatus.UNAVAILABLE)
				{
					//System.out.printf("8");
				}
			}
		}

	}


	private void onBoardCircleClick(BoardCircle circle)
	{
		if(arePawnsAdded)
		{
			if(clickedBoardCircle == null)
			{
				clickedBoardCircle = circle;
				lastClickedColor = clickedBoardCircle.getFill();
				clickedBoardCircle.setFill(Color.valueOf("#333333"));
			}
			else if(clickedBoardCircle == circle)
			{
				clickedBoardCircle.setFill(lastClickedColor);
				clickedBoardCircle = null;
			}
			else
			{
				// JESLI POLE JEST PIONKIEM TO WTEDY MOZNA TU COS ZROBIC
				// Czekaj na potwierdznie ruchu i wtedy rusz ten pionek tam..
//			move(clickedBoardCircle, circle);

				// jesli metoda move pozwoli to rusz
				//circle.setFill(lastClickedColor);
				clickedBoardCircle.setFill(lastClickedColor);
				int oldX, oldY, newX, newY;
				oldX = clickedBoardCircle.getX();
				oldY = clickedBoardCircle.getY();
				newX = circle.getX();
				newY = circle.getY();

				//this.movePawn(oldX, oldY, newX, newY);

				MoveMessage newMessage = new MoveMessage(oldX, oldY, newX, newY);
				try {
					myClient.sendMessage(newMessage);
				} catch (IOException e) {

				}

				clickedBoardCircle = null;
			}
		}
	}

	public void getPlayerNameColored(String name, ColorPlayer colorPlayer)
	{
		String line = "Now round: " + name;
		playerNameText.setText(line);

		switch (colorPlayer)
		{
			case PLAYER_ONE:
			{
				playerNameText.setFill(Color.valueOf("#DFFF00"));
				break;
			}
			case PLAYER_TWO:
			{
				playerNameText.setFill(Color.valueOf("#FF00FF"));
				break;
			}
			case PLAYER_THREE:
			{
				playerNameText.setFill(Color.valueOf("#00FFFF"));
				break;
			}
			case PLAYER_FOUR:
			{
				playerNameText.setFill(Color.valueOf("#AFAFAF"));
				break;
			}
			case PLAYER_FIVE:
			{
				playerNameText.setFill(Color.valueOf("#CAFAFA"));
				break;
			}
			case PLAYER_SIX:
			{
				playerNameText.setFill(Color.valueOf("#CCCCCC"));
				break;
			}
			case PLAYER_EMPTY:
			{
				playerNameText.setFill(Color.valueOf("#FFFFFF"));
				break;
			}
			default:
				break;

		}
	}
	
	public void movePawn(int oldX, int oldY, int newX, int newY, ColorPlayer movePlayer)
	{
		tabField[oldX][oldY].setFill(Color.valueOf("#FFFFFF"));
		setBoardCircleColor(tabField[newX][newY], movePlayer);
	}


	private void createBoard()
	{
		int i = 0;

		for(int j = 0; j<19; j++)
			tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

		int k = 13;

		for(i=1; i<5; i++)
		{
			for(int j = 0; j < k; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

			for(int j = k; j < 14; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);

			for(int j = 14; j < 19; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

			k--;
		}

		k = 4;
		for(i=5; i<10; i++)
		{
			for(int j = 0; j < 5; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

			int tmp = 5 + k;

			for(int j = 5; j < tmp; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);

			for(int j = tmp; j < 14; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);

			tmp = 14 + k;

			for(int j = 14; j < tmp; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);

			for(int j = tmp; j < 19; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

			k--;

		}


		k = 4;
		int c = 8;

		for(i=10; i<14; i++)
		{
			for(int j = 0; j < k; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);


			for(int j = k; j < 5; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);

			int tmp = 5 + c;

			for(int j = 5; j < tmp; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);


			k--;
			c--;

			for(int j = tmp; j < 14; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);

			for(int j = 14; j < 19; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

		}

		k = 4;

		for(i=14; i<18; i++)
		{
			for(int j = 0; j < 5; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

			int tmp = k + 5;

			for(int j = 5; j < tmp; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.AVAILABLE);

			for(int j = tmp; j < 19; j++)
				tabField[i][j] = new BoardCircle(FieldStatus.CLOSED);

			k--;
		}

		for(int j = 0; j<19; j++)
			tabField[18][j] = new BoardCircle(FieldStatus.CLOSED);

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

	public void addPawn(ColorPlayer colorPlayer, int x, int y)
	{
		BoardCircle k = tabField[x][y];
		k.setColorPlayer(colorPlayer);
		k.setX(x);
		k.setY(y);
		setBoardCircleColor(k, colorPlayer);
		arePawnsAdded = true;
	}

	private void setBoardCircleColor(BoardCircle boardCircle, ColorPlayer colorPlayer)
	{
		switch (colorPlayer)
		{
			case PLAYER_ONE:
			{
				boardCircle.setFill(Color.valueOf("#DFFF00"));
				break;
			}
			case PLAYER_TWO:
			{
				boardCircle.setFill(Color.valueOf("#FF00FF"));
				break;
			}
			case PLAYER_THREE:
			{
				boardCircle.setFill(Color.valueOf("#00FFFF"));
				break;
			}
			case PLAYER_FOUR:
			{
				boardCircle.setFill(Color.valueOf("#AFAFAF"));
				break;
			}
			case PLAYER_FIVE:
			{
				boardCircle.setFill(Color.valueOf("#CAFAFA"));
				break;
			}
			case PLAYER_SIX:
			{
				boardCircle.setFill(Color.valueOf("#CCCCCC"));
				break;
			}
			case PLAYER_EMPTY:
			{
				boardCircle.setFill(Color.valueOf("#FFFFFF"));
				break;
			}
			default:
				break;

		}
	}
}
