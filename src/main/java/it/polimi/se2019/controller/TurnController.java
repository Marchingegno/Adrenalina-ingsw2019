package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.view.ViewInterface;

import java.util.Observable;
import java.util.Observer;

/**
 * This class is in a lower level than GameController. It handles the logic relative
 * This class is a message handler.
 * @author Marchingegno
 */
public class TurnController{

	private Player currentPlayer;
	private Model model;
	private ViewInterface view;
	private WeaponController weaponController;


	public TurnController(Model model) {
		this.model = model;
		this.weaponController = new WeaponController();
	}

	/**
	 * Beginning of the turn.
	 * @param player the current player.
	 */
	@Deprecated
	public void handleTurn(Player player) {
		currentPlayer = player;
		player.setTurnStatus(TurnStatus.YOUR_TURN);

	}

	public void displayPossibleMoves(){

	}

	public void performPowerup(int indexOfPowerup) {
	}


	public void performMacroaction(int indexOfMacroAction) {
	}

	void processMessage(Message message){
		switch(message.getMessageType()){


		}

	}
}