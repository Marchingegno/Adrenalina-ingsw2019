package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.view.server.VirtualView;

/**
 * This class is in a lower level than GameController. It handles the logic relative
 * This class is a message handler.
 * @author Marchingegno
 */
public class TurnController{

	private Model model;
	private GameMap gameMap;
	private WeaponController weaponController;


	public TurnController(Model model) {
		this.model = model;
		this.weaponController = new WeaponController();
		this.gameMap = model.getGameMap();
	}


	public void displayPossibleMoves(){

	}

	public void performPowerup(int indexOfPowerup) {
	}


	public void performMacroaction(int indexOfMacroAction) {
	}

	void processMessage(Message message) {
		//TODO: Control veridicity of the message.

		VirtualView virtualView = ((ActionMessage)message).getVirtualView();
		Player player = model.getPlayerFromName(virtualView.getPlayerName());

		if(player.getTurnStatus() != TurnStatus.YOUR_TURN){
			throw new RuntimeException("It's not your turn!");
		}

		switch(message.getMessageType()){
			case ACTION:
				VirtualView vw = ((ActionMessage) message).getVirtualView();
			case GRAB_AMMO:
				model.grabAmmoCard(player, ((DefaultActionMessage)message).getIndex());
				break;
			case GRAB_WEAPON:
				model.grabWeaponCard(player, ((DefaultActionMessage)message).getIndex());
				break;
			case MOVE:
				model.movePlayerTo(player, ((MoveActionMessage)message).getCoordinates());
				break;
			case RELOAD:
				player.reload(((DefaultActionMessage)message).getIndex());
				break;
			default: throw new RuntimeException("Received wrong type of message.");
		}

	}
}