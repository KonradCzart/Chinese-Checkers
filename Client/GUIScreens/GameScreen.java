package Client.GUIScreens;

import Game.BoardCircle;
import Game.ColorPlayer;
import Game.FieldStatus;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import Client.*;
import Message.*;

/**
 * Main game GUI
 * Contains board, chat, menu, buttons, containers etc.
 */
public class GameScreen
{
	private static final int WINDOW_WIDTH = 1024;
	private static final int WINDOW_HEIGHT = 800;

	private boolean isChatCreated;
	private boolean arePawnsAdded;
	private BorderPane border;
	private BoardCircle clickedBoardCircle;
	private BoardCircle[][] tabField;
	private Client myClient;
	private HBox topHBox;
	private Paint lastClickedColor;
	private Scene scene;
	private String playerName;
	private Text playerNameText;
	private TextField chatField;
	private Stage stage;
	private VBox chatBox;

	/**
	 * Constructor, it starts game GUI
	 * @param playerName name taken from previous GUI (ex. LoadingScreen)
	 * @param stage javaFX stage
	 * @param myClient Client game class to handle with server
	 */
	GameScreen(String playerName, Stage stage, Client myClient)
	{
		this.stage = stage;
		this.playerName = playerName;
		this.myClient = myClient;
		this.myClient.startServerListener(this);
		isChatCreated = false;
		arePawnsAdded = false;
		Platform.setImplicitExit(false);

		NewNameMessage newMessage = new NewNameMessage(playerName);
		try {
			this.myClient.sendMessage(newMessage);
		} catch (IOException e) {
			System.out.println("NewNameMessage method failed..");
		}

		tabField = new BoardCircle[19][19];

		createBoard();
		load();
	}

	/**
	 * Create board field logic and table
	 */
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

	/**
	 * It loads containers and set their layouts
	 */
	private void load()
	{
		border = new BorderPane();
		topHBox = new HBox();

		createGameMenu();
		addPlayerNameText();
		addTopButtons();

		border.setTop(topHBox);
		border.setCenter(addBoardPane());
		border.setRight(addChatPane());

		topHBox.setPadding(new Insets(15, 12, 15, 12));
		topHBox.setSpacing(10);
		topHBox.setStyle("-fx-background-color: #336699;");

		scene = new Scene(border, WINDOW_WIDTH - 100, WINDOW_HEIGHT - 100);
		stage.setScene(scene);
		stage.setMinWidth(WINDOW_WIDTH);
		stage.setMinHeight(WINDOW_HEIGHT);
		stage.show();
	}

	/**
	 * Creates menuBar with menuItems
	 */
	private void createGameMenu()
	{
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		Menu menuHelp = new Menu("Help");

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
		joinGame.setOnAction(event -> gameIdDialog());

		MenuItem removeGame = new MenuItem(("Remove game"));
		removeGame.setOnAction(event ->
		{
			SuccessMessage newMessage = new SuccessMessage(22222, "remove game");
			try {
				myClient.sendMessage(newMessage);
			} catch (IOException e) {
				System.out.println("Removing game failure..");
			}
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
		exit.setOnAction(t ->
		{
			Platform.exit();
			System.exit(0);
		});

		menuFile.getItems().addAll(joinGame, createGame, changeServer, setName, new SeparatorMenuItem(),removeGame, exit);
		menuBar.getMenus().addAll(menuFile, menuHelp);

		MenuItem authorsInfo = new MenuItem("About..");
		authorsInfo.setOnAction(t ->
		{
			String context = "Created by:\nKonrad Czart & Kacper Zielinski\nConnection: "
					+ myClient.getLocalhost() + ":" + myClient.getPort();
			infoDialog("About", "Chinese Checkers Game", context);
		});
		menuHelp.getItems().addAll(authorsInfo);

		topHBox.getChildren().add(menuBar);
	}

	/**
	 * Adds playerName Text to container
	 */
	private void addPlayerNameText()
	{
		playerNameText = new Text("");
		topHBox.getChildren().add(playerNameText);
	}


	/**
	 * Creates change server object
	 */
	private void changeServer()
	{
		new ChangeServer(stage);
	}

	/**
	 * Creates join game Dialog
	 */
	private void gameIdDialog()
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


	/**
	 * Adds top button to start game and end turn.
	 */
	private void addTopButtons()
	{
		HBox stack = new HBox();

		Button startGameButton = new Button("Start");
		Button endRoundButton = new Button("EndRound");

		startGameButton.setPrefSize(100, 20);
		endRoundButton.setPrefSize(100, 20);

		startGameButton.setOnAction(event ->
		{
			SuccessMessage newMessage = new SuccessMessage(11111, "start game");
			try {
				myClient.sendMessage(newMessage);
			} catch (IOException e) {
				System.out.println("Start game failure..");
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
		stack.setAlignment(Pos.CENTER_RIGHT);
		StackPane.setMargin(startGameButton, new Insets(0, 10, 0, 0));

		topHBox.getChildren().add(stack);
		HBox.setHgrow(stack, Priority.ALWAYS);
	}

	/**
	 * Adds chatPane to GUI
	 * @return BorderPane scrolled object
	 */
	private BorderPane addChatPane()
	{
		BorderPane flow = new BorderPane();
		flow.setPadding(new Insets(5, 5, 10, 5));
		flow.setMinWidth(240);
		flow.setMaxWidth(240);
		flow.setStyle("-fx-background-color: DAE6F3;");

		chatBox = new VBox();
		isChatCreated = true;
		ScrollPane scrollPane = new ScrollPane();
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
		Button sendChatButton = new Button("Send");
		sendChatButton.setOnAction(event -> sendMessageToChat());

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

	/**
	 * Sends message to chat
	 */
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

	/**
	 * Adds boardPane to GUI
	 * @return boardPane container
	 */
	private GridPane addBoardPane()
	{
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setTranslateX(-170);

		paintBoard(gridPane);

		return gridPane;
	}

	/**
	 * Draw a board
	 * @param grid container
	 */
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
					c.setTranslateX(i*19);
					GridPane.setRowIndex(c, i);
					GridPane.setColumnIndex(c, j);
					GridPane.setMargin(c, new Insets(6, 6, 6, 6));
					grid.getChildren().addAll(c);
					c.setOnMouseClicked(event -> onBoardCircleClick(c));
				}
			}
		}

	}

	/**
	 * Actions when BoardCircle is clicked
	 * @param circle single circle
	 */
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
				clickedBoardCircle.setFill(lastClickedColor);
				int oldX, oldY, newX, newY;
				oldX = clickedBoardCircle.getX();
				oldY = clickedBoardCircle.getY();
				newX = circle.getX();
				newY = circle.getY();

				MoveMessage newMessage = new MoveMessage(oldX, oldY, newX, newY);
				try {
					myClient.sendMessage(newMessage);
				} catch (IOException e) {
					System.out.println("Move message failure..");
				}

				clickedBoardCircle = null;
			}
		}
	}

	/**
	 * Color the Text of player name in top HBox
	 * @param name name of player
	 * @param colorPlayer player unique color
	 */
	public void getPlayerNameColored(String name, ColorPlayer colorPlayer)
	{
		String line = "Now round: " + name;
		playerNameText.setText(line);
		setBoardCircleColor(playerNameText, colorPlayer);
	}

	/**
	 * Moves player circle in GUI
	 * @param oldX old x param
	 * @param oldY old y param
	 * @param newX new x param
	 * @param newY new y param
	 * @param movePlayer player unique color
	 */
	public void movePawn(int oldX, int oldY, int newX, int newY, ColorPlayer movePlayer)
	{
		tabField[oldX][oldY].setFill(Color.valueOf("#FFFFFF"));
		setBoardCircleColor(tabField[newX][newY], movePlayer);
	}

	/**
	 * Color shapes such as: BoardCircle, TextField
	 * @param boardCircle shape to color
	 * @param colorPlayer player unique color
	 *
	 */
	private void setBoardCircleColor(Shape boardCircle, ColorPlayer colorPlayer)
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

	/**
	 * Adds message to chat
	 * @param chatMessage Text to be shown in chat
	 */
	private void addTextToChat(Text chatMessage)
	{
		Platform.runLater(() ->
		{
			VBox.setMargin(chatMessage, new Insets(0, 0, 0, 8));
			chatBox.getChildren().add(chatMessage);
		});
	}
	/**
	 * Mediates between server and GUI to send message chat
	 * @param chat message to be shown
	 */
	public void sendToChat(String chat)
	{
		if(isChatCreated)
			addTextToChat(new Text(chat));
	}

	/**
	 * Add pawn to board
	 * @param colorPlayer player unique color
	 * @param x x point
	 * @param y y point
	 */
	public void addPawn(ColorPlayer colorPlayer, int x, int y)
	{
		BoardCircle k = tabField[x][y];
		k.setColorPlayer(colorPlayer);
		k.setX(x);
		k.setY(y);
		setBoardCircleColor(k, colorPlayer);
		arePawnsAdded = true;
	}

	/**
	 * Creates Dialog windows with information icon
	 * @param title Title of window
	 * @param headerText Header text
	 * @param context Context
	 */
	public void infoDialog(String title, String headerText, String context)
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(context);

		alert.showAndWait();
	}

	/**
	 * Creates Dialog windows with error icon
	 * @param errorMessage Context of error
	 */
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

	/**
	 * Creates Dialog windows with success icon
	 * @param successMessage Context of success
	 */
	public void successDialog(String successMessage)
	{
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("");
			alert.setContentText(successMessage);

			alert.showAndWait();
		});

	}

	/**
	 * It removes game and reset GUI
	 */
	public void removeGame()
	{
		Platform.runLater(() -> {
			this.createBoard();
			border.setCenter(addBoardPane());
			arePawnsAdded = false;
		});

	}
}
