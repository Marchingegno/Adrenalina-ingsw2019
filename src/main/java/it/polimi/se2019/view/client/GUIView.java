package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.client.Client;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.NicknameMessage;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class GUIView extends RemoteView {

	private ModelRep modelRep;
	private JFrame waitingRoomFrame;
	private JLabel waitingPlayersTextWaitingRoom;
	private JLabel timerTextWaitingRoom;

	public GUIView() {
		this.modelRep = new ModelRep();
		JComponent.setDefaultLocale(Locale.ENGLISH);
	}

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
	public void nicknameIsOk(String nickname) {
		// No need to show a dialog here.
		Utils.logInfo("Nickname set to: \"" + nickname + "\".");
	}

	@Override
	public void displayWaitingPlayers(String waitingPlayers) {
		if(waitingRoomFrame == null)
			showWaitingRoomFrame();

		waitingPlayersTextWaitingRoom.setText("Players in the waiting room: " + waitingPlayers + ".");
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

	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {

	}

	// TODO remove
	@Override
	public void askActionExample() {

	}

	@Override
	public void askAction() {

	}

	@Override
	public void askGrab() {

	}

	@Override
	public void askMove() {

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
	public void displayPossibleActions(List<MacroAction> possibleActions) {

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
		panel.setLayout(new GridLayout(3, 1));

		JLabel introductionText = new JLabel("Waiting for other players to connect.");
		introductionText.setVerticalTextPosition(JLabel.CENTER);
		introductionText.setHorizontalTextPosition(JLabel.CENTER);
		introductionText.setOpaque(true);
		panel.add(introductionText);

		waitingPlayersTextWaitingRoom = new JLabel("Players in the waiting room: .");
		waitingPlayersTextWaitingRoom.setVerticalTextPosition(JLabel.CENTER);
		waitingPlayersTextWaitingRoom.setHorizontalTextPosition(JLabel.CENTER);
		waitingPlayersTextWaitingRoom.setOpaque(true);
		panel.add(waitingPlayersTextWaitingRoom);

		timerTextWaitingRoom = new JLabel("");
		timerTextWaitingRoom.setVerticalTextPosition(JLabel.CENTER);
		timerTextWaitingRoom.setHorizontalTextPosition(JLabel.CENTER);
		timerTextWaitingRoom.setOpaque(true);
		panel.add(timerTextWaitingRoom);

		waitingRoomFrame.add(panel);
		waitingRoomFrame.pack(); // Sizes the frame so that all its contents are at or above their preferred sizes.
		waitingRoomFrame.setVisible(true); // Set the window to be visible.
	}
}