package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.view.ViewInterface;

import java.util.List;

public class TurnController {

	private Player currentPlayer;
	private Model model;
	private ViewInterface view;


	public TurnController(Model model) {
		this.model = model;
	}


	public void handleTurn(Player player) {
		currentPlayer = player;
		List<MacroAction> possibleActions = currentPlayer.getDamageStatus().getAvailableActions();

		while(player.getDamageStatus().getNumberOfActionsPerformed() != 0){
			view.displayPossibleActions(possibleActions);
		}
	}

	public void displayPossibleMoves(){

	}

	public void performPowerup(int indexOfPowerup) {
	}


	public void performMacroaction(int indexOfMacroAction) {
	}

}