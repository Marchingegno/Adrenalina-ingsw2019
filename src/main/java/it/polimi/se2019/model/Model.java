package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerBoard;
import it.polimi.se2019.view.ViewInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import static it.polimi.se2019.utils.GameConstants.FRENZY_SCORES;
import static it.polimi.se2019.utils.GameConstants.SCORES;

/**
 * @author Marchingegno
 * @author Desno365
 */
public class Model extends Observable {

	private ViewInterface view;
	private GameBoard gameBoard;


	public void initialize(String mapPath, List<String> playerNames, int startingSkulls) {
		gameBoard = new GameBoard(mapPath, playerNames, startingSkulls);
	}

	public void movePlayerTo(Player playerToMove) {
	}

	public void doDamageAndAddMarks(Player shootingPlayer, Player damagedPlayer, int amountOfDamage, int amountOfMarks) {
		doDamage(shootingPlayer, damagedPlayer, amountOfDamage);
		addMarks(shootingPlayer, damagedPlayer, amountOfMarks);
	}

	public void addMarks(Player shootingPlayer, Player damagedPlayer, int amountOfMarks) {
		damagedPlayer.getPlayerBoard().addMarks(shootingPlayer, amountOfMarks);
	}

	public void doDamage(Player shootingPlayer, Player damagedPlayer, int amountOfDamage) {
		PlayerBoard damagedPlayerBoard = damagedPlayer.getPlayerBoard();
		damagedPlayerBoard.addDamage(shootingPlayer, amountOfDamage);
		if(damagedPlayerBoard.isDead()) {
			gameBoard.addKillShot(shootingPlayer, damagedPlayerBoard.isOverkilled());
		}
	}

	public boolean areSkullsFinished() {
		return gameBoard.areSkullsFinished();
	}

	public void scoreDeadPlayers() {
		gameBoard.getPlayers().stream()
				.filter(item -> item.getPlayerBoard().isDead())
				.forEach(item -> scoreDeadPlayer(item));
	}

	private void scoreDeadPlayer(Player player) {
		PlayerBoard playerBoard = player.getPlayerBoard();
		DamageDone damageDone = new DamageDone();
		ArrayList<Player> sortedPlayers;

		for (Player p : playerBoard.getDamageBoard()) {
			damageDone.damageUp(p);
		}

		sortedPlayers = damageDone.getSortedPlayers();
		awardPoint(playerBoard, sortedPlayers);

		playerBoard.resetBoardAfterDeath(); //This automatically increases its number of deaths.
	}

    private synchronized void awardPoint(PlayerBoard deadPlayerBoard, ArrayList<Player> sortedPlayers){

		//TODO: Set new status for the player.
		int offset = 0;

		//This method relies on the "SCORES" array defined in GameConstants.
		if(deadPlayerBoard.isFrenzy()) {
			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(FRENZY_SCORES.get(deadPlayerBoard.getNumberOfDeaths() + offset));
				offset++;
			}
		}

		else{
			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(SCORES.get(deadPlayerBoard.getNumberOfDeaths() + offset));
				offset++;
				/*Se con questa morte si attiva la frenzy, o la frenzy è già attivata,
				 *si deve swappare la damageBoard e il damageStatus del player.
				 */
			}

			//ADD FIRST BLOOD POINT
			sortedPlayers.get(0).getPlayerBoard().addPoints(1);
		}
	}
	public void drawPowerupCard(Player player) {
	}

	public void drawWeaponCard(Player player, WeaponCard weapon) {
	}

	public ArrayList<Coordinates> getReachableCoordinates(Player player) {
		return null;
	}

	public void getPlayersCoordinates() {
	}

	public GameBoardRep getGameboardRep() {
		return null;
	}

	public void notifyMapChange() {
	}

	public void notifyPlayerChange() {
	}

	public void notifyGameboardChange() {
	}

}

class DamageDone{
	private ArrayList<Player> players;
	private ArrayList<Integer> damages;


	DamageDone() {
		this.players = new ArrayList<>();
		this.damages = new ArrayList<>();
	}

	void damageUp(Player player){

		int indexOfPlayer;
		int oldDamage;

		if (!players.contains(player)) {
			addPlayer(player);
		}

		indexOfPlayer = players.indexOf(player);
		oldDamage = damages.get(indexOfPlayer);
		damages.set(indexOfPlayer, (oldDamage + 1));
	}

	private void addPlayer(Player player){
		players.add(player);
		damages.add(0);
	}

	ArrayList<Player> getSortedPlayers()
	{
		sort();
		return (ArrayList<Player>) players.clone();
	}

	private void sort(){
		Player pToSwap;
		Integer iToSwap;

		while(!isSorted()) {
			for (int i = damages.size() - 1; i > 0 ; i--) {
				if (damages.get(i) > damages.get(i - 1)){
					//Swap in damages
					iToSwap = damages.get(i);
					damages.set(i, damages.get(i - 1));
					damages.set(i - 1, iToSwap);

					//Swap in players
					pToSwap = players.get(i);
					players.set(i, players.get(i - 1));
					players.set(i - 1, pToSwap);

				}
			}
		}
	}


	private boolean isSorted() {
		for (int i = 0; i < damages.size() - 1; i++) {
			if(damages.get(i) < damages.get(i+1)) {
				return false;
			}
		}

		return true;
	}
}