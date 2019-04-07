package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.awt.Color;
import java.util.ArrayList;

public class PlayerRep {

	private String playerName;
	private Color playerColor;
	private int score;
	private ArrayList<String> damageBoard;
	private ArrayList<String> marks;
	private boolean[] weaponLoaded;
	private ArrayList<String> powerupCard;
	private ArrayList<AmmoType> powerupAmmo;
	private int redAmmo;
	private int yellowAmmo;
	private int blueAmmo;
	private boolean hidden;


	public PlayerRep(Player player) {
	}


	public void getHiddenPlayerRep() {
	}

	public boolean isHidden() {
		return false;
	}

}