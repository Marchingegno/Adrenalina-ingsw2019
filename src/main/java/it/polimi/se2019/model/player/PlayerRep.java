package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PlayerRep {

	private String playerName;
	private Color playerColor;
	private int points;
	private ArrayList<String> damageBoard;
	private ArrayList<String> marks;
	private boolean[] weaponLoaded;
	private ArrayList<String> powerupCards;
	private ArrayList<AmmoType> powerupAmmos;
	private int redAmmo;
	private int yellowAmmo;
	private int blueAmmo;
	private boolean hidden;


	public PlayerRep(Player player) {
		playerName = player.getPlayerName();
		playerColor = player.getPlayerColor();
		points = player.getPlayerBoard().getPoints();

		damageBoard = new ArrayList<>(player.getPlayerBoard().getDamage().size());
		for(Player player1 : player.getPlayerBoard().getDamage()) {
			damageBoard.add(player1.getPlayerName());
		}

		marks = new ArrayList<>(player.getPlayerBoard().getMarks().size());
		for(Player player2 : player.getPlayerBoard().getMarks()) {
			marks.add(player2.getPlayerName());
		}

		weaponLoaded = new boolean[player.getPlayerBoard().getWeaponCards().size()];
		for (int i = 0; i < player.getPlayerBoard().getWeaponCards().size(); i++) {
			weaponLoaded[i] = player.getPlayerBoard().getWeaponCards().get(i).isLoaded();
		}

		powerupCards = new ArrayList<>(player.getPlayerBoard().getPowerupCards().size());
		for(PowerupCard powerupCard : player.getPlayerBoard().getPowerupCards()) {
			powerupCards.add(powerupCard.toString());
		}

		powerupAmmos = new ArrayList<>(player.getPlayerBoard().getPowerupCards().size());
		for(PowerupCard powerupCard : player.getPlayerBoard().getPowerupCards()) {
			powerupAmmos.add(powerupCard.getAssociatedAmmo());
		}

		redAmmo = player.getPlayerBoard().getAmmoContainer().getAmmo(AmmoType.RED_AMMO);
		yellowAmmo = player.getPlayerBoard().getAmmoContainer().getAmmo(AmmoType.YELLOW_AMMO);
		blueAmmo = player.getPlayerBoard().getAmmoContainer().getAmmo(AmmoType.BLUE_AMMO);
		hidden = false;
	}

	private PlayerRep() {}

	public PlayerRep getHiddenPlayerRep() {
		PlayerRep newPlayerRep = new PlayerRep();
		newPlayerRep.playerName = this.playerName;
		newPlayerRep.playerColor = this.playerColor;
		newPlayerRep.points = -1; // hidden
		newPlayerRep.damageBoard = new ArrayList<>(damageBoard.size());
		newPlayerRep.damageBoard.addAll(damageBoard);
		newPlayerRep.marks = new ArrayList<>(marks.size());
		newPlayerRep.marks.addAll(marks);
		newPlayerRep.weaponLoaded = this.weaponLoaded;
		newPlayerRep.powerupCards = null; // hidden
		newPlayerRep.powerupAmmos = null; // hidden
		newPlayerRep.redAmmo = this.redAmmo;
		newPlayerRep.yellowAmmo = this.yellowAmmo;
		newPlayerRep.blueAmmo = this.blueAmmo;
		newPlayerRep.hidden = true;
		return newPlayerRep;
	}

	public boolean isHidden() {
		return hidden;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Color getPlayerColor() {
		return playerColor;
	}

	public int getPoints() throws HiddenException {
		if(isHidden())
			throw new HiddenException("The value of \"points\" is hidden in this PlayerRep.");
		return points;
	}

	public List<String> getDamageBoard() {
		return damageBoard;
	}

	public List<String> getMarks() {
		return marks;
	}

	public boolean[] getWeaponLoaded() {
		return weaponLoaded;
	}

	public List<String> getPowerupCards() throws HiddenException {
		if(isHidden())
			throw new HiddenException("The value of \"powerupCards\" is hidden in this PlayerRep.");
		return powerupCards;
	}

	public List<AmmoType> getPowerupAmmos() throws HiddenException {
		if(isHidden())
			throw new HiddenException("The value of \"powerupAmmos\" is hidden in this PlayerRep.");
		return powerupAmmos;
	}

	public int getRedAmmo() {
		return redAmmo;
	}

	public int getYellowAmmo() {
		return yellowAmmo;
	}

	public int getBlueAmmo() {
		return blueAmmo;
	}

}

/**
 * Thrown when trying to get the value of an attribute that is hidden.
 * @author Desno365
 */
class HiddenException extends Exception {

	/**
	 * Constructs an HiddenException with the specified detail message.
	 * @param message the detail message.
	 */
	public HiddenException(String message) {
		super(message);
	}
}