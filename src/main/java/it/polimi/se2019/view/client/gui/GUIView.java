package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.cards.CardRep;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.model.cards.weapons.WeaponRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.PlayerRepPosition;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.RemoteView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIView extends RemoteView {

	private LoginController loginController;
	private Scene loginScene;

	private LobbyController lobbyController;
	private Scene lobbyScene;

	private MapAndSkullsController mapAndSkullsController;
	private Scene mapAndSklullsScene;

	private GameBoardController gameBoardController;
	private Scene gameBoardScene;

	private PowerupChoiceController powerupChoiceController;
	private Stage powerupChoiceStage;

	private WeaponChoiceController weaponChoiceController;
	private Stage weaponChoiceStage;

	private AskStringController askStringController;
	private Stage askStringStage;

	private EndGameController endGameController;
	private Stage endGameStage;

	private Stage window;

	public GUIView(Stage window) {
		this.window = window;
		loadLoginController();
		loadLobbyController();
		loadMapsAndSkulls();
		loadGameBoard();
		loadPowerupChoice();
		loadWeaponChoice();
		loadAskString();
		loadEndGame();
	}

	private void loadLoginController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
		try {
			Parent root = loader.load();
			window.setTitle("Adrenaline");
			loginScene = new Scene(root);
			loginController = loader.getController();
			loginController.setGui(this);
		} catch (IOException e) {
			Utils.logError("Error loading loginController", e);
		}
	}

	private void loadLobbyController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Lobby.fxml"));
		try {
			Parent root = loader.load();
			window.setTitle("Adrenaline");
			lobbyScene = new Scene(root);
			lobbyController = loader.getController();
		} catch (IOException e) {
			Utils.logError("Error loading lobbyController", e);
		}
	}

	private void loadMapsAndSkulls() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MapAndSkulls.fxml"));
		try {
			Parent root = loader.load();
			mapAndSklullsScene = new Scene(root);
			mapAndSkullsController = loader.getController();
			mapAndSkullsController.setGui(this);
			window.setTitle("Adrenaline");
		} catch (IOException e) {
			Utils.logError("Error loading mapsAndSkullsController", e);
		}
	}

	private void loadGameBoard() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GameBoard.fxml"));
		try {
			Parent root = loader.load();
			gameBoardScene = new Scene(root);
			gameBoardController = loader.getController();
			window.setTitle("Adrenaline");
		} catch (IOException e) {
			Utils.logError("Error loading gameBoardController", e);
		}
	}

	private void loadPowerupChoice() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PowerupChoice.fxml"));
		try {
			Parent root = loader.load();
			Scene powerupChoiceScene = new Scene(root);
			powerupChoiceStage = new Stage();
			powerupChoiceStage.setTitle("Adrenaline");
			powerupChoiceStage.setResizable(false);
			powerupChoiceStage.setOnCloseRequest(event ->
					Platform.runLater(() -> powerupChoiceStage.show())
			);
			powerupChoiceStage.setScene(powerupChoiceScene);
			powerupChoiceController = loader.getController();
			powerupChoiceController.setGuiAndStage(this, powerupChoiceStage);
		} catch (IOException e) {
			Utils.logError("Error loading powerupChoice", e);
		}
	}

	private void loadWeaponChoice() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/WeaponChoice.fxml"));
		try {
			Parent root = loader.load();
			Scene weaponChoiceScene = new Scene(root);
			weaponChoiceStage = new Stage();
			weaponChoiceStage.setResizable(false);
			weaponChoiceStage.setTitle("Adrenaline");
			weaponChoiceStage.setOnCloseRequest(event ->
					Platform.runLater(() -> weaponChoiceStage.show())
			);
			weaponChoiceStage.setScene(weaponChoiceScene);
			weaponChoiceController = loader.getController();
			weaponChoiceController.setGuiAndStage(this, weaponChoiceStage);
		} catch (IOException e) {
			Utils.logError("Error loading weaponChoice", e);
		}
	}

	private void loadAskString() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AskString.fxml"));
		try {
			Parent root = loader.load();
			Scene askStringScene = new Scene(root);
			askStringStage = new Stage();
			askStringStage.setTitle("Adrenaline");
			askStringStage.setResizable(false);
			askStringStage.setScene(askStringScene);
			askStringController = loader.getController();
			askStringController.setGuiAndStage(this, askStringStage);
			askStringStage.setOnCloseRequest(event ->
					Platform.runLater(() -> askStringStage.show())
			);
		} catch (IOException e) {
			Utils.logError("Error loading askString", e);
		}
	}

	private void loadEndGame() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/EndGame.fxml"));
		try {
			Parent root = loader.load();
			endGameStage = new Stage();
			Scene endGameScene = new Scene(root);
			endGameStage.setTitle("Adrenaline");
			endGameStage.setResizable(false);
			endGameStage.setScene(endGameScene);
			endGameStage.initModality(Modality.APPLICATION_MODAL);
			endGameController = loader.getController();
		} catch (IOException e) {
			Utils.logError("Error loading endGame", e);
		}
	}

	@Override
	public void askForConnectionAndStartIt() {
	}

	@Override
	public void failedConnectionToServer() {
		JOptionPane.showMessageDialog(null, "Failed to connect to the server. Try again later.", "Failed Connection", JOptionPane.ERROR_MESSAGE);
		Client.terminateClient();
	}

	@Override
	public void lostConnectionToServer() {
		JOptionPane.showMessageDialog(null, "Lost connection with the server. Please restart the game.", "Lost Connection", JOptionPane.ERROR_MESSAGE);
		Client.terminateClient();
	}

	@Override
	public void askNickname() {
		Platform.runLater(() -> {
			window.setScene(loginScene);
			window.show();
		});
	}

	@Override
	public void askNicknameError() {
		Platform.runLater(() ->
				loginController.nicknameAlreadyChoosen()
		);
	}

	@Override
	public void displayWaitingPlayers(List<String> waitingPlayers) {
		Platform.runLater(() -> {
			lobbyController.showNicknames(waitingPlayers);
			window.setScene(lobbyScene);
			window.show();
		});
	}

	@Override
	public void displayTimerStarted(long delayInMs) {
		Platform.runLater(() ->
				lobbyController.startTimer()
		);
	}

	@Override
	public void displayTimerStopped() {
		Platform.runLater(() ->
				lobbyController.stopTimer()
		);
	}

	@Override
	public void askMapAndSkullsToUse() {
		Platform.runLater(() -> {
			List<String> mapsName = new ArrayList<>();
			for (GameConstants.MapType map : GameConstants.MapType.values()) {
				mapsName.add(map.getMapName());
			}
			mapAndSkullsController.set(mapsName, GameConstants.MIN_SKULLS, GameConstants.MAX_SKULLS);
			window.setScene(mapAndSklullsScene);
			window.show();
		});
	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {
		String message = "Average of voted skulls: " + skulls + ".\n" +
				"Most voted map: " + mapType.getDescription() + "\n\n" +
				"Match ready to start!";
		JOptionPane.showMessageDialog(null, message, "Starting Match...", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void askWeaponChoice(QuestionContainer questionContainer) {
		askQuestionContainerAndSendAnswer(questionContainer, MessageType.WEAPON);
	}

	@Override
	public void askPowerupActivation(List<Integer> activablePowerups) {
		Platform.runLater(() -> {
			powerupChoiceController.setPowerups(getModelRep().getClientPlayerRep().getPowerupCards());
			powerupChoiceController.activatePowerupsButtons(activablePowerups);
			powerupChoiceController.activateNoPowerupButton(true);
			powerupChoiceController.setTitle("Choose the powerup you want to use");
			int answer = powerupChoiceController.askChoice();
			sendMessage(new IntMessage(answer, MessageType.POWERUP, MessageSubtype.ANSWER));
		});
	}

	@Override
	public void askOnDamagePowerupActivation(List<Integer> activablePowerups, String shootingPlayer) {
		Platform.runLater(() -> {
			Utils.logInfo("GUIView -> askOnDamagePowerupActivation(): activable powerups" + activablePowerups);
			powerupChoiceController.setPowerups(getModelRep().getClientPlayerRep().getPowerupCards());
			powerupChoiceController.activatePowerupsButtons(activablePowerups);
			powerupChoiceController.activateNoPowerupButton(true);
			powerupChoiceController.setTitle("You have just been damaged by " + shootingPlayer + "!");
			int answer = powerupChoiceController.askChoice();
			sendMessage(new IntMessage(answer, MessageType.POWERUP, MessageSubtype.ANSWER));
		});
	}

	@Override
	public void askPowerupChoice(QuestionContainer questionContainer) {
		askQuestionContainerAndSendAnswer(questionContainer, MessageType.POWERUP);
	}

	private void askQuestionContainerAndSendAnswer(QuestionContainer questionContainer, MessageType messageType) {
		Platform.runLater(() -> {
			if (questionContainer.isAskString()) {
				askStringController.setAskString(questionContainer, messageType);
				askStringStage.show();
			} else {
				gameBoardController.setQuestion(questionContainer.getQuestion());
				gameBoardController.highlightCoordinates(questionContainer.getCoordinates(), Request.CHOOSE, messageType);
			}
		});
	}

	@Override
	public void updateDisplay() {
		Platform.runLater(() -> {
			if (!gameBoardController.isInitialized())
				gameBoardController.init_GameMap(getModelRep(), this);
			else
				gameBoardController.updateGameBoard(getModelRep());
			window.setScene(gameBoardScene);
			window.show();
		});
	}

	@Override
	public void askAction(boolean activablePowerups, boolean activableWeapons) {
		gameBoardController.setAvailableActions(activablePowerups, activableWeapons, getModelRep().getClientPlayerRep().getDamageStatusRep());
	}

	@Override
	public void askGrabWeapon(List<Integer> indexesOfTheGrabbableWeapons) {
		gameBoardController.grabWeapon(indexesOfTheGrabbableWeapons, getModelRep().getGameMapRep().getPlayerCoordinates(getNickname()));
	}

	@Override
	public void askSwapWeapon(List<Integer> indexesOfTheGrabbableWeapons) {
		Platform.runLater(() -> {
			int indexOfTheWeaponToDiscard;
			int indexOfTheWeaponToGrab;
			List<WeaponRep> weaponsInSpawn = new ArrayList<>();
			for (CardRep card : getModelRep().getGameMapRep().getPlayerSquare(getNickname()).getCards()) {
				weaponsInSpawn.add((WeaponRep) card);
			}
			List<WeaponRep> weaponsOfThePlayer = getModelRep().getClientPlayerRep().getWeaponReps();
			List<Integer> discardableWeapons = new ArrayList<>();
			for (int i = 0; i < weaponsOfThePlayer.size(); i++) {
				discardableWeapons.add(i);
			}

			weaponChoiceController.setTitle("Select the weapon to grab");
			weaponChoiceController.setWeaponsToChoose(indexesOfTheGrabbableWeapons, weaponsInSpawn, Request.CHOOSE_INT);
			indexOfTheWeaponToGrab = weaponChoiceController.askWeapon();
			weaponChoiceController.setTitle("Select the weapon to discard");
			weaponChoiceController.setWeaponsToChoose(discardableWeapons, weaponsOfThePlayer, Request.CHOOSE_INT);
			indexOfTheWeaponToDiscard = weaponChoiceController.askWeapon();
			sendMessage(new SwapMessage(indexOfTheWeaponToGrab, indexOfTheWeaponToDiscard));
		});
	}

	@Override
	public void askMove(List<Coordinates> reachableCoordinates) {
		gameBoardController.highlightCoordinates(reachableCoordinates, Request.MOVE, MessageType.MOVE);
	}

	@Override
	public void askShoot(List<Integer> shootableWeapons) {
		Platform.runLater(() -> {
			weaponChoiceController.setWeaponsToChoose(shootableWeapons, getModelRep().getClientPlayerRep().getWeaponReps(), Request.CHOOSE);
			weaponChoiceController.setTitle("Choose the weapon to shoot with");
			weaponChoiceStage.show();
		});
	}

	@Override
	public void askReload(List<Integer> loadableWeapons) {
		Platform.runLater(() -> {
			weaponChoiceController.setWeaponsToChoose(loadableWeapons, getModelRep().getClientPlayerRep().getWeaponReps(), Request.RELOAD);
			weaponChoiceController.setTitle("Which weapon do you want to reload?");
			weaponChoiceStage.show();
		});
	}

	@Override
	public void askSpawn() {
		Platform.runLater(() -> {
			powerupChoiceController.setPowerups(getModelRep().getClientPlayerRep().getPowerupCards());
			List<Integer> activablePowerups = new ArrayList<>();
			List<PowerupCardRep> playerPowerups = new ArrayList<>(getModelRep().getClientPlayerRep().getPowerupCards());
			for (int i = 0; i < playerPowerups.size(); i++) {
				activablePowerups.add(i);
			}
			powerupChoiceController.activateNoPowerupButton(false);
			powerupChoiceController.activatePowerupsButtons(activablePowerups);
			powerupChoiceController.setTitle("Choose a powerup to discard in order to spawn");
			powerupChoiceStage.show();
		});
	}

	@Override
	public void askEnd(boolean activablePowerups) {
		gameBoardController.setEndTurnActions(activablePowerups);
	}

	@Override
	public void endOfGame(List<PlayerRepPosition> finalPlayersInfo) {
		Platform.runLater(() -> {
			endGameController.setValues(endGameStage, finalPlayersInfo);
			endGameStage.show();
		});
	}

	@Override
	public void askToPay(List<AmmoType> price, boolean canAffordAlsoWithAmmo) {
		Platform.runLater(() -> {
			List<AmmoType> priceToPay = new ArrayList<>(price);
			powerupChoiceController.setPowerups(getModelRep().getClientPlayerRep().getPowerupCards());
			powerupChoiceController.setTitle("Choose a powerup to discard to pay");
			List<Integer> usablePowerups = new ArrayList<>();
			List<Integer> answer = new ArrayList<>();
			List<PowerupCardRep> playerPowerups = getModelRep().getClientPlayerRep().getPowerupCards();
			List<PowerupCardRep> remainingPowerups = new ArrayList<>(playerPowerups);
			powerupChoiceController.setPowerups(playerPowerups);

			int choice = -2;
			boolean canAffordAlsoWithOnlyAmmo;

			Utils.logInfo("GUIView -> askToPay: price " + priceToPay + " with  " + remainingPowerups);
			for (int i = 0; i < remainingPowerups.size(); i++) {
				if (priceToPay.contains(remainingPowerups.get(i).getAssociatedAmmo())) {
					usablePowerups.add(i);
				}
			}
			Utils.logInfo("GUIView -> askToPay(): indexes of usable powerups " + usablePowerups);

			while (!usablePowerups.isEmpty() && choice != -1) {
				canAffordAlsoWithOnlyAmmo = canAffordAlsoWithOnlyAmmo(priceToPay);
				powerupChoiceController.activatePowerupsButtons(usablePowerups);
				powerupChoiceController.activateNoPowerupButton(canAffordAlsoWithOnlyAmmo);
				choice = powerupChoiceController.askChoice();
				if (choice != -1) {
					Utils.logInfo("GUIView -> askToPay(): player has chosen " + playerPowerups.get(choice) + " index " + choice);
					Utils.logInfo("GUIView -> askToPay(): removing from price " + playerPowerups.get(choice).getAssociatedAmmo());
					priceToPay.remove(playerPowerups.get(choice).getAssociatedAmmo());
					Utils.logInfo("GUIView -> askToPay(): adding to answer " + choice);
					answer.add(choice);
					remainingPowerups.remove(playerPowerups.get(choice));
					Utils.logInfo("GUIView -> askToPay: price " + priceToPay + " with " + remainingPowerups);
					usablePowerups = new ArrayList<>();
					for (int i = 0; i < playerPowerups.size(); i++) {
						if (remainingPowerups.contains(playerPowerups.get(i)) && priceToPay.contains(playerPowerups.get(i).getAssociatedAmmo())) {
							usablePowerups.add(i);
						}
					}
					Utils.logInfo("GUIView -> askToPay(): indexes of usable powerups " + usablePowerups);
				} else
					Utils.logInfo("GUIView -> askToPay(): player decided to use ammo");
			}
			sendMessage(new PaymentMessage(price, MessageSubtype.ANSWER).setPowerupsUsed(answer));
		});
	}

	private boolean canAffordAlsoWithOnlyAmmo(List<AmmoType> price) {
		List<AmmoType> playerAmmo = new ArrayList<>();
		List<AmmoType> priceToPay = new ArrayList<>(price);
		for (int i = 0; i < getModelRep().getClientPlayerRep().getAmmo().length; i++) {
			for (int j = 0; j < getModelRep().getClientPlayerRep().getAmmo()[i]; j++) {
				playerAmmo.add(AmmoType.values()[i]);
			}
		}
		Utils.logInfo("GUIView -> canAffordAlsoWithOnlyAmmo(): player ammo " + playerAmmo + " price to pay " + priceToPay);

		for (int i = priceToPay.size() - 1; i >= 0; i--) {
			if (playerAmmo.contains(priceToPay.get(i))) {
				playerAmmo.remove(priceToPay.get(i));
				priceToPay.remove(i);
			} else {
				Utils.logInfo("GUIView -> canAffordAlsoWithOnlyAmmo(): player ammo " + playerAmmo + " price to pay " + priceToPay + " => player cannot afford");
				return false;
			}
		}
		Utils.logInfo("GUIView -> canAffordAlsoWithOnlyAmmo(): player ammo " + playerAmmo + " remaining price to pay " + priceToPay + " => player can afford");

		return priceToPay.isEmpty();
	}
}