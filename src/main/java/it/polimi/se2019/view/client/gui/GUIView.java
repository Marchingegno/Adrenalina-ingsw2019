package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.GameConfigMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.utils.GameConstants;
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
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIView extends RemoteView {

	private GUIController guiController;

	private ConnectionController connectionController;
	private Scene connectionScene;

	private LoginController loginController;
	private Scene loginScene;

	private LobbyController lobbyController;
	private Scene lobbyScene;

	private MapAndSkullsController mapAndSkullsController;
	private Scene mapAndSklullsScene;

	private GameBoardController gameBoardController;
	private Scene gameBoardScene;

	private PowerupChoiceController powerupChoiceController;
	private Scene powerupChoiceScene;
	private Stage powerupChoiceStage;

	private Stage window;

	//TO REMOVE
	private JFrame waitingRoomFrame;
	private JLabel waitingPlayersTextWaitingRoom;
	private JLabel timerTextWaitingRoom;
	private JFrame gameConfigFrame;
	private JDialog gameConfigWaitingDialog;

	public GUIView(Stage window) {
		this.window = window;
		loadLoginController();
		loadLobbyController();
		loadMapsAndSkulls();
		loadGameBoard();
		loadPowerupChoice();
	}

	public void loadLoginController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginController.fxml"));
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

	public void loadLobbyController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LobbyController.fxml"));
		try {
			Parent root = loader.load();
			window.setTitle("Adrenaline");
			lobbyScene = new Scene(root);
			lobbyController = loader.getController();
		} catch (IOException e) {
			Utils.logError("Error loading lobbyController", e);
		}
	}

	public void loadMapsAndSkulls() {
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

	public void loadGameBoard() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GameBoardController.fxml"));
		try {
			Parent root = loader.load();
			gameBoardScene = new Scene(root);
			gameBoardController = loader.getController();
			window.setTitle("Adrenaline");
		} catch (IOException e) {
			Utils.logError("Error loading gameBoardController", e);
		}
	}

	public void loadPowerupChoice() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PowerupChoice.fxml"));
		try {
			Parent root = loader.load();
			powerupChoiceScene = new Scene(root);
			powerupChoiceStage = new Stage();
			powerupChoiceStage.setTitle("Adrenaline");
			powerupChoiceStage.setResizable(false);
			powerupChoiceStage.initModality(Modality.APPLICATION_MODAL);
			powerupChoiceStage.setOnCloseRequest(event -> {
				Platform.runLater(() -> {
					powerupChoiceStage.show();
				});
			});
			powerupChoiceStage.setScene(powerupChoiceScene);
			powerupChoiceController = loader.getController();
			powerupChoiceController.setGuiAndStage(this, powerupChoiceStage);
		} catch (IOException e) {
			Utils.logError("Error loading powerupChoice", e);
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

//		if(waitingRoomFrame == null)
//			showWaitingRoomFrame();
//
//		timerTextWaitingRoom.setText("Timer for starting the match cancelled.");
//		waitingRoomFrame.pack();
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
//		closeWaitingRoomFrame();
//		showGameConfigFrame();
	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {
		closeGameConfigFrame();
		closeGameConfigWaiting();

		String message = "Average of voted skulls: " + skulls + ".\n" +
				"Most voted map: " + mapType.getDescription() + "\n\n" +
				"Match ready to start!";
		JOptionPane.showMessageDialog(null, message, "Starting Match...", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void askWeaponChoice(QuestionContainer questionContainer) {

	}

	@Override
	public void askPowerupActivation(List<Integer> activablePowerups) {

	}

	@Override
	public void askPowerupChoice(QuestionContainer questionContainer) {

	}

	@Override
	public void updateDisplay() {
		Platform.runLater(() -> {
			if (!gameBoardController.isInitialized())
				gameBoardController.init_GameMap(getModelRep());
			else
				gameBoardController.updateGameBoard(getModelRep());
			window.setScene(gameBoardScene);
			window.show();
		});
	}

	@Override
	public void askAction(boolean activablePowerups, boolean activableWeapons) {

	}

	@Override
	public void askGrabWeapon(List<Integer> indexesOfTheGrabbableWeapons) {

	}

	@Override
	public void askSwapWeapon(List<Integer> indexesOfTheGrabbableWeapons) {

	}

	@Override
	public void askMove(List<Coordinates> reachableCoordinates) {

	}

	@Override
	public void askShoot(List<Integer> shootableWeapons) {

	}

	@Override
	public void askReload(List<Integer> loadableWeapons) {

	}

	@Override
	public void askSpawn() {
		Platform.runLater(() -> {
			powerupChoiceController.setPowerups(getModelRep().getClientPlayerRep().getPowerupCards());
			powerupChoiceController.setTitle("Choose powerup to discard in order to spawn");
			powerupChoiceStage.show();
		});
	}

	@Override
	public void askEnd(boolean activablePowerups) {

	}

	@Override
	public void askToPay(List<AmmoType> priceToPay){

	}


	// Not used, here just for example
	// TODO remove
	private void dialogWithCancel() {
		Object[] possibleValues = { "RMI", "Socket" };
		Object selectedValue = JOptionPane.showInputDialog(null,
				"Choose one",
				"Input",
				JOptionPane.INFORMATION_MESSAGE,
				null,
				possibleValues,
				possibleValues[0]);
		if(selectedValue == possibleValues[0])
			startConnectionWithRMI();
		else if(selectedValue == possibleValues[1])
			startConnectionWithSocket();
		else if(selectedValue == null)
			closeProgram();
	}

	private void showWaitingRoomFrame() {
		// Create Frame.
		JFrame.setDefaultLookAndFeelDecorated(true);
		waitingRoomFrame = new JFrame("Waiting Room");
		waitingRoomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set behaviour on close.

		// Create Panel.
		JPanel panel = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 1); // With 0 rows is like a vertical layout.
		gridLayout.setHgap(20);
		panel.setLayout(gridLayout);

		JLabel introductionText = new JLabel("Waiting for other players to connect.");
		introductionText.setOpaque(true);
		panel.add(introductionText);

		waitingPlayersTextWaitingRoom = new JLabel("Players in the waiting room: .");
		waitingPlayersTextWaitingRoom.setOpaque(true);
		panel.add(waitingPlayersTextWaitingRoom);

		timerTextWaitingRoom = new JLabel(" ");
		timerTextWaitingRoom.setOpaque(true);
		panel.add(timerTextWaitingRoom);

		waitingRoomFrame.add(panel);
		waitingRoomFrame.pack(); // Sizes the frame so that all its contents are at or above their preferred sizes.
		waitingRoomFrame.setVisible(true); // Set the window to be visible.
	}

	private void closeWaitingRoomFrame() {
		if(waitingRoomFrame != null) {
			waitingRoomFrame.setVisible(false);
			waitingRoomFrame.dispose();
		}
	}

	private void showGameConfigFrame() {
		// Create options.
		String[] skullsOptions = new String[GameConstants.MAX_SKULLS - GameConstants.MIN_SKULLS + 1];
		for (int i = GameConstants.MIN_SKULLS; i <= GameConstants.MAX_SKULLS; i++)
			skullsOptions[i - GameConstants.MIN_SKULLS] = ((Integer) i).toString();
		String[] mapOptions = new String[GameConstants.MapType.values().length];
		for (int i = 0; i < GameConstants.MapType.values().length; i++) {
			mapOptions[i] = GameConstants.MapType.values()[i].getDescription();
		}

		// Create frame.
		gameConfigFrame = new JFrame("Game Configurations");
		gameConfigFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Up panel.
		final JPanel upPanel = new JPanel();
		GridLayout upGridLayout = new GridLayout(1,1);
		upPanel.setLayout(upGridLayout);
		JLabel introductionText = new JLabel("Select the map and how many skulls you would like to use.");
		upPanel.add(introductionText);

		// Down panel.
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new GridLayout(2,3));
		downPanel.add(new JLabel("Skulls:"));
		downPanel.add(new JLabel("Map:"));
		downPanel.add(new JLabel(" "));
		JComboBox skullsComboBox = new JComboBox<>(skullsOptions);
		downPanel.add(skullsComboBox);
		JComboBox mapComboBox = new JComboBox<>(mapOptions);
		mapComboBox.setPreferredSize(skullsComboBox.getPreferredSize()); // Make the same dimension as the skulls options.
		downPanel.add(mapComboBox);
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(e -> {
			closeGameConfigFrame();
			showGameConfigWaiting();

			int skulls = skullsComboBox.getSelectedIndex() + GameConstants.MIN_SKULLS;
			int mapIndex = mapComboBox.getSelectedIndex();

			GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
			gameConfigMessage.setMapIndex(mapIndex);
			gameConfigMessage.setSkulls(skulls);
			sendMessage(gameConfigMessage);
		});
		downPanel.add(doneButton);

		// Add panels to container panel.
		gameConfigFrame.getContentPane().add(upPanel, BorderLayout.NORTH);
		gameConfigFrame.getContentPane().add(new JSeparator(), BorderLayout.CENTER);
		gameConfigFrame.getContentPane().add(downPanel, BorderLayout.SOUTH);

		// Display the window.
		gameConfigFrame.pack();
		gameConfigFrame.setVisible(true);
	}

	private void closeGameConfigFrame() {
		if(gameConfigFrame != null) {
			gameConfigFrame.setVisible(false);
			gameConfigFrame.dispose();
		}
	}

	private void showGameConfigWaiting() {
		gameConfigWaitingDialog = new JDialog();
		gameConfigWaitingDialog.setTitle("Waiting for other clients...");

		JLabel label = new JLabel("Waiting for other clients to answer...");

		gameConfigWaitingDialog.add(label);
		gameConfigWaitingDialog.pack();
		gameConfigWaitingDialog.setVisible(true);
	}

	private void closeGameConfigWaiting() {
		if(gameConfigWaitingDialog != null) {
			gameConfigWaitingDialog.setVisible(false);
			gameConfigWaitingDialog.dispose();
		}
	}
}