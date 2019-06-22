package it.polimi.se2019.view.server;

import it.polimi.se2019.GameTester;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.model.player.damagestatus.DamageStatusRep;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.ModelRep;
import it.polimi.se2019.view.client.cli.RepPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VirtualViewDriver extends VirtualView {

	private final boolean TEST_MOVE;
	private final boolean TEST_SHOOT;
	private final boolean TEST_POWERUP;
	private boolean displayReps;

	private static int numberOfTurns = 0;

	private ModelRep modelRep = new ModelRep();
	private String nickname;
	private RepPrinter repPrinter = new RepPrinter(modelRep);


	public VirtualViewDriver(String nickname, final boolean TEST_MOVE, final boolean TEST_SHOOT, final boolean TEST_POWERUP) {
		super(null);
		this.nickname = nickname;
		this.TEST_MOVE = TEST_MOVE;
		this.TEST_SHOOT = TEST_SHOOT;
		this.TEST_POWERUP = TEST_POWERUP;
	}


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	@Override
	public void onClientDisconnected() {
		// TODO inform controller/model and suspend the player
	}

	@Override
	public void sendReps() {
		throw new IllegalStateException("This method must be overridden.");
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public void askAction(boolean activablePowerups, boolean activableWeapons) {
		DamageStatusRep damageStatusRep = modelRep.getClientPlayerRep().getDamageStatusRep();

		// Create list with possible answers.
		List<Integer> possibleAnswers = new ArrayList<>();
		int i;
		for (i = 0; i < damageStatusRep.numOfMacroActions(); i++) {
			// Move actions.
			if(damageStatusRep.getMacroActionName(i).equals("Move")) {
				if(TEST_MOVE)
					possibleAnswers.add(i);
			} else if(damageStatusRep.getMacroActionName(i).equals("Shoot")) {
				if(TEST_SHOOT) {
					if(activableWeapons || !damageStatusRep.isShootWithoutReload(i)) // If weapons loaded.
						possibleAnswers.add(i);
				}
			} else {
				possibleAnswers.add(i);
			}
		}
		if(activablePowerups && TEST_POWERUP)
			possibleAnswers.add(i + 1);

		int randomIndex = new Random().nextInt(possibleAnswers.size());
		int answer = possibleAnswers.get(randomIndex);
		if(answer == i + 1) // If answer is powerup.
			sendMessageToController(new Message(MessageType.ACTIVATE_POWERUP, MessageSubtype.ANSWER));
		else
			sendMessageToController(new IntMessage(answer, MessageType.ACTION, MessageSubtype.ANSWER));
	}

	@Override
	public void askGrabWeapon(List<Integer> indexesOfTheGrabbableWeapons) {
		int randomIndex = new Random().nextInt(indexesOfTheGrabbableWeapons.size());
		Utils.logInfo("VirtualViewDriver -> askGrabWeapon(): created random answer for grab weapon: " + indexesOfTheGrabbableWeapons.get(randomIndex));
		sendMessageToController(new IntMessage(indexesOfTheGrabbableWeapons.get(randomIndex),  MessageType.GRAB_WEAPON, MessageSubtype.ANSWER));
	}

	@Override
	public void askSwapWeapon(List<Integer> indexesOfTheGrabbableWeapons) {
		int randomIndex = new Random().nextInt(indexesOfTheGrabbableWeapons.size());
		int randomAnswer = new Random().nextInt(GameConstants.MAX_WEAPON_CARDS_PER_PLAYER);
		Utils.logInfo("VirtualViewDriver -> askSwapWeapon(): created random answer for swap weapon: grab " + indexesOfTheGrabbableWeapons.get(randomIndex) + " discard " + randomAnswer);
		sendMessageToController(new SwapMessage(indexesOfTheGrabbableWeapons.get(randomIndex), randomAnswer, MessageType.SWAP_WEAPON));
	}

	@Override
	public void askMove(List<Coordinates> reachableCoordinates) {
		int randomIndex = new Random().nextInt(reachableCoordinates.size());
		Utils.logInfo("VirtualViewDriver -> askMove(): created random answer for move: " + reachableCoordinates.get(randomIndex));
		sendMessageToController(new CoordinatesAnswerMessage(reachableCoordinates.get(randomIndex), MessageType.MOVE));
	}

	@Override
	public void askShoot(List<Integer> shootableWeapons) {
		int randomIndex = new Random().nextInt(shootableWeapons.size());
		Utils.logInfo("VirtualViewDriver -> askShoot(): created random answer for shoot: " + shootableWeapons.get(randomIndex));
		sendMessageToController(new IntMessage(shootableWeapons.get(randomIndex), MessageType.WEAPON, MessageSubtype.ANSWER));
	}

	@Override
	public void askReload(List<Integer> loadableWeapons) {
		int randomIndex = new Random().nextInt(loadableWeapons.size());
		Utils.logInfo("VirtualViewDriver -> askReload(): created random answer for reload: " + loadableWeapons.get(randomIndex));
		sendMessageToController(new IntMessage(loadableWeapons.get(randomIndex), MessageType.RELOAD, MessageSubtype.ANSWER));
	}

	@Override
	public void askSpawn() {
		List<PowerupCardRep> powerupCards = modelRep.getClientPlayerRep().getPowerupCards();
		int randomAnswer = new Random().nextInt(powerupCards.size());
		Utils.logInfo("VirtualViewDriver -> askSpawn(): created random answer for spawn: " + randomAnswer);
		sendMessageToController(new IntMessage(randomAnswer, MessageType.SPAWN, MessageSubtype.ANSWER));
	}

	@Override
	public void askWeaponChoice(QuestionContainer questionContainer) {
		askQuestionContainerAndSendAnswer(questionContainer, MessageType.WEAPON);
	}

	@Override
	public void askPowerupActivation(List<Integer> activablePowerups) {
		int randomIndex = new Random().nextInt(activablePowerups.size());
		Utils.logInfo("VirtualViewDriver -> askPowerupActivation(): created random answer for powerup activation: " + activablePowerups.get(randomIndex));
		sendMessageToController(new IntMessage(activablePowerups.get(randomIndex), MessageType.POWERUP, MessageSubtype.ANSWER));
	}

	@Override
	public void askPowerupChoice(QuestionContainer questionContainer) {
		askQuestionContainerAndSendAnswer(questionContainer, MessageType.POWERUP);
	}

	@Override
	public void askEnd(boolean activablePowerups) {
		int randomNumber;
		if(activablePowerups && TEST_POWERUP)
			randomNumber = new Random().nextInt(3);
		else
			randomNumber = new Random().nextInt(2);

		// End turn.
		if(randomNumber == 0) {
			if(canTestContinue()) {
				Utils.logInfo("VirtualViewDriver -> askEnd(): created random answer for End: END_TURN");
				sendMessageToController(new Message(MessageType.END_TURN, MessageSubtype.ANSWER));
			}
		}

		// Reload.
		else if(randomNumber == 1) {
			Utils.logInfo("VirtualViewDriver -> askEnd(): created random answer for End: RELOAD");
			sendMessageToController(new Message(MessageType.RELOAD, MessageSubtype.REQUEST));
		}

		// Powerup.
		else if(randomNumber == 2) {
			Utils.logInfo("VirtualViewDriver -> askEnd(): created random answer for End: ACTIVATE_POWERUP");
			sendMessageToController(new Message(MessageType.ACTIVATE_POWERUP, MessageSubtype.ANSWER)); // Powerup activation.
		}
	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {
		modelRep.setGameBoardRep(gameBoardRepToUpdate);
		Utils.logRep("Updated " + getNickname() + "'s Game Board rep");
	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {
		modelRep.setGameMapRep(gameMapRepToUpdate);
		Utils.logRep("Updated " + getNickname() + "'s Game Map rep");
	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {
		modelRep.setPlayerRep(playerRepToUpdate);
		Utils.logRep("Updated " + getNickname() + "'s Player rep of " + playerRepToUpdate.getPlayerName());
	}

	@Override
	public void askToPay(List<AmmoType> priceToPay, boolean canAffordAlsoWithAmmo) {
		List<Integer> answer = new ArrayList<>();
		List<AmmoType> price = new ArrayList<>(priceToPay);

		List<PowerupCardRep> powerupCardReps = new ArrayList<>(modelRep.getClientPlayerRep().getPowerupCards());
		int choice;
		int numOfOptions;
		do {
			//eliminates all powerups that cannot be used to pay
			for (int i = 0; i < powerupCardReps.size(); i++) {
				PowerupCardRep powerupCardRep = powerupCardReps.get(i);
				if (!price.contains(powerupCardRep.getAssociatedAmmo()))
					powerupCardReps.remove(powerupCardRep);
			}
			numOfOptions = powerupCardReps.size();
			choice = new Random().nextInt(canAffordAlsoWithAmmo ? numOfOptions - 1 : numOfOptions);
			if (choice != (numOfOptions)) {
				answer.add(choice);
				price.remove(powerupCardReps.get(choice).getAssociatedAmmo());
				powerupCardReps.remove(choice);
			}
		} while (!powerupCardReps.isEmpty() && choice != (numOfOptions));

		Utils.logInfo("VirtualViewDriver -> askToPay(): created random answer for payment: " + answer);
		sendMessageToController(new PaymentMessage(priceToPay, MessageSubtype.ANSWER).setPowerupsUsed(answer));
	}

	// ####################################
	// PUBLIC METHODS
	// ####################################

	public void setDisplayReps(boolean displayReps) {
		this.displayReps = displayReps;
	}


	// ####################################
	// PROTECTED METHODS (for subclasses)
	// ####################################

	protected void showRep() {
		if(displayReps && modelRep.getGameBoardRep() != null && modelRep.getGameMapRep() != null && modelRep.getPlayersRep().size() >= modelRep.getGameBoardRep().getNumberOfPlayers())
			repPrinter.displayGame();
	}

	protected void sendMessageToController(Message message) {
		throw new IllegalStateException("This method must be overridden.");
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	private void askQuestionContainerAndSendAnswer(QuestionContainer questionContainer, MessageType messageType) {
		int randomIndex;
		if(questionContainer.isAskString()) {
			randomIndex = new Random().nextInt(questionContainer.getOptions().size());
			Utils.logInfo("VirtualViewDriver -> askQuestionContainerAndSendAnswer(): created random answer for QuestionContainer with strings: " + questionContainer.getOptions().get(randomIndex));
			sendMessageToController(new IntMessage(randomIndex, messageType, MessageSubtype.ANSWER));
		} else if(questionContainer.isAskCoordinates()) {
			randomIndex = new Random().nextInt(questionContainer.getCoordinates().size());
			Utils.logInfo("VirtualViewDriver -> askQuestionContainerAndSendAnswer(): created random answer for QuestionContainer with coordinates: " + questionContainer.getCoordinates().get(randomIndex));
			sendMessageToController(new IntMessage(randomIndex, messageType, MessageSubtype.ANSWER));
		} else {
			Utils.logError("QuestionContainer doesn't contain a question!", new IllegalArgumentException());
		}
	}

	private boolean canTestContinue() {
		numberOfTurns++;
		if(numberOfTurns < GameTester.MAX_NUMBER_OF_TURNS) {
			Utils.logInfo(Color.getColoredString("##################", Color.CharacterColorType.YELLOW) +  " TURN " + numberOfTurns + " ENDED (" + getNickname() + "'s turn) " + Color.getColoredString("##################", Color.CharacterColorType.YELLOW));
			return true;
		} else {
			Utils.logInfo(Color.getColoredString("##################", Color.CharacterColorType.GREEN) + " TEST GAME FINISHED CORRECTLY " + Color.getColoredString("##################", Color.CharacterColorType.GREEN));
			return false;
		}
	}
}