package it.polimi.se2019.view.client.gui;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static it.polimi.se2019.view.client.gui.GUIController.loadFXML;

public class ConnectionController {

	private static GUIView guiView;
	private static Stage window;
	private static GUIController guiController;
	@FXML
	private Button SocketButton;
	@FXML
	private Button RMIButton;


	public void start(GUIController guicontroller) {
		guiController = guicontroller;
		window = new Stage();
		Scene scene = new Scene(loadFXML("Connection"));
		window.setScene(scene);
		window.setTitle("Adrenaline");
		window.show();
	}

//	@FXML
////	public void startConnectionWithRMI() {
////		guiView = new GUIView(window);
////		guiView.startConnectionWithRMI();
////		guiController.setGUIView(guiView);
////		window.close();
////	}
////
////	@FXML
////	public void startConnectionWithSocket() {
////		guiView = new GUIView(window);
////		guiView.startConnectionWithSocket();
////		guiController.setGUIView(guiView);
////		window.close();
////	}
}
