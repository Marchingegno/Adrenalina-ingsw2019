package it.polimi.se2019.view.client.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIController extends Application {
	private static Stage window;
	private static GUIView guiView;
	private ConnectionController connectionController;
	private LoginController loginController;

	@FXML
	private Button SocketButton;
	@FXML
	private Button RMIButton;

	static Parent loadFXML(String fxmlName) {
		try {
			return new FXMLLoader(GUIInitializer.class.getResource("/gui/" + fxmlName + ".fxml")).load();
		} catch (IOException e) {
			//TODO chanche exception handling
			throw new RuntimeException("Unable to load file");
		}
	}

	@Override
	public void start(Stage primaryStage) {
		window = primaryStage;
		Scene scene = new Scene(loadFXML("Connection"));
		window.setScene(scene);
		window.setTitle("Adrenaline");
		window.show();
	}

//	@FXML
//	public void startConnectionWithRMI() {
//		setGUIView(new GUIView(this));
//		guiView.startConnectionWithRMI();
//		setSceneTo("LoginController", "Adrenaline");
//	}
//
//	@FXML
//	public void startConnectionWithSocket() {
//		setGUIView(new GUIView(this));
//		guiView.startConnectionWithSocket();
//		setSceneTo("LoginController", "Adrenaline");
//	}


	private void setSceneTo(String fxmlName, String sceneTitle) {
		Scene scene = new Scene(loadFXML(fxmlName));
		window.setScene(scene);
		window.setTitle(sceneTitle);
//		stage.show();
	}


//	public void displayWaitingPlayers(List<String> waitingPlayers) {
//		setSceneTo("LobbyController", "Adrenaline");
//		LobbyController.addNickname(waitingPlayers);
//	}


	public void setGUIView(GUIView guiView) {
		GUIController.guiView = guiView;
	}
}
