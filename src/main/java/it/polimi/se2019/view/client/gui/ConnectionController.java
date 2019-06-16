package it.polimi.se2019.view.client.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static it.polimi.se2019.view.client.gui.GUIController.loadFXML;

public class ConnectionController extends Application {

	private static GUIView guiView;
	private static Stage window;
	@FXML
	private Button SocketButton;
	@FXML
	private Button RMIButton;

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		Scene scene = new Scene(loadFXML("Connection"));
		window.setScene(scene);
		window.setTitle("Adrenaline");
		guiView = new GUIView(window);
		window.show();
	}

	@FXML
	public void startConnectionWithRMI() {
		guiView.startConnectionWithRMI();
	}

	@FXML
	public void startConnectionWithSocket() {
		guiView.startConnectionWithSocket();
	}
}
