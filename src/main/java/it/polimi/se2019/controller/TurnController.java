package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.view.server.VirtualView;

import java.util.List;

/**
 * This class is in a lower level than GameController. It handles the logic relative
 * This class is a message handler.
 * @author Marchingegno
 */
public class TurnController{

	private Model model;
	private GameMap gameMap;
	private WeaponController weaponController;
	private List<VirtualView> virtualViews;

	public TurnController(Model model, List<VirtualView> virtualViews) {
		this.model = model;
		this.weaponController = new WeaponController();
		this.gameMap = model.getGameMap();
		this.virtualViews = virtualViews;
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

		switch(message.getMessageType()){
			case ACTION:
				//VirtualView vw = ((ActionMessage) message).getVirtualView();
				break;
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