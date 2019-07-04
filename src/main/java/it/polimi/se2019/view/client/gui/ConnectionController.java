package it.polimi.se2019.view.client.gui;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML controller to display the connection choice.
 *
 * @author MarcerAndrea
 */
public class ConnectionController {

	private static GUIView guiView;
	@FXML
	private Button SocketButton;
	@FXML
	private Button RMIButton;
	@FXML
	private TextField ipAddressField;

	@FXML
	public void startConnectionWithRMI() {
		SocketButton.setDisable(true);
		RMIButton.setDisable(true);
		guiView = new GUIView((Stage) RMIButton.getScene().getWindow());
		guiView.startConnectionWithRMI(ipAddressField.getAccessibleText());
	}

	@FXML
	public void startConnectionWithSocket() {
		SocketButton.setDisable(true);
		RMIButton.setDisable(true);
		guiView = new GUIView((Stage) SocketButton.getScene().getWindow());
		guiView.startConnectionWithSocket(ipAddressField.getText());
	}
}
