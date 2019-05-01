package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.damagestatus.*;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.utils.GameConstants.HIGH_DAMAGE_THRESHOLD;
import static it.polimi.se2019.utils.GameConstants.MEDIUM_DAMAGE_THRESHOLD;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * This class is in a lower level than Controller. It handles the logic relative to the game.
 * @author Marchingegno
 */
public class GameController {

	private TurnController turnController;
	private Model model;
	private boolean frenzyStarted;


	public GameController(Model model) {
		this.model = model;
		turnController = new TurnController(model);
		frenzyStarted = FALSE;
		Utils.logInfo("Create GameController");
	}


	public void startGame() {
		Utils.logInfo("GameController: startGame");
		gameLogic();
	}

	public void initialRounds() {
	}

	public void startFinalRounds() {
		for (int i = 0; i < model.getPlayers().size(); i++) {
			startTurn(model.getCurrentPlayer());
			flipPlayers(model.getPlayers());
		}
	}


	@Deprecated
	private void gameLogic() {
		Utils.logInfo("Iteration of gameLogic");
		while(!model.areSkullsFinished()){
			startTurn(model.getCurrentPlayer());
			endTurn();
		}

		startFinalRounds();
	}

	private void flipPlayers(List<Player> playersToFlip){
		playersToFlip.stream().forEach(Player::flipIfNoDamage);

	}

	private void startFrenzy() {
		Player firstPlayer = model.getPlayers().get(0);
		ArrayList<Player> sortedPlayers = model.getPlayerQueue().getAsArray();
		int i = 0;

		while(sortedPlayers.get(i) != firstPlayer){
			sortedPlayers.get(i).setDamageStatus(new FrenzyBefore());
			i++;
		}

		while(i < sortedPlayers.size()){
			sortedPlayers.get(i).setDamageStatus(new FrenzyAfter());
			i++;
		}

		flipPlayers(sortedPlayers);
	}

	public void refillCardsOnMap() {
	}

	public void spawnPlayer(Player player, int indexOfPowerup) {
	}

	private void setCorrectDamageStatus(Player player){
		List<Player> damageBoard = player.getPlayerBoard().getDamageBoard();

		//If the game is in frenzy mode, then the player already has the right damageStatus.
		if (frenzyStarted)
			player.getDamageStatus().refillActions();

		else if(damageBoard.size() < MEDIUM_DAMAGE_THRESHOLD)
			player.setDamageStatus(new LowDamage());

		else if(damageBoard.size() < HIGH_DAMAGE_THRESHOLD)
			player.setDamageStatus(new MediumDamage());

		else
			player.setDamageStatus(new HighDamage());
	}

	private void startTurn(Player player) {
		setCorrectDamageStatus(player);
		turnController.handleTurn(player);
	}

	/**
	 * This method scores dead players, starts frenzy if it needs to be started
	 * and moves to the bottom the first player of playerQueue.
	 */
	private void endTurn() {
		model.scoreDeadPlayers();
		refillCardsOnMap();
		model.nextPlayerTurn();
		if(model.areSkullsFinished() && !frenzyStarted) {
			frenzyStarted = TRUE;
			startFrenzy();
		}
	}

}