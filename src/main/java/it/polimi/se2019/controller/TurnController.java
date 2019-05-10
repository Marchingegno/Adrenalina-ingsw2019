package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

import java.util.List;

/**
 * This class is in a lower level than GameController. It handles the logic relative
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

	void processEvent(Event event) {
		//TODO: Control veridicity of the message.

		VirtualView virtualView = event.getVirtualView();
		Player player = model.getPlayerFromName(virtualView.getPlayerName());

		Utils.logInfo("TurnController: processing this event " + event.toString());
		switch(event.getMessage().getMessageType()){
			case ACTION:
				player.getDamageStatus().decreaseActionsToPerform();
				player.getDamageStatus().setCurrentActionIndex(((DefaultActionMessage)event.getMessage()).getContent());
				handleAction(player, virtualView);
				break;
			case GRAB_AMMO:
				model.grabAmmoCard(player, ((DefaultActionMessage)event.getMessage()).getContent());
				handleAction(player,virtualView);
				break;
			case GRAB_WEAPON:
				model.grabWeaponCard(player, ((DefaultActionMessage)event.getMessage()).getContent());
				handleAction(player,virtualView);
				break;
			case MOVE:
				model.movePlayerTo(player, ((MoveActionMessage)event.getMessage()).getCoordinates());
				handleAction(player,virtualView);
				break;
			case RELOAD:
				player.reload(((DefaultActionMessage)event.getMessage()).getContent());
				handleAction(player,virtualView);
				break;
			case WEAPON:
				Pair intString = player.getFiringWeapon().handleFire(((DefaultActionMessage)event.getMessage()).getContent());
				if(player.getFiringWeapon().doneFiring()){
					player.getFiringWeapon().reset();
					handleAction(player,virtualView);
				}
				else {
					virtualView.askWeapon(intString);
				}
				break;
			default: throw new RuntimeException("Received wrong type of message: "+ event.toString());
		}

	}

	void handleAction(Player player, VirtualView virtualView) {
		ActionType actionType = player.getDamageStatus().executeAction();
		switch (actionType){
			case MOVE:
				virtualView.askMove();
				break;
			case GRAB:
				virtualView.askGrab();
				break;
			case RELOAD:
				virtualView.askReload();
				break;
			case SHOOT:
				virtualView.askShoot();
				break;
			case END:
				//The MacroAction is already refilled.
				handleEnd(player, virtualView);
				break;
			default:
				Utils.logWarning("WTF");
				break;
		}
	}

	private void handleEnd(Player player, VirtualView virtualView) {
		if(player.getDamageStatus().hasActionLeft()){
			virtualView.askAction();
		}
		else {
			virtualView.askEnd();
		}
	}
}