package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A sharable version of all the useful Player information.
 * @author Desno365
 */
public class PlayerRep extends Message {

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


	/**
	 * Create a new PlayerRep with all the information of a Player.
	 * @param player the player from which the information are extracted.
	 */
	public PlayerRep(Player player) {
		super(MessageType.PLATER_REP, MessageSubtype.INFO);
		playerName = player.getPlayerName();
		playerColor = player.getPlayerColor();
		points = player.getPlayerBoard().getPoints();

		damageBoard = new ArrayList<>(player.getPlayerBoard().getDamageBoard().size());
		for(Player player1 : player.getPlayerBoard().getDamageBoard()) {
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

	/**
	 * Used to create the hidden PlayerRep.
	 */
	private PlayerRep() {
		super(MessageType.PLATER_REP, MessageSubtype.INFO);
	}

	/**
	 * Returns a PlayerRep that contains only the information that are available also to the other players.
	 * @return a new PlayerRep without the sensitive information.
	 */
	public PlayerRep getHiddenPlayerRep() {
		PlayerRep newPlayerRep = new PlayerRep();
		newPlayerRep.playerName = this.playerName;
		newPlayerRep.playerColor = this.playerColor;
		newPlayerRep.points = -1; // hidden
		newPlayerRep.damageBoard = new ArrayList<>(damageBoard);
		newPlayerRep.marks = new ArrayList<>(marks);
		newPlayerRep.weaponLoaded = this.weaponLoaded;
		newPlayerRep.powerupCards = null; // hidden
		newPlayerRep.powerupAmmos = null; // hidden
		newPlayerRep.redAmmo = this.redAmmo;
		newPlayerRep.yellowAmmo = this.yellowAmmo;
		newPlayerRep.blueAmmo = this.blueAmmo;
		newPlayerRep.hidden = true;
		return newPlayerRep;
	}

	/**
	 * Returns true if the sensitive information are removed.
	 * @return true if the sensitive information are removed.
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Return the player name.
	 * @return the player name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Returns the player color.
	 * @return the player color.
	 */
	public Color getPlayerColor() {
		return playerColor;
	}

	/**
	 * Returns the sensitive information of player's points.
	 * @return the sensitive information of player's points.
	 * @throws HiddenException if the PlayerRep is hidden and doesn't contain sensitive information.
	 */
	public int getPoints() throws HiddenException {
		if(isHidden())
			throw new HiddenException("The value of \"points\" is hidden in this PlayerRep.");
		return points;
	}

	/**
	 * Returns the damage board of this player with all the player names that made the damage.
	 * @return the damage board of this player with all the player names that made the damage.
	 */
	public List<String> getDamageBoard() {
		return damageBoard;
	}

	/**
	 * Returns the marks of this player with all the player names that made the marks.
	 * @return the marks of this player with all the player names that made the marks.
	 */
	public List<String> getMarks() {
		return marks;
	}

	/**
	 * Returns an array of boolean that represents which weapons are loaded in the player's inventory.
	 * @return an array of boolean that represents which weapons are loaded in the player's inventory.
	 */
	public boolean[] getWeaponLoaded() {
		return weaponLoaded;
	}

	/**
	 * Returns the sensitive information of player's powerup cards.
	 * @return the sensitive information of player's powerup cards.
	 * @throws HiddenException if the PlayerRep is hidden and doesn't contain sensitive information.
	 */
	public List<String> getPowerupCards() throws HiddenException {
		if(isHidden())
			throw new HiddenException("The value of \"powerupCards\" is hidden in this PlayerRep.");
		return powerupCards;
	}

	/**
	 * Returns the sensitive information of the ammo associated with the player's powerups.
	 * @return the sensitive information of the ammo associated with the player's powerups.
	 * @throws HiddenException if the PlayerRep is hidden and doesn't contain sensitive information.
	 */
	public List<AmmoType> getPowerupAmmos() throws HiddenException {
		if(isHidden())
			throw new HiddenException("The value of \"powerupAmmos\" is hidden in this PlayerRep.");
		return powerupAmmos;
	}

	/**
	 * Returns the total amount of red ammo this player posses.
	 * @return the total amount of red ammo this player posses.
	 */
	public int getRedAmmo() {
		return redAmmo;
	}

	/**
	 * Returns the total amount of yellow ammo this player posses.
	 * @return the total amount of yellow ammo this player posses.
	 */
	public int getYellowAmmo() {
		return yellowAmmo;
	}

	/**
	 * Returns the total amount of blue ammo this player posses.
	 * @return the total amount of blue ammo this player posses.
	 */
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