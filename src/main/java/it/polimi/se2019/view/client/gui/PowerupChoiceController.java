package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class PowerupChoiceController {
	@FXML
	private ImageView powerup0;
	@FXML
	private ImageView powerup1;
	@FXML
	private ImageView powerup2;
	@FXML
	private Button powerupButton0;
	@FXML
	private Button powerupButton1;
	@FXML
	private Button powerupButton2;
	@FXML
	private Label title;

	private GUIView guiView;
	private Stage stage;

	public void setPowerups(List<PowerupCardRep> powerups) {
		if (!powerups.isEmpty()) {
			powerup0.setImage(loadImage(powerups.get(0).getImagePath()));
			powerup0.setVisible(true);
			powerupButton0.setVisible(true);
			powerupButton0.setDisable(false);
		} else {
			throw new IllegalStateException("You should be able to choose from at least one powerup");
		}

		if (powerups.size() >= 2) {
			powerup1.setImage(loadImage(powerups.get(1).getImagePath()));
			powerup1.setVisible(true);
			powerupButton1.setVisible(true);
			powerupButton1.setDisable(false);
		} else {
			powerup1.setVisible(false);
			powerupButton1.setVisible(false);
			powerupButton1.setDisable(true);
		}

		if (powerups.size() >= 3) {
			powerup2.setImage(loadImage(powerups.get(2).getImagePath()));
			powerup2.setVisible(true);
			powerupButton2.setVisible(true);
			powerupButton2.setDisable(false);
		} else {
			powerup2.setVisible(false);
			powerupButton2.setVisible(false);
			powerupButton2.setDisable(true);
		}
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	@FXML
	public void powerupButton0Pressed() {
		guiView.sendMessage(new IntMessage(0, MessageType.SPAWN, MessageSubtype.ANSWER));
		stage.close();
	}

	@FXML
	public void powerupButton1Pressed() {
		guiView.sendMessage(new IntMessage(1, MessageType.SPAWN, MessageSubtype.ANSWER));
		stage.close();
	}

	@FXML
	public void powerupButton2Pressed() {
		guiView.sendMessage(new IntMessage(2, MessageType.SPAWN, MessageSubtype.ANSWER));
		stage.close();
	}

	public void setGuiAndStage(GUIView guiView, Stage stage) {
		this.stage = stage;
		this.guiView = guiView;
	}

	private Image loadImage(String filePath) {
		System.out.println("Loading: " + "/graphicassets/powerups/" + filePath + ".png");
		return new Image(getClass().getResource("/graphicassets/powerups/" + filePath + ".png").toString());
	}
}
