package it.polimi.se2019.view.client.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIInitializer extends Application {


	@Override
	public void start(Stage primaryStage) throws Exception {
		GUIController guiController = new GUIController();
		guiController.startGUI();
	}
}
