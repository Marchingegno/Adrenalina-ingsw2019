package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.network.client.MessageReceiverInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.view.ViewInterface;

import java.util.Observable;
import java.util.Observer;

/**
 * This class is in a lower level than GameController. It handles the logic relative
 * This class is a message handler.
 * @author Marchingegno
 */
public class TurnController implements Observer, MessageReceiverInterface {

	private Player currentPlayer;
	private Model model;
	private ViewInterface view;


	public TurnController(Model model) {
		this.model = model;
	}

	/**
	 * Beginning of the turn.
	 * @param player the current player.
	 */
	public void handleTurn(Player player) {
		currentPlayer = player;
		player.setTurnStatus(TurnStatus.YOUR_TURN);

	}

	public void displayPossibleMoves(){

	}

	public void performPowerup(int indexOfPowerup) {
	}


	public void performMacroaction(int indexOfMacroAction) {
	}

	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {
		processMessage( (Message) arg);
	}

	@Override
	public void processMessage(Message message) {

	}

	@Override
	public void lostConnection() {

	}
}