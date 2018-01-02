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
 * Class create Dialog window, check if parameters are ok and change server
 */
public class ChangeServer
{
	private Stage stage;

	public ChangeServer(Stage stage)
	{
		this.stage = stage;
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Connect to server");
		dialog.setHeaderText("");

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

		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		hostname.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> hostname.requestFocus());

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
