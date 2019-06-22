package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.model.cards.weapons.WeaponRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.Color;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
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


	private List<ImageView> damageTokens = new ArrayList<>();
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

	private List<ImageView> marks = new ArrayList<>();

	@FXML
	private ImageView marks00;
	@FXML
	private ImageView marks01;
	@FXML
	private ImageView marks02;
	@FXML
	private ImageView marks10;
	@FXML
	private ImageView marks11;
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
	private ImageView skull0;
	@FXML
	private ImageView skull1;
	@FXML
	private ImageView skull2;
	@FXML
	private ImageView skull3;

	@FXML
	private Label nickname;

	@FXML
	private Label points;


	public void setInventory(PlayerRep playerRep) {

		marks.add(marks00);
		marks.add(marks01);
		marks.add(marks02);
		marks.add(marks10);
		marks.add(marks11);
		marks.add(marks12);
		marks.add(marks20);
		marks.add(marks21);
		marks.add(marks22);
		marks.add(marks30);
		marks.add(marks31);
		marks.add(marks31);

		damageTokens.add(damageToken0);
		damageTokens.add(damageToken1);
		damageTokens.add(damageToken2);
		damageTokens.add(damageToken3);
		damageTokens.add(damageToken4);
		damageTokens.add(damageToken5);
		damageTokens.add(damageToken6);
		damageTokens.add(damageToken7);
		damageTokens.add(damageToken8);
		damageTokens.add(damageToken9);
		damageTokens.add(damageToken10);
		damageTokens.add(damageToken11);
		setNickname(playerRep.getPlayerName());
		setPoints(playerRep.getPoints());
		setPlayerBoard(playerRep.getPgName(), false); //TODO set correct player board if in frenzy
		setAmmoContainer(playerRep);
		setDamageToken(playerRep.getDamageBoard());

		List<String> weaponsPath = new ArrayList<>();
		for (WeaponRep weaponRep : playerRep.getWeaponReps()) {
			weaponsPath.add("weapons/" + weaponRep.getImagePath());
		}
		setWeapons(weaponsPath);

		List<String> powerupsPath = new ArrayList<>();
		if (!playerRep.isHidden()) {
			for (PowerupCardRep powerupCardRep : playerRep.getPowerupCards()) {
				powerupsPath.add("powerups/" + powerupCardRep.getImagePath());
			}
		}
		setPowerups(powerupsPath);
	}

	private void setNickname(String nickname) {
		this.nickname.setText(nickname);
	}

	private void setPoints(int points) {
		this.points.setText(Integer.toString(points));
	}

	private void setPlayerBoard(String characterName, boolean isFrenzy) {
		String pathImage = "playerBoards/" + characterName + "/" + (isFrenzy ? "frenzy" : "normal") + "Board";
		playerBoard.setImage(loadImage(pathImage));
	}

	public void setWeapons(List<String> weaponsPath) {
		if (!weaponsPath.isEmpty()) {
			weapon0.setImage(loadImage(weaponsPath.get(0)));
			weapon0.setVisible(true);
		} else weapon0.setVisible(false);
		if (weaponsPath.size() >= 2) {
			weapon1.setImage(loadImage(weaponsPath.get(1)));
			weapon1.setVisible(true);
		} else weapon1.setVisible(false);
		if (weaponsPath.size() >= 3) {
			weapon2.setImage(loadImage(weaponsPath.get(2)));
			weapon2.setVisible(true);
		} else weapon2.setVisible(false);
	}

	public void setPowerups(List<String> powerupsPath) {
		if (powerupsPath.size() >= 3) {
			powerup2.setImage(loadImage(powerupsPath.get(2)));
			powerup2.setVisible(true);
		} else powerup2.setVisible(false);
		if (powerupsPath.size() >= 2) {
			powerup1.setImage(loadImage(powerupsPath.get(1)));
			powerup1.setVisible(true);
		} else powerup1.setVisible(false);
		if (!powerupsPath.isEmpty()) {
			powerup0.setImage(loadImage(powerupsPath.get(0)));
			powerup0.setVisible(true);
		} else powerup0.setVisible(false);
	}

	public void setAmmoContainer(PlayerRep playerRep) {
		blueAmmo0.setVisible(playerRep.getAmmo(AmmoType.BLUE_AMMO) > 0);
		blueAmmo1.setVisible(playerRep.getAmmo(AmmoType.BLUE_AMMO) > 1);
		blueAmmo2.setVisible(playerRep.getAmmo(AmmoType.BLUE_AMMO) > 2);
		redAmmo0.setVisible(playerRep.getAmmo(AmmoType.RED_AMMO) > 0);
		redAmmo1.setVisible(playerRep.getAmmo(AmmoType.RED_AMMO) > 1);
		redAmmo2.setVisible(playerRep.getAmmo(AmmoType.RED_AMMO) > 2);
		yellowAmmo0.setVisible(playerRep.getAmmo(AmmoType.YELLOW_AMMO) > 0);
		yellowAmmo1.setVisible(playerRep.getAmmo(AmmoType.YELLOW_AMMO) > 1);
		yellowAmmo2.setVisible(playerRep.getAmmo(AmmoType.YELLOW_AMMO) > 2);
	}

	public void setDamageToken(List<Color.CharacterColorType> damageBoard) {
		for (int i = 0; i < 12; i++) {
			if (i < damageBoard.size())
				(damageTokens.get(i)).setImage(loadImage("playerBoards/" + damageBoard.get(i).getPgName() + "/token"));
			else
				damageTokens.get(i).setVisible(false);
		}
	}

	public void setMarks() {

	}

	public void setSkulls() {

	}

	private Image loadImage(String filePath) {
		System.out.println("Loading: " + "/graphicassets/" + filePath + ".png");
		return new Image(getClass().getResource("/graphicassets/" + filePath + ".png").toString());
	}
}
