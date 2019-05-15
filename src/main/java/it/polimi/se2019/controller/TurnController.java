package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.network.message.DefaultActionMessage;
import it.polimi.se2019.network.message.MoveActionMessage;
import it.polimi.se2019.utils.ActionType;
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
	private WeaponController weaponController;
	private List<VirtualView> virtualViews;

	public TurnController(Model model, List<VirtualView> virtualViews) {
		this.model = model;
		this.weaponController = new WeaponController();
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
		String playerName = virtualView.getPlayerName();

		Utils.logInfo("TurnController: processing this event " + event.toString());
		switch(event.getMessage().getMessageType()){
			case ACTION:
				model.setNextMacroAction(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case GRAB_AMMO:
				model.grabAmmoCard(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				Controller.sendUpdatedReps(virtualViews);
				break;
			case GRAB_WEAPON:
				model.grabWeaponCard(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				Controller.sendUpdatedReps(virtualViews);
				break;
			case MOVE:
				model.movePlayerTo(playerName, ((MoveActionMessage)event.getMessage()).getCoordinates());
				handleNextAction(virtualView);
				Controller.sendUpdatedReps(virtualViews);
				break;
			case RELOAD:
				model.reloadWeapon(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				Controller.sendUpdatedReps(virtualViews);
				break;
			default: Utils.logError("Received wrong type of message: " + event.toString(), new IllegalStateException());
		}

	}

	private void handleNextAction(VirtualView playerVirtualView) {
		ActionType actionType = model.getNextActionToExecute(playerVirtualView.getPlayerName());
		switch (actionType){
			case MOVE:
				playerVirtualView.askMove();
				break;
			case GRAB:
				playerVirtualView.askGrab();
				break;
			case RELOAD:
				playerVirtualView.askReload();
				break;
			case SHOOT:
				playerVirtualView.askShoot();
				break;
			case END:
				//The MacroAction is already refilled.
				handleEnd(playerVirtualView);
				break;
			default:
				Utils.logError("This action type cannot be processed.", new IllegalStateException());
				break;
		}
	}

	private void handleEnd(VirtualView playerVirtualView) {
		String playerName = playerVirtualView.getPlayerName();
		if(model.doesThePlayerHaveActionsLeft(playerName)){
			playerVirtualView.askAction();
		} else {
			playerVirtualView.askEnd();
		}
	}
}