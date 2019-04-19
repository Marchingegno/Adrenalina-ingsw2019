package it.polimi.se2019.view;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.network.server.ConnectionToClientInterface;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class VirtualView implements ViewInterface, Observer {

	private ConnectionToClientInterface client;
	private Controller controller;

	public VirtualView(Controller controller, ConnectionToClientInterface client) {
		this.client = client;
		this.controller = controller;

		this.controller.getModel().getGameBoard().addObserver(this);
		System.out.println("Added Game Board Observer");
		this.controller.getModel().getGameBoard().getGameMap().addObserver(this);
		System.out.println("Added Game Map Observer");
		for (Player player : this.controller.getModel().getPlayers()) {
			player.addObserver(this);
			System.out.println("Added Player Observer");
		}
	}

	public void displayPossibleActions(List<MacroAction> possibleActions) {
		for (MacroAction macroAction : possibleActions) {
			Utils.logInfo(macroAction.toString());
		}
	}

	@Override
	public void handleMove(int row, int column) {
	}

	@Override
	public void handleReload(int indexOfweaponToReload) {
	}

	@Override
	public void handleShoot(int indexOfWeapon, String playersToShoot) {
	}

	@Override
	public void handleSpawn(int indexOfPowerup) {
	}

	@Override
	public void showMessage(String message) {
	}

	@Override
	public void showGameBoard() {
	}

	@Override
	public int chooseWeapon() {
		return 0;
	}

	@Override
	public int chooseAction() {
		return 0;
	}

	@Override
	public int choosePowerup() {
		return 0;
	}

	//TODO HORRIBLE, find another solution. Sorry For the Kittens :(
	@Override
	public void update(Observable observable, Object arg) {
		if (observable instanceof GameMap) {
			System.out.println("Map Rep Created");
			client.sendMessage(new GameMapRep((GameMap) observable));
		}

		if (observable instanceof GameBoard) {
			System.out.println("Game Map Rep Created");
			client.sendMessage(new GameBoardRep((GameBoard) observable));
		}

		if (observable instanceof Player) {
			System.out.println("Player Rep Created");
			client.sendMessage(new PlayerRep((Player) observable));
		}
	}
}