package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.cards.powerups.QuestionContainer;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.network.message.CoordinatesAnswerMessage;
import it.polimi.se2019.network.message.DefaultActionMessage;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.Message;
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

	private VirtualViewsContainer virtualViewsContainer;
	private WeaponController weaponController;
	private Model model;

	private int powerupInExecution = -1;


	public TurnController(Model model, VirtualViewsContainer virtualViewsContainer) {
		this.virtualViewsContainer = virtualViewsContainer;
		this.model = model;
		this.weaponController = new WeaponController();
	}


	void processEvent(Event event) {
		//TODO: Control veridicity of the message.

		VirtualView virtualView = event.getVirtualView();
		String playerName = virtualView.getNickname();

		Utils.logInfo("TurnController -> processEvent(): processing an event received from \"" + playerName + "\" with a message of type " + event.getMessage().getMessageType() + " and subtype " + event.getMessage().getMessageSubtype() + ".");

		switch(event.getMessage().getMessageType()){
			case ACTION:
				model.setNextMacroAction(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case GRAB_AMMO:
				model.grabAmmoCard(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case GRAB_WEAPON:
				model.grabWeaponCard(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case MOVE:
				Coordinates playerChoice = ((CoordinatesAnswerMessage) event.getMessage()).getSingleCoordinates();
				if (model.getReachableCoordinatesOfTheCurrentPlayer().contains(playerChoice)) {
					model.movePlayerTo(playerName, playerChoice);
					handleNextAction(virtualView);
				} else {
					virtualView.askMove(model.getReachableCoordinatesOfTheCurrentPlayer());
				}
				break;
			case RELOAD:
				model.reloadWeapon(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case WEAPON:
				//TODO fix this warning
				Pair<String, List<String>> stringListString = model.playerWeaponHandleFire(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				if(model.isTheplayerDoneFiring(playerName)){
					model.resetPlayerCurrentWeapon(playerName);
					handleNextAction(virtualView);
				}
				else {
					virtualView.askChoice(stringListString.getFirst(), stringListString.getSecond());
				}
				break;
			case ON_TURN_POWERUP:
				int powerupIndex = ((IntMessage)event.getMessage()).getContent();
				if(model.canOnTurnPowerupBeActivated(playerName, powerupIndex)) {
					powerupInExecution = powerupIndex;
					handleNextPowerupStep(virtualView, null);
				}
				break;
			case POWERUP_INFO_OPTIONS:
			case POWERUP_INFO_COORDINATES:
				if(powerupInExecution != -1)
					handleNextPowerupStep(virtualView, event.getMessage());
				break;
			default: Utils.logError("Received wrong type of message: " + event.toString(), new IllegalStateException());
		}
	}

	private void  handleNextAction(VirtualView playerVirtualView) {
		ActionType actionType = model.getNextActionToExecute(playerVirtualView.getNickname());
		switch (actionType){
			case MOVE:
				if (model.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().isGrab())
					playerVirtualView.askMove(model.getEmptyReachableCoordinatesOfTheCurrentPlayer());
				else
					playerVirtualView.askMove(model.getReachableCoordinatesOfTheCurrentPlayer());
				break;
			case GRAB:
				playerVirtualView.askGrab(model.getGrabMessageType());
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
		String playerName = playerVirtualView.getNickname();
		if(model.doesThePlayerHaveActionsLeft(playerName)){
			playerVirtualView.askAction(model.getActivableOnTurnPowerups(playerName));
		} else {
			playerVirtualView.askEnd(model.getActivableOnTurnPowerups(playerName));
		}
	}

	private void handleNextPowerupStep(VirtualView virtualView, Message answer) {
		QuestionContainer questionContainer = model.activateOnTurnPowerup(virtualView.getNickname(), powerupInExecution, answer);
		if(questionContainer == null) {
			model.discardPowerupCard(virtualView.getNickname(), powerupInExecution);
			powerupInExecution = -1;
			handleEnd(virtualView);
		} else if(questionContainer.isAskString()) {
			virtualView.askPowerupChoice(questionContainer.getQuestion(), questionContainer.getOptions());
		} else if(questionContainer.isAskCoordinates()) {
			virtualView.askPowerupCoordinates(questionContainer.getQuestion(), questionContainer.getCoordinates());
		}
	}
}