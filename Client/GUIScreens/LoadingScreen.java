package Client.GUIScreens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Kacper on 2017-12-29.
 */
public class LoadingScreen extends Group
{
	Stage stage;
	Scene scene;
	GridPane grid;
	Button btn;
	TextField userTextField;
	Text connectingText;
	private String playerName;

	public LoadingScreen(Stage primaryStage)
	{
		load();
		stage = primaryStage;
		scene = new Scene(grid, 850, 450);
		stage.setScene(scene);
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
		userTextField.setFocusTraversable(false);
		grid.add(userTextField, 1, 4);

		btn = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 5);

		connectingText = new Text();
		grid.add(connectingText, 1, 7);

		grid.setBackground(new Background(new BackgroundFill(Color.valueOf("#2c2f33"), CornerRadii.EMPTY, Insets.EMPTY)));

		performLoginButton();
	}

	public GridPane getPane()
	{
		return grid;
	}

	/**
	 * Performs login button and validates player name.
	 */
	private void performLoginButton()
	{
		btn.setOnAction(event ->
		{
			connectingText.setFill(Color.GREEN);
			connectingText.setSmooth(true);
			connectingText.setText("Connecting...");
			String name = userTextField.getText();

			if (name.matches("[\\w]+"))
			{
				playerName = name;
				new GameScreen(playerName, stage);
			}
			else
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Your name is invalid. Use only alphanumeric characters");

				alert.showAndWait();
			}
		});
	}

}