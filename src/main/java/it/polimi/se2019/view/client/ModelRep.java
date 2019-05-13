package it.polimi.se2019.view.client;

import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.utils.exceptions.HiddenException;

import java.util.ArrayList;
import java.util.List;

public class ModelRep{

	private GameBoardRep gameBoardRep;
	private GameMapRep gameMapRep;
	private ArrayList<PlayerRep> playersRep = new ArrayList<>();

	public GameBoardRep getGameBoardRep() {
		return gameBoardRep;
	}

	public GameMapRep getGameMapRep() {
		return gameMapRep;
	}

	public List<PlayerRep> getPlayersRep() {
		return playersRep;
	}

	public PlayerRep getClientPlayerRep() {
		for (PlayerRep playerRep : playersRep) {
			if(!playerRep.isHidden())
				return playerRep;
		}

		throw new IllegalStateException("Player rep of Client not found.");
	}

	public void setGameBoardRep(GameBoardRep gameBoardRep) {
		this.gameBoardRep = gameBoardRep;
		Utils.logInfo("ModelRep -> setGameBoardRep(): Updated the GameBoardRep");
	}

	public void setGameMapRep(GameMapRep gameMapRep) {
		this.gameMapRep = gameMapRep;
		Utils.logInfo("ModelRep -> setGameMapRep(): Updated the GameMapRep");
	}

	public void setPlayersRep(PlayerRep playerRepToSet) {
		int numOfPlayersRep = playersRep.size();
		for (int i = 0; i < numOfPlayersRep; i++) {
			if (playersRep.get(i).getPlayerName().equals(playerRepToSet.getPlayerName())) {
				playersRep.set(i, playerRepToSet);
				Utils.logInfo("ModelRep -> setPlayersRep(): Updated the PlayersRep of " + playerRepToSet.getPlayerName());
				//To remove
				try {
					System.out.println("num of powerups" + playerRepToSet.getPowerupCards().size());
				} catch (HiddenException e) {

				}
				return;
			}
		}
		//There is no PlayerRep for this player so I add it
		Utils.logInfo("ModelRep -> setPlayersRep(): Received a new PlayersRep of " + playerRepToSet.getPlayerName());
		playersRep.add(playerRepToSet);
	}

	/**
	 * Returns true if this ModelRep has all the information needed to be displayed.
	 * @return true if this ModelRep has all the information needed to be displayed.
	 */
	public boolean isModelRepReadyToBeDisplayed() {
		return gameBoardRep != null && gameMapRep != null && playersRep.size() >= gameBoardRep.getNumberOfPlayers();
	}

	private ArrayList<PlayerRep> generatePlayersRep(ArrayList<Player> players){
		ArrayList<PlayerRep> tempPlayersRep = new ArrayList<>();
		for (Player player: players ) {
			tempPlayersRep.add(new PlayerRep(player));
		}
		return tempPlayersRep;
	}

	private ArrayList<PlayerRep> generatePlayersRep(ArrayList<Player> players, ModelRep oldModelRep){
		ArrayList<PlayerRep> tempPlayersRep = new ArrayList<>();
		for (int i = 0; i < players.size(); i++){
			if (players.get(i).hasChanged())
				tempPlayersRep.add(new PlayerRep(players.get(i)));
			else
				tempPlayersRep.add(oldModelRep.getPlayersRep().get(i));
		}
		return tempPlayersRep;
	}


}