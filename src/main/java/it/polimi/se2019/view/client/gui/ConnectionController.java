package it.polimi.se2019.view.client.gui;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConnectionController {

	private static GUIView guiView;
	@FXML
	private Button SocketButton;
	@FXML
	private Button RMIButton;

	@FXML
	public void startConnectionWithRMI() {
		SocketButton.setDisable(true);
        RMIButton.setDisable(true);
        guiView = new GUIView((Stage) RMIButton.getScene().getWindow());
		guiView.startConnectionWithRMI();
	}

	@FXML
	public void startConnectionWithSocket() {
        SocketButton.setDisable(true);
        RMIButton.setDisable(true);
		guiView = new GUIView((Stage) SocketButton.getScene().getWindow());
		guiView.startConnectionWithSocket();
	}
}
