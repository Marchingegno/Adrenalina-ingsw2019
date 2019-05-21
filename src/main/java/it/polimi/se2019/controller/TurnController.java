package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

/**
 * This class is in a lower level than GameController. It handles the logic relative
 * @author Marchingegno
 */
public class TurnController{

	private VirtualViewsContainer virtualViewsContainer;
	private Model model;


	public TurnController(Model model, VirtualViewsContainer virtualViewsContainer) {
		this.virtualViewsContainer = virtualViewsContainer;
		this.model = model;
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
				model.grabWeaponCard(playerName, ((IntMessage) event.getMessage()).getContent());
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
				QuestionContainer questionContainer = model.playerWeaponHandleFire(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				if(model.isTheplayerDoneFiring(playerName)){
					model.resetPlayerCurrentWeapon(playerName);
					handleNextAction(virtualView);
				}
				else {
					virtualView.askWeaponChoice(questionContainer);
				}
				break;
			case POWERUP:
				if(model.isPowerupInExecution(playerName))
					doPowerupStep(virtualView, event.getMessage());
				else
					initialPowerupActivation(virtualView, event.getMessage());
				break;
			default: Utils.logError("Received wrong type of message: " + event.toString(), new IllegalStateException());
		}
	}

	private void  handleNextAction(VirtualView playerVirtualView) {
		ActionType actionType = model.getNextActionToExecute(playerVirtualView.getNickname());
		switch (actionType){
			case MOVE:
				if (model.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().isGrab())
					playerVirtualView.askMove(model.getCoordinatesWhereCurrentPlayerCanGrab());
				else
					playerVirtualView.askMove(model.getReachableCoordinatesOfTheCurrentPlayer());
				break;
			case GRAB:
				if (model.getGrabMessageType() == MessageType.GRAB_WEAPON) {
					if (model.currPlayerHasWeaponInventoryFull())
						playerVirtualView.askSwapWeapon(model.getIndexesOfTheGrabbableWeaponCurrentPlayer());
					else
						playerVirtualView.askGrabWeapon(model.getIndexesOfTheGrabbableWeaponCurrentPlayer());
				} else {
					model.grabAmmoCard(playerVirtualView.getNickname(), 0);
					handleNextAction(playerVirtualView);
				}
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


	// ####################################
	// POWERUPS METHODS
	// ####################################

	private void initialPowerupActivation(VirtualView virtualView, Message answer) {
		int indexOfPowerup = ((IntMessage)answer).getContent();
		if(model.canOnTurnPowerupBeActivated(virtualView.getNickname(), indexOfPowerup)) {
			QuestionContainer questionContainer = model.initialPowerupActivation(virtualView.getNickname(), indexOfPowerup);
			handlePowerupQuestionContainer(virtualView, questionContainer);
		}
	}

	private void doPowerupStep(VirtualView virtualView, Message answer) {
		QuestionContainer questionContainer = model.doPowerupStep(virtualView.getNickname(), answer);
		handlePowerupQuestionContainer(virtualView, questionContainer);
	}

	private void handlePowerupQuestionContainer(VirtualView virtualView, QuestionContainer questionContainer) {
		if(questionContainer == null) {
			model.discardPowerupCardInExecution(virtualView.getNickname());
			handleEnd(virtualView);
		} else {
			virtualView.askPowerupChoice(questionContainer);
		}
	}
}