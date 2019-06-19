package it.polimi.se2019.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameBoardController {
	@FXML
	private Button button00;
	@FXML
	private Button button01;
	@FXML
	private Button button02;
	@FXML
	private Button button03;
	@FXML
	private Button button10;
	@FXML
	private Button button11;
	@FXML
	private Button button12;
	@FXML
	private Button button13;
	@FXML
	private Button button20;
	@FXML
	private Button button21;
	@FXML
	private Button button22;
	@FXML
	private Button button23;

	@FXML
	private Group square00;
	@FXML
	private Group square01;
	@FXML
	private Group square02;
	@FXML
	private Group square03;
	@FXML
	private Group square10;
	@FXML
	private Group square11;
	@FXML
	private Group square12;
	@FXML
	private Group square13;
	@FXML
	private Group square20;
	@FXML
	private Group square21;
	@FXML
	private Group square22;
	@FXML
	private Group square23;

	@FXML
	private Button weaponRed0;
	@FXML
	private Button weaponRed1;
	@FXML
	private Button weaponRed2;
	@FXML
	private Button weaponBlue0;
	@FXML
	private Button weaponBlue1;
	@FXML
	private Button weaponBlue2;
	@FXML
	private Button weaponYellow0;
	@FXML
	private Button weaponYellow1;
	@FXML
	private Button weaponYellow2;

	@FXML
	private ImageView skull0;
	@FXML
	private ImageView skull1;
	@FXML
	private ImageView skull2;
	@FXML
	private ImageView skull3;
	@FXML
	private ImageView skull4;
	@FXML
	private ImageView skull5;
	@FXML
	private ImageView skull6;
	@FXML
	private ImageView skull7;

	@FXML
	private ImageView backGround;

	private Image loadImage(String filePath) {
		return new Image(getClass().getResource("/graphicassets/" + filePath + ".png").toString());
	}

	public void setMap(String mapName) {
		backGround.setImage(loadImage("maps/" + mapName));
	}
}
