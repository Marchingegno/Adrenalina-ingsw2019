package it.polimi.se2019.view.client.gui;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class PlayerInventoryController {
	@FXML
	private ImageView playerBoard;

	@FXML
	private ImageView weapon0;
	@FXML
	private ImageView weapon1;
	@FXML
	private ImageView weapon2;
	@FXML
	private ImageView powerup0;
	@FXML
	private ImageView powerup1;
	@FXML
	private ImageView powerup2;

	@FXML
	private GridPane ammoContainer;
	@FXML
	private ImageView blueAmmo0;
	@FXML
	private ImageView blueAmmo1;
	@FXML
	private ImageView blueAmmo2;
	@FXML
	private ImageView redAmmo0;
	@FXML
	private ImageView redAmmo1;
	@FXML
	private ImageView redAmmo2;
	@FXML
	private ImageView yellowAmmo0;
	@FXML
	private ImageView yellowAmmo1;
	@FXML
	private ImageView yellowAmmo2;

	@FXML
	private Group damageToken;
	@FXML
	private ImageView damageToken0;
	@FXML
	private ImageView damageToken1;
	@FXML
	private ImageView damageToken2;
	@FXML
	private ImageView damageToken3;
	@FXML
	private ImageView damageToken4;
	@FXML
	private ImageView damageToken5;
	@FXML
	private ImageView damageToken6;
	@FXML
	private ImageView damageToken7;
	@FXML
	private ImageView damageToken8;
	@FXML
	private ImageView damageToken9;
	@FXML
	private ImageView damageToken10;
	@FXML
	private ImageView damageToken11;

	@FXML
	private ImageView marks00;
	@FXML
	private ImageView marks01;
	@FXML
	private ImageView marks02;
	@FXML
	private ImageView marks10;
	@FXML
	private ImageView marks12;
	@FXML
	private ImageView marks20;
	@FXML
	private ImageView marks21;
	@FXML
	private ImageView marks22;
	@FXML
	private ImageView marks30;
	@FXML
	private ImageView marks31;
	@FXML
	private ImageView marks32;

	@FXML
	private Label nickname;

	@FXML
	private Label points;

	public void setNickname(String nickname) {
		this.nickname.setText(nickname);
	}

	public void setPoints(int points) {
		this.points.setText(Integer.toString(points));
	}

	public void setPlayerBoard(String characterName, boolean isFrenzy) {
		String pathImage = "playerBoards/" + characterName + "/" + characterName + (isFrenzy ? "Frenzy" : "") + "Board";
		playerBoard.setImage(loadImage(pathImage));
	}

	public void setWepons(List<String> weapons) {
		if (weapons.get(0) != null) weapon0.setImage(loadImage("weapons/"));
	}

	private Image loadImage(String filePath) {
		return new Image(getClass().getResource("/graphicassets/" + filePath + ".png").toString());
	}


}
