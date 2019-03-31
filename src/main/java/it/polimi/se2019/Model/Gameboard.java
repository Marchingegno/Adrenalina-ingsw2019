package it.polimi.se2019.Model;

import it.polimi.se2019.Model.Cards.Ammo.AmmoDeck;
import it.polimi.se2019.Model.Cards.Powerups.PowerupDeck;
import it.polimi.se2019.Model.Cards.Weapons.WeaponDeck;
import it.polimi.se2019.Model.GameMap.GameMap;
import it.polimi.se2019.Model.Player.Player;

import java.util.*;

/**
 * 
 */
public class Gameboard {

    /**
     * Default constructor
     */
    public Gameboard() {
    }

    /**
     * 
     */
    private int skulls;

    /**
     * 
     */
    private ArrayList<Player> killShots;

    /**
     * 
     */
    private ArrayList<Player> doubleKill;

    /**
     * 
     */
    private WeaponDeck weaponDeck;

    /**
     * 
     */
    private AmmoDeck ammoDeck;

    /**
     * 
     */
    private PowerupDeck powerupDeck;

    /**
     * 
     */
    private Player currentPlayer;

    /**
     * 
     */
    private ArrayList<Player> players;

    /**
     * 
     */
    private GameMap gameMap;

    /**
     * @param skulls
     */
    public void Gameboard(int skulls) {
        // TODO implement here
    }

    /**
     * @return
     */
    public ArrayList<Player> getPlayers() {
        // TODO implement here
        return null;
    }

    /**
     * 
     */
    public void isFrenzyTriggered() {
        // TODO implement here
    }

    /**
     * @param player
     */
    public void addDoubleKill(Player player) {
        // TODO implement here
    }

    /**
     * @param player 
     * @param overkill
     */
    public void addKill(Player player, boolean overkill) {
        // TODO implement here
    }

    /**
     * 
     */
    public void nextPlayerTurn() {
        // TODO implement here
    }

    /**
     * @return
     */
    public WeaponDeck getWeaponDeck() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public PowerupDeck getPowerupDeck() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public AmmoDeck getAmmoDeck() {
        // TODO implement here
        return null;
    }

}