package it.polimi.se2019.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class WarningController {
	@FXML
	private Label warningText;

	@FXML
	private void donePressed() {
		System.exit(0);
	}

	public void setWarning(String text) {
		warningText.setText(text);
	}
}
