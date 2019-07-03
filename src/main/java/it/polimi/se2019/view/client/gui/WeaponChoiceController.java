package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.cards.weapons.WeaponRep;
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

/**
 * FXML controller to display the request of a weapon.
 *
 * @author MarcerAndrea
 */
public class WeaponChoiceController {
	@FXML
	private ImageView weapon0;
	@FXML
	private ImageView weapon1;
	@FXML
	private ImageView weapon2;
	@FXML
	private Button weaponButton0;
	@FXML
	private Button weaponButton1;
	@FXML
	private Button weaponButton2;
	@FXML
	private Label title;

	private GUIView guiView;
	private Stage stage;
	private Request request;
	private List<WeaponRep> weaponReps;
	private int answer;

	@FXML
	public void pressedWeapon0() {
		if (request == Request.CHOOSE_INT)
			answer = 0;
		if (request == Request.RELOAD)
			guiView.sendMessage(new IntMessage(0, MessageType.RELOAD, MessageSubtype.ANSWER));
		if (request == Request.CHOOSE)
			guiView.sendMessage(new IntMessage(0, MessageType.WEAPON, MessageSubtype.ANSWER));
		stage.close();
	}

	@FXML
	public void pressedWeapon1() {
		if (request == Request.CHOOSE_INT)
			answer = 1;
		if (request == Request.RELOAD)
			guiView.sendMessage(new IntMessage(1, MessageType.RELOAD, MessageSubtype.ANSWER));
		if (request == Request.CHOOSE)
			guiView.sendMessage(new IntMessage(1, MessageType.WEAPON, MessageSubtype.ANSWER));
		stage.close();
	}

	@FXML
	public void pressedWeapon2() {
		if (request == Request.CHOOSE_INT)
			answer = 2;
		if (request == Request.RELOAD)
			guiView.sendMessage(new IntMessage(2, MessageType.RELOAD, MessageSubtype.ANSWER));
		if (request == Request.CHOOSE)
			guiView.sendMessage(new IntMessage(2, MessageType.WEAPON, MessageSubtype.ANSWER));
		stage.close();
	}

	void setWeaponsToChoose(List<Integer> indexesOfTheWeapons, List<WeaponRep> weapons, Request request) {
		this.request = request;
		this.weaponReps = weapons;

		if (!weapons.isEmpty()) {
			weapon0.setVisible(true);
			weapon0.setImage(loadImage("weapons/" + weapons.get(0).getImagePath()));
		} else {
			weapon0.setVisible(false);
		}

		if (weapons.size() >= 2) {
			weapon1.setVisible(true);
			weapon1.setImage(loadImage("weapons/" + weapons.get(1).getImagePath()));
		} else {
			weapon1.setVisible(false);
		}

		if (weapons.size() >= 3) {
			weapon2.setVisible(true);
			weapon2.setImage(loadImage("weapons/" + weapons.get(2).getImagePath()));
		} else {
			weapon2.setVisible(false);
		}

		if (indexesOfTheWeapons.contains(0)) {
			weaponButton0.setDisable(false);
			weaponButton0.setVisible(true);
		} else {
			weaponButton0.setVisible(false);
			weaponButton0.setDisable(true);
		}
		if (indexesOfTheWeapons.contains(1)) {
			weaponButton1.setVisible(true);
			weaponButton1.setDisable(false);
		} else {
			weaponButton1.setVisible(false);
			weaponButton1.setDisable(true);
		}
		if (indexesOfTheWeapons.contains(2)) {
			weaponButton2.setVisible(true);
			weaponButton2.setDisable(false);
		} else {
			weaponButton2.setVisible(false);
			weaponButton2.setDisable(true);
		}
	}

	int askWeapon() {
		stage.showAndWait();
		return answer;
	}

	void setTitle(String title) {
		this.title.setText(title);
	}

	void setGuiAndStage(GUIView guiView, Stage stage) {
		this.guiView = guiView;
		this.stage = stage;
	}

	private Image loadImage(String filePath) {
		System.out.println("/graphicassets/" + filePath + ".png");
		return new Image(getClass().getResource("/graphicassets/" + filePath + ".png").toString());
	}
}
