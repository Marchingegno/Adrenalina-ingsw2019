package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.view.VirtualView;

import java.util.ArrayList;
import java.util.Set;

public class TurnController {

	private Player currentPlayer;
	private Model model;
	private Set<MacroAction> possibleActions;
	private VirtualView view;


	public TurnController(Model model) {
	}


	public void handleTurn(Player player) {
	}

	public void displayPossibleMoves(){

	}

	public void performPowerup(int indexOfPowerup) {
	}

	public void performShoot(int indexOfWeapon, ArrayList<String> playersToShoot) {
	}

	public void performMacroaction(int indexOfMacroAction) {
	}

}