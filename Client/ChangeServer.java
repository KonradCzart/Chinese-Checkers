package Client;

import Client.GUIScreens.LoadingScreen;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Created by Kacper on 2018-01-01.
 */
public class ChangeServer
{
	private Stage stage;

	public ChangeServer(Stage stage)
	{
		this.stage = stage;
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
			int portData;

			try
			{
				portData = Integer.parseInt(connectionData.getValue());

				if(portData > 0 && portData <= 65535)
					new LoadingScreen(stage, connectionData.getKey(), portData);
				else
					badPortValue();
			}
			catch (NumberFormatException e)
			{
				badPortValue();
			}
		});
	}

	private void badPortValue()
	{
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("");
		alert.setContentText("Bad port syntax..");

		alert.showAndWait();
		new ChangeServer(stage);
	}
}
