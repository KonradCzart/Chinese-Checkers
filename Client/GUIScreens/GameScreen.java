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
import java.util.Optional;
import java.util.Random;

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

	GameScreen(String playerName, Stage stage)
	{
		this.stage = stage;
		this.playerName = playerName;
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
		border.setCenter(addGridPane());
		border.setRight(addBorderPane());

		scene = new Scene(border, 1024, 768);
		stage.setScene(scene);
		stage.setMinWidth(1024);
		stage.setMinHeight(768);
		stage.show();
	}

	private HBox addHBox() {
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");

//		Button buttonCurrent = new Button("Current");
//		buttonCurrent.setPrefSize(100, 20);
//
//		Button buttonProjected = new Button("Projected");
//		buttonProjected.setPrefSize(100, 20);
//		hbox.getChildren().addAll(buttonCurrent, buttonProjected);

		return hbox;
	}

	private void changeServer()
	{
// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText("Look, a Custom Login Dialog");

// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
		});
	}

	private VBox addVBox()
	{
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(8);

		Text title = new Text("Data");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		vbox.getChildren().add(title);

		Hyperlink options[] = new Hyperlink[] {
				new Hyperlink("Sales"),
				new Hyperlink("Marketing"),
				new Hyperlink("Distribution"),
				new Hyperlink("Costs")};

		for (int i=0; i<4; i++) {
			VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
			vbox.getChildren().add(options[i]);
		}

		return vbox;
	}

	public void addStackPane(HBox hb) {
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

	public GridPane addGridPane()
	{
		GridPane grid = new GridPane();
		//grid.setHgap(10);
		//grid.setVgap(10);
		//grid.setPadding(new Insets(0, 10, 0, 10));

//		Text chartTitle = new Text("Current Year");
//		chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		createBoard(grid);
//		grid.add(chartTitle, 0, 0);

		return grid;
	}

	private BorderPane addBorderPane() {
		BorderPane flow = new BorderPane();
		flow.setPadding(new Insets(5, 5, 10, 5));
		flow.setMinWidth(240);
		flow.setMaxWidth(240);
		flow.setStyle("-fx-background-color: DAE6F3;");

		chatField = new TextField();
		sendChatButton = new Button("     Send     ");
		sendChatButton.setOnAction(event ->
		{
			String message = chatField.getText();
			if(!message.equals(""))
			{
				sendToChat(chatField.getText());
				chatField.setText("");
			}
		});

		GridPane tmpGrid = new GridPane();
		tmpGrid.add(chatField, 1, 0);
		tmpGrid.add(sendChatButton, 2, 0);

		flow.setBottom(tmpGrid);

		return flow;
	}

	private void createBoard(GridPane grid)
	{
		int rowNum = 10;
		int colNum = 10;

		//grid.getColumnConstraints().add(new ColumnConstraints(gridWidth));
		//grid.getRowConstraints().add(new RowConstraints(gridHeight));

		Random rand = new Random();
		Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED};

		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
				int n = rand.nextInt(4);
				Rectangle rec = new Rectangle();
				rec.setWidth(50);
				rec.setHeight(50);
				rec.setFill(colors[n]);
				GridPane.setRowIndex(rec, row);
				GridPane.setColumnIndex(rec, col);
				grid.getChildren().addAll(rec);
			}
		}
	}

	public void sendToChat(String chat)
	{
		System.out.println(chat);
	}
}
