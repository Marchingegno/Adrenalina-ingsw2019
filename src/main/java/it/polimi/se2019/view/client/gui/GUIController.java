package it.polimi.se2019.view.client.gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
	private ConnectionController connectionController;
	private Stage window;
	private GUIView guiView;

	static Parent loadFXML(String fxmlName) {
		try {
			return new FXMLLoader(GUIInitializer.class.getResource("/gui/" + fxmlName + ".fxml")).load();
		} catch (IOException e) {
			//TODO chanche exception handling
			throw new RuntimeException("Unable to load file");
		}
	}

	public void start(Stage primaryStage) {
		window = primaryStage;
		window.setTitle("Adrenalina");
	}

	static Stage setSceneTo(String fxmlName, String sceneTitle) {
			Scene scene = new Scene(loadFXML(fxmlName));
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(sceneTitle);
//			stage.show();
		return stage;
	}

	public void setGUIView(GUIView guiView) {
		this.guiView = guiView;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
