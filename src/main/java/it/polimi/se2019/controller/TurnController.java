package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is in a lower level than GameController. It handles the logic relative to the action that the player can
 * perform inside of a turn.
 *
 * @author Marchingegno
 */
public class TurnController {

	private VirtualViewsContainer virtualViewsContainer;
	private Model model;

	/**
	 * The constructor of the class.
	 *
	 * @param model                 the model of this game.
	 * @param virtualViewsContainer the VirtualViewsContainer of this game's players.
	 */
	public TurnController(Model model, VirtualViewsContainer virtualViewsContainer) {
		this.virtualViewsContainer = virtualViewsContainer;
		this.model = model;
	}

	// ####################################
	// EVENT HANDLING METHODS
	// ####################################

	/**
	 * Process an event observed by the Controller.
	 * This method relies on the {@link MessageType} and {@link MessageSubtype} of the {@link Message} enclosed in the {@link Event}
	 *
	 * @param event the event observed.
	 */
	void processEvent(Event event) {
		//TODO: Control veridicity of the message.

		Utils.logInfo("TurnController -> processEvent(): processing an event received from \"" + event.getVirtualView().getNickname() + "\" with a message of type " + event.getMessage().getMessageType() + " and subtype " + event.getMessage().getMessageSubtype() + ".");

		switch (event.getMessage().getMessageType()) {
			case ACTION:
				handleActionEvent(event);
				break;
			case GRAB_AMMO:
				handleGrabAmmoEvent(event);
				break;
			case GRAB_WEAPON:
				handleGrabWeaponEvent(event);
				break;
			case SWAP_WEAPON:
				handleSwapWeaponEvent(event);
				break;
			case MOVE:
				handleMoveEvent(event);
				break;
			case RELOAD:
				handleReloadEvent(event);
				break;
			case WEAPON:
				handleWeaponEvent(event);
				break;
			case PAYMENT:
				handlePaymentEvent(event);
				break;
			case ACTIVATE_ON_TURN_POWERUP:
				handleActivateOnTurnPowerupEvent(event);
				break;
			case POWERUP:
				handlePowerupEvent(event);
				break;
			default:
				Utils.logError("Received wrong type of message: " + event.toString(), new IllegalStateException());
		}
	}

	/**
	 * Process an event of type ACTION.
	 * Event relative to the choice of a MacroAction by the player.
	 *
	 * @param event the event to be processed.
	 */
	private void handleActionEvent(Event event) {
//TODO check if the player can do the action
		model.setNextMacroAction(event.getVirtualView().getNickname(), ((IntMessage) event.getMessage()).getContent());
		handleNextMacroActionStep(event.getVirtualView());
	}

	/**
	 * Process an event of type GRAB_AMMO.
	 *
	 * @param event the event to be processed.
	 */
	private void handleGrabAmmoEvent(Event event) {
        model.grabAmmoCard(event.getVirtualView().getNickname());
		handleNextMacroActionStep(event.getVirtualView());
	}

	/**
	 * Process an event of type GRAB_WEAPON.
	 *
	 * @param event the event to be processed.
	 */
	private void handleGrabWeaponEvent(Event event) {
		if (!model.hasCurrentPlayerPayed())
			handlePayment(event.getVirtualView(), model.getPriceOfTheChosenWeapon(((IntMessage) event.getMessage()).getContent()), event);
		else {
			model.setPayed(false);
			model.grabWeaponCard(event.getVirtualView().getNickname(), ((IntMessage) event.getMessage()).getContent());
			handleNextMacroActionStep(event.getVirtualView());
		}
	}

	/**
	 * Process an event of type SWAP_WEAPON.
	 * Swapping a weapon if the player currently has already 3 weapons.
	 *
	 * @param event the event to be processed.
	 */
	private void handleSwapWeaponEvent(Event event) {
		if (!model.hasCurrentPlayerPayed())
			handlePayment(event.getVirtualView(), model.getPriceOfTheChosenWeapon(((SwapMessage) event.getMessage()).getIndexToGrab()), event);
		else {
			model.setPayed(false);
			SwapMessage swapMessage = (SwapMessage) event.getMessage();
			model.swapWeapons(swapMessage.getIndexToDiscard(), swapMessage.getIndexToGrab());
			handleNextMacroActionStep(event.getVirtualView());
		}
	}

	/**
	 * Process an event of type MOVE.
	 * A request by the player to be moved to a square.
	 *
	 * @param event the event to be processed.
	 */
	private void handleMoveEvent(Event event) {
		Coordinates playerChoice = ((CoordinatesAnswerMessage) event.getMessage()).getSingleCoordinates();
		if (model.getReachableCoordinatesOfTheCurrentPlayer().contains(playerChoice)) {
			model.movePlayerTo(event.getVirtualView().getNickname(), playerChoice);
			handleNextMacroActionStep(event.getVirtualView());
		} else {
			event.getVirtualView().askMove(model.getReachableCoordinatesOfTheCurrentPlayer());
		}
	}

	/**
	 * Process an event of type RELOAD.
	 *
	 * @param event the event to be processed.
	 */
	private void handleReloadEvent(Event event) {
		if (event.getMessage().getMessageSubtype() == MessageSubtype.REQUEST) {
			List<Integer> loadableWeapons = model.getLoadableWeapons(event.getVirtualView().getNickname());
			if (loadableWeapons.isEmpty()) {
				event.getVirtualView().askEnd(model.doesPlayerHaveActivableOnTurnPowerups(event.getVirtualView().getNickname()));
			} else {
				event.getVirtualView().askReload(loadableWeapons);
			}
		} else {
			if (!model.hasCurrentPlayerPayed())
				handlePayment(event.getVirtualView(), model.getPriceOfTheSelectedWeapon(((IntMessage) event.getMessage()).getContent()), event);
			else {
				model.setPayed(false);
				model.reloadWeapon(event.getVirtualView().getNickname(), ((IntMessage) event.getMessage()).getContent());
				VirtualView playerVirtualView = event.getVirtualView();
				if (model.isInAMacroAction(playerVirtualView.getNickname()))
					handleNextMacroActionStep(playerVirtualView);
				else
					playerVirtualView.askEnd(model.doesPlayerHaveActivableOnTurnPowerups(playerVirtualView.getNickname()));
			}
		}
	}

	/**
	 * Process an event of type WEAPON.
	 * Advancing the shooting process of the weapon with which the player is currently shooting.
	 *
	 * @param event the event to be processed.
	 */
	private void handleWeaponEvent(Event event) {

		if (model.isShootingWeapon(event.getVirtualView().getNickname())) {
			if (model.isPaymentStep(event.getVirtualView().getNickname())) {
				if (!model.hasCurrentPlayerPayed()) {
					handlePayment(event.getVirtualView(), model.getFiringCost(event.getVirtualView().getNickname(), ((IntMessage) event.getMessage()).getContent()), event);
				} else {
					model.setPayed(false);
					doWeaponStep(event.getVirtualView(), ((IntMessage) event.getMessage()).getContent());
				}
			} else {
				doWeaponStep(event.getVirtualView(), ((IntMessage) event.getMessage()).getContent());
			}
		}
		else
			initialWeaponActivation(event.getVirtualView(), ((IntMessage) event.getMessage()).getContent());
	}

	/**
	 * Process an event of type PAYMENT.
	 *
	 * @param event the event to be processed.
	 */
	private void handlePaymentEvent(Event event) {
		PaymentMessage paymentMessage = (PaymentMessage) event.getMessage();
		resolvePayment(event.getVirtualView(), paymentMessage.getPowerupsUsed(), paymentMessage.getPriceToPay());
	}

	/**
	 * Process an event of type ACTIVATE_ON_TURN_POWERUP.
	 *
	 * @param event the event to be processed.
	 */
	private void handleActivateOnTurnPowerupEvent(Event event) {
		Utils.logInfo("Model -> getAc");
		event.getVirtualView().askPowerupActivation(model.getActivableOnTurnPowerups(event.getVirtualView().getNickname()));
	}

	/**
	 * Process an event of type POWERUP.
	 *
	 * @param event the event to be processed.
	 */
	private void handlePowerupEvent(Event event) {
		if (model.isPowerupInExecution(event.getVirtualView().getNickname()))
			doPowerupStep(event.getVirtualView(), ((IntMessage) event.getMessage()).getContent());
		else
			initialPowerupActivation(event.getVirtualView(), ((IntMessage) event.getMessage()).getContent());
	}

	// ####################################
	// MACROACTION HANDLING METHODS
	// ####################################

	/**
	 * Handles the logic relative to a {@link it.polimi.se2019.utils.MacroAction}
	 * @param playerVirtualView the VirtualView of the player currently executing the MacroAction
	 */
	private void handleNextMacroActionStep(VirtualView playerVirtualView) {
		ActionType actionType = model.getNextActionToExecuteAndAdvance(playerVirtualView.getNickname());
		Utils.logInfo("TurnController -> handleNextMacroActionStep(): Performing " + actionType + " of " + model.getCurrentAction());
		switch (actionType) {
			case MOVE:
				playerVirtualView.askMove(model.getCoordinatesWherePlayerCanMove());
				break;
			case GRAB:
				handleGrabAction(playerVirtualView);
				break;
			case RELOAD:
				handleReloadAction(playerVirtualView);
				break;
			case SHOOT:
				handleShootAction(playerVirtualView);
				break;
			case END:
				//The MacroAction is already refilled.
				handleMacroActionEnd(playerVirtualView);
				break;
			default:
				Utils.logError("This action type cannot be processed.", new IllegalStateException());
				break;
		}
	}

	/**
	 * Handles a grab action.
	 *
	 * @param playerVirtualView the VirtualView of the player executing this action.
	 */
	private void handleGrabAction(VirtualView playerVirtualView) {
		if (model.getGrabMessageType() == MessageType.GRAB_WEAPON) {
			if (model.currPlayerHasWeaponInventoryFull())
				playerVirtualView.askSwapWeapon(model.getIndexesOfTheGrabbableWeaponCurrentPlayer());
			else
				playerVirtualView.askGrabWeapon(model.getIndexesOfTheGrabbableWeaponCurrentPlayer());
		} else {
            model.grabAmmoCard(playerVirtualView.getNickname());
			handleNextMacroActionStep(playerVirtualView);
		}
	}

	/**
	 * Handles a reload action.
	 *
	 * @param playerVirtualView the VirtualView of the player executing this action.
	 */
	private void handleReloadAction(VirtualView playerVirtualView) {
		List<Integer> loadableWeapons = model.getLoadableWeapons(playerVirtualView.getNickname());
		if (loadableWeapons.isEmpty())
			handleNextMacroActionStep(playerVirtualView);
		else
			playerVirtualView.askReload(loadableWeapons);
	}

	/**
	 * Handles a shoot action.
	 *
	 * @param playerVirtualView the VirtualView of the player executing this action.
	 */
	private void handleShootAction(VirtualView playerVirtualView) {
		List<Integer> activableWeapons = model.getActivableWeapons(playerVirtualView.getNickname());
		if (activableWeapons.isEmpty())
			handleNextMacroActionStep(playerVirtualView);
		else
			playerVirtualView.askShoot(activableWeapons);
	}

	/**
	 * Handles an end action.
	 * @param playerVirtualView the VirtualView of the player executing this action.
	 */
	private void handleMacroActionEnd(VirtualView playerVirtualView) {
		String playerName = playerVirtualView.getNickname();
		model.endAction(playerName);
		endOnTurnAction(playerVirtualView);
	}

	private void endOnTurnAction(VirtualView playerVirtualView) {
		String playerName = playerVirtualView.getNickname();
		if (model.doesThePlayerHaveActionsLeft(playerName)) {
			playerVirtualView.askAction(model.doesPlayerHaveActivableOnTurnPowerups(playerName), model.doesPlayerHaveLoadedWeapons(playerName));
		} else {
			playerVirtualView.askEnd(model.doesPlayerHaveActivableOnTurnPowerups(playerName));
		}
	}

	// ####################################
	// WEAPONS METHODS
	// ####################################

	/**
	 * Called when the player chooses the weapon to fire.
	 * @param virtualView the VirtualView of the player firing.
	 * @param indexOfWeapon the index of the weapon in the inventory of the player.
	 */
	private void initialWeaponActivation(VirtualView virtualView, int indexOfWeapon) {
		if (model.canWeaponBeActivated(virtualView.getNickname(), indexOfWeapon)) {
			QuestionContainer questionContainer = model.initialWeaponActivation(virtualView.getNickname(), indexOfWeapon);
			handleWeaponQuestionContainer(virtualView, questionContainer);
		}
	}

	/**
	 * Advance the shooting process of the weapon.
	 * @param virtualView the VirtualView of the player firing.
	 * @param choice the choice of the player.
	 */
	private void doWeaponStep(VirtualView virtualView, int choice) {
		QuestionContainer questionContainer = model.doWeaponStep(virtualView.getNickname(), choice);


		handleWeaponQuestionContainer(virtualView, questionContainer);
	}

	/**
	 * Handles the {@link QuestionContainer} that the weapon returns when doWeaponStep is called.
	 * @param virtualView the VirtualView of the player firing.
	 * @param questionContainer the QuestionContainer to be asked to the player.
	 */
	private void handleWeaponQuestionContainer(VirtualView virtualView, QuestionContainer questionContainer) {
		if (model.isTheWeaponConcluded(virtualView.getNickname())) {
			Utils.logWeapon("Weapon ended.");
			model.handleWeaponEnd(virtualView.getNickname());
			if (model.doesPlayerHaveActivableOnShootPowerups(virtualView.getNickname()))
				virtualView.askPowerupActivation(model.getActivableOnShootPowerups(virtualView.getNickname()));
			else
				handleInitialOnDamagePowerup();
		} else {
			if (questionContainer == null || questionContainer.isThisQuestionContainerUseless()) {
				Utils.logWarning("This QuestionContainer is either null or useless.");
				throw new IllegalStateException("Question container null");
			}
			Utils.logWeapon("Asking this question container to view:");
			Utils.logWeapon(questionContainer.getQuestion());
			virtualView.askWeaponChoice(questionContainer);
		}
	}

	// ####################################
	// POWERUPS METHODS
	// ####################################

	/**
	 * Checks if the there are players that have a powerup of use case ON_DAMAGE.
	 * If there are any player with a ON_DAMAGE powerup, the virtualview will be ask to use a powerup.
	 */
	private void handleInitialOnDamagePowerup() {
		String damagedPlayer = model.getNextPlayerWaitingForDamagePowerups();
		if (damagedPlayer == null) {
			handleNextMacroActionStep(virtualViewsContainer.getVirtualViewFromPlayerName(model.getCurrentPlayerName()));
		} else {
			virtualViewsContainer.getVirtualViewFromPlayerName(damagedPlayer).askOnDamagePowerupActivation(model.getActivableOnDamagePowerups(damagedPlayer, model.getCurrentPlayerName()), model.getCurrentPlayerName());
		}
	}

	/**
	 * Initial activation of a powerup. This method is called when activating a powerup.
	 * @param virtualView the virtualview of the player activating the powerup.
	 * @param indexOfPowerup the powerup to activate.
	 */
	private void initialPowerupActivation(VirtualView virtualView, int indexOfPowerup) {
		// If player doesn't want to use any powerup.
		if (indexOfPowerup == -1) {
			handleActionEndForPowerup(virtualView);
		}

		if (model.canPowerupBeActivated(virtualView.getNickname(), indexOfPowerup)) {
			QuestionContainer questionContainer = model.initialPowerupActivation(virtualView.getNickname(), indexOfPowerup);
			handlePowerupQuestionContainer(virtualView, questionContainer);
		}
	}

	/**
	 * Advance the process of activating a powerup.
	 * @param virtualView the VirtualView of the player activating the powerup.
	 * @param choice the choice of the player.
	 */
	private void doPowerupStep(VirtualView virtualView, int choice) {
		QuestionContainer questionContainer = model.doPowerupStep(virtualView.getNickname(), choice);
		handlePowerupQuestionContainer(virtualView, questionContainer);
	}

	/**
	 * Handles the {@link QuestionContainer} that the powerup returns when doPowerupStep or initialPowerupActivation is called.
	 * @param virtualView the VirtualView of the player activating the powerup.
	 * @param questionContainer the QuestionContainer to be asked to the player.
	 */
	private void handlePowerupQuestionContainer(VirtualView virtualView, QuestionContainer questionContainer) {
		if (model.isThePowerupConcluded(virtualView.getNickname())) {
			model.handlePowerupEnd(virtualView.getNickname());
			handleActionEndForPowerup(virtualView);
		} else {
			virtualView.askPowerupChoice(questionContainer);
		}
	}

	/**
	 * Called when a powerup has finished its activation.
	 * @param virtualView the player that finished the powerup activation.
	 */
	private void handleActionEndForPowerup(VirtualView virtualView) {
		if (virtualView.getNickname().equals(model.getCurrentPlayerName()) && model.isPlayerWaitingForDamagePowerupsEmpty()) {
			// The player that finished the powerup activation is the current player of the model, and there aren't any players left for ON_DAMAGE powerups.
			// So we can end the action.
			//If the player is in a MacroAction (ex: he used targeting scopre while firing, we need to continue the MacroAction
			//Else, we need to resume its turn, whether he has actions left or no.
			if (model.isInAMacroAction(virtualView.getNickname())) {
				handleNextMacroActionStep(virtualView);
			} else {
				endOnTurnAction(virtualView);
			}
		} else {
			// The player that finished the powerup activation is not the current player or there are players that have an ON_DAMAGE powerup.
			// So we need to check if there are other players that have an ON_DAMAGE powerup.
			handleInitialOnDamagePowerup();
		}
	}

	// ####################################
	// PAYMENT METHODS
	// ####################################

    /**
     * Executes the transaction.
     *
     * @param virtualView              the VirtualView of the player paying.
     * @param indexesOfThePowerupToUse indexes of the powerups the player has decided to use in order to pay.
     * @param priceToPay               the ammo to be payed for this transaction.
     */
    private void resolvePayment(VirtualView virtualView, List<Integer> indexesOfThePowerupToUse, List<AmmoType> priceToPay) {
        model.pay(virtualView.getNickname(), priceToPay, indexesOfThePowerupToUse);
		processEvent(model.resumeAction());
	}

	/**
	 * Handle a payment.
	 * @param virtualView the VirtualView of the player paying.
	 * @param ammoToPay the ammo to be payed for this transaction.
	 * @param eventToSave the event to be put on hold while the player is completing the payment.
	 */
	private void handlePayment(VirtualView virtualView, List<AmmoType> ammoToPay, Event eventToSave) {
		model.saveEvent(eventToSave);
		if (!model.canUsePowerupToPay(virtualView.getNickname(), ammoToPay)) {
			resolvePayment(virtualView, new ArrayList<>(), ammoToPay);
		} else {
			virtualView.askToPay(ammoToPay, model.canAffordWithOnlyAmmo(virtualView.getNickname(), ammoToPay));
		}
	}

}
