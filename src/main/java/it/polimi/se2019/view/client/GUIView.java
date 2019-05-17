package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.GameConfigMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.NicknameMessage;
import it.polimi.se2019.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class GUIView extends RemoteView {

	private JFrame waitingRoomFrame;
	private JLabel waitingPlayersTextWaitingRoom;
	private JLabel timerTextWaitingRoom;
	private JFrame gameConfigFrame;
	private JDialog gameConfigWaitingDialog;


	@Override
	public void askForConnectionAndStartIt() {
		Object[] options = {"RMI", "Socket"};
		int answer = JOptionPane.showOptionDialog(null,
				"Which connection would you like to use?",
				"Connection",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		switch(answer) {
			case 0:
				startConnectionWithRMI();
				break;
			case 1:
				startConnectionWithSocket();
				break;
			case JOptionPane.CLOSED_OPTION:
				closeProgram();
				break;
			default:
				startConnectionWithRMI();
				break;
		}
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
		String input = JOptionPane.showInputDialog(null,
				"Enter your nickname.",
				"Nickname",
				JOptionPane.QUESTION_MESSAGE);
		if(input == null)
			closeProgram();
		else
			sendMessage(new NicknameMessage(input, MessageSubtype.ANSWER));
	}

	@Override
	public void askNicknameError() {
		JOptionPane.showMessageDialog(null, "The nickname already exists or is not valid, please use a different one.", "Nickname Error", JOptionPane.WARNING_MESSAGE);
		askNickname();
	}

	@Override
	public void displayWaitingPlayers(List<String> waitingPlayers) {
		if(waitingRoomFrame == null)
			showWaitingRoomFrame();

		waitingPlayersTextWaitingRoom.setText("Players in the waiting room: " + waitingPlayers.toString() + ".");
		waitingRoomFrame.pack();
	}

	@Override
	public void displayTimerStarted(long delayInMs) {
		if(waitingRoomFrame == null)
			showWaitingRoomFrame();

		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(0);
		timerTextWaitingRoom.setText("The match will start in " + decimalFormat.format(delayInMs / 1000d) + " seconds...");
		waitingRoomFrame.pack();
	}

	@Override
	public void displayTimerStopped() {
		if(waitingRoomFrame == null)
			showWaitingRoomFrame();

		timerTextWaitingRoom.setText("Timer for starting the match cancelled.");
		waitingRoomFrame.pack();
	}

	@Override
	public void askMapAndSkullsToUse() {
		closeWaitingRoomFrame();
		showGameConfigFrame();
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
	public void updateDisplay() {

	}

	@Override
	public void askAction() {

	}

	@Override
	public void askGrab() {

	}

	@Override
	public void askMove(List<Coordinates> reachableCoordinates) {

	}

	@Override
	public void askShoot() {

	}

	@Override
	public void askReload() {

	}

	@Override
	public void askSpawn() {

	}

	@Override
	public void askEnd() {

	}

	@Override
	public void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate) {

	}

	@Override
	public void updateGameMapRep(GameMapRep gameMapRepToUpdate) {

	}

	@Override
	public void updatePlayerRep(PlayerRep playerRepToUpdate) {

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