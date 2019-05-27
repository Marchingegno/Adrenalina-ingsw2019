package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.network.message.CoordinatesAnswerMessage;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.network.message.SwapMessage;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.QuestionContainer;
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
				//TODO check if the player can do the action
				model.setNextMacroAction(playerName, ((IntMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case GRAB_AMMO:
				model.grabAmmoCard(playerName, ((IntMessage) event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case GRAB_WEAPON:
				model.grabWeaponCard(playerName, ((IntMessage) event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case SWAP_WEAPON:
				SwapMessage swapMessage = (SwapMessage) event.getMessage();
				model.swapWeapons(swapMessage.getIndexToDiscard(), swapMessage.getIndexToGrab());
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
				model.reloadWeapon(playerName, ((IntMessage)event.getMessage()).getContent());
				//This method should not be called because the macroaction is already refilled and it will start another turn.
//				handleNextAction(virtualView);
				//For now,you can reload only one weapon.
				virtualView.askEnd(model.doesPlayerHaveActivableOnTurnPowerups(playerName));
				break;
			case WEAPON:
				if(model.isShootingWeapon(playerName))
					doWeaponStep(virtualView, ((IntMessage) event.getMessage()).getContent());
				else
					initialWeaponActivation(virtualView, ((IntMessage) event.getMessage()).getContent());
				break;
			case ACTIVATE_POWERUP:
				virtualView.askPowerupActivation(model.getActivableOnTurnPowerups(playerName));
				break;
			case POWERUP:
				if(model.isPowerupInExecution(playerName))
					doPowerupStep(virtualView, ((IntMessage) event.getMessage()).getContent());
				else
					initialPowerupActivation(virtualView, ((IntMessage) event.getMessage()).getContent());
				break;
			default: Utils.logError("Received wrong type of message: " + event.toString(), new IllegalStateException());
		}
	}

	private void handleNextAction(VirtualView playerVirtualView) {
		ActionType actionType = model.getNextActionToExecuteAndAdvance(playerVirtualView.getNickname());
		Utils.logInfo("TurnController -> handleNextAction(): Performing " + actionType + " of " + model.getCurrentAction());
		switch (actionType){
			case MOVE:
				playerVirtualView.askMove(model.getCoordinatesWherePlayerCanMove());
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
				List<Integer> activableWeapons = model.getActivableWeapons(playerVirtualView.getNickname());
				if (activableWeapons.isEmpty())
					handleNextAction(playerVirtualView);
				else
					playerVirtualView.askShoot(activableWeapons);
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
			playerVirtualView.askAction(model.doesPlayerHaveActivableOnTurnPowerups(playerName), model.doesPlayerHaveLoadedWeapons(playerName));
		} else {
			playerVirtualView.askEnd(model.doesPlayerHaveActivableOnTurnPowerups(playerName));
		}
	}


	// ####################################
	// WEAPONS METHODS
	// ####################################

	private void initialWeaponActivation(VirtualView virtualView, int indexOfWeapon) {
		if(model.canWeaponBeActivated(virtualView.getNickname(), indexOfWeapon)) {
			QuestionContainer questionContainer = model.initialWeaponActivation(virtualView.getNickname(), indexOfWeapon);
			handleWeaponQuestionContainer(virtualView, questionContainer);
		}
	}

	private void doWeaponStep(VirtualView virtualView, int choice) {
		QuestionContainer questionContainer = model.doWeaponStep(virtualView.getNickname(), choice);
		handleWeaponQuestionContainer(virtualView, questionContainer);
	}

	private void handleWeaponQuestionContainer(VirtualView virtualView, QuestionContainer questionContainer) {
		if (questionContainer == null || questionContainer.isThisQuestionContainerUseless()) {
			Utils.logWarning("This QuestionContainer is either null or useless.");
			model.handleWeaponEnd(virtualView.getNickname());
			handleNextAction(virtualView);
		} else {
			virtualView.askWeaponChoice(questionContainer);
		}
	}


	// ####################################
	// POWERUPS METHODS
	// ####################################

	private void initialPowerupActivation(VirtualView virtualView, int indexOfPowerup) {
		if(model.canOnTurnPowerupBeActivated(virtualView.getNickname(), indexOfPowerup)) {
			QuestionContainer questionContainer = model.initialPowerupActivation(virtualView.getNickname(), indexOfPowerup);
			handlePowerupQuestionContainer(virtualView, questionContainer);
		}
	}

	private void doPowerupStep(VirtualView virtualView, int choice) {
		QuestionContainer questionContainer = model.doPowerupStep(virtualView.getNickname(), choice);
		handlePowerupQuestionContainer(virtualView, questionContainer);
	}

	private void handlePowerupQuestionContainer(VirtualView virtualView, QuestionContainer questionContainer) {
		if(questionContainer == null) {
			model.handlePowerupEnd(virtualView.getNickname());
			handleEnd(virtualView);
		} else {
			virtualView.askPowerupChoice(questionContainer);
		}
	}
}