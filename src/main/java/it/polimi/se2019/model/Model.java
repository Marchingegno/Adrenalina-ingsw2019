package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerBoard;
import it.polimi.se2019.model.player.PlayerQueue;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.ViewInterface;

import java.util.*;

import static it.polimi.se2019.utils.GameConstants.*;

/**
 * Facade of the game board.
 * TODO add updateReps to all methods that change the model.
 * @author Marchingegno
 * @author Desno365
 * @author MarcerAndrea
 */
public class Model{

	private GameBoard gameBoard;
	private GameMap gameMap;

	public Model(String mapPath, List<String> playerNames, int startingSkulls) {
		gameBoard = new GameBoard(mapPath, playerNames, startingSkulls);
		gameMap = gameBoard.getGameMap();
	}

	public void movePlayerTo(Player playerToMove, Coordinates coordinates) {
		gameMap.movePlayerTo(playerToMove, coordinates);
		updateReps();
	}

	public PlayerQueue getPlayerQueue()
	{
		return gameBoard.getPlayerQueue();
	}

	public Player getCurrentPlayer(){
		return gameBoard.getCurrentPlayer();
	}

	public GameBoard getGameBoard(){return gameBoard;}

	public void nextPlayerTurn(){
		gameBoard.nextPlayerTurn();
		updateReps();
	}


	public void doDamageAndAddMarks(Player shootingPlayer, Player damagedPlayer, int amountOfDamage, int amountOfMarks) {
		doDamage(shootingPlayer, damagedPlayer, amountOfDamage);
		addMarks(shootingPlayer, damagedPlayer, amountOfMarks);
	}

	public void addMarks(Player shootingPlayer, Player damagedPlayer, int amountOfMarks) {
		damagedPlayer.getPlayerBoard().addMarks(shootingPlayer, amountOfMarks);
		updateReps();
	}

	public void doDamage(Player shootingPlayer, Player damagedPlayer, int amountOfDamage) {
		PlayerBoard damagedPlayerBoard = damagedPlayer.getPlayerBoard();
		damagedPlayerBoard.addDamage(shootingPlayer, amountOfDamage);
		if(damagedPlayerBoard.isDead()) {
			gameBoard.addKillShot(shootingPlayer, damagedPlayerBoard.isOverkilled());
		}
		updateReps();
	}

	public boolean areSkullsFinished() {
		return gameBoard.areSkullsFinished();
	}

	public void scoreDeadPlayers() {
		gameBoard.getPlayers().stream()
				.filter(item -> item.getPlayerBoard().isDead())
				.forEach(this::scoreDeadPlayer);
		updateReps();
	}

	private void scoreDeadPlayer(Player player) {
		PlayerBoard playerBoard = player.getPlayerBoard();
		DamageDone damageDone = new DamageDone();
		ArrayList<Player> sortedPlayers;
		Player killingPlayer;
		boolean overkill = false;

		//This will check the damageBoard of the player and award killShot points.
		killingPlayer = playerBoard.getDamageBoard().get(DEATH_DAMAGE - 1);
		if(playerBoard.getDamageBoard().lastIndexOf(killingPlayer) == OVERKILL_DAMAGE - 1){
			overkill = true;
		}
		gameBoard.addKillShot(killingPlayer, overkill);

		//This foreach cannot be replaced with a lambda expression because it's not synchronized.
		for (Player p : playerBoard.getDamageBoard()) {
			damageDone.damageUp(p);
		}

		sortedPlayers = damageDone.getSortedPlayers();
		awardPoints(playerBoard, sortedPlayers);

		player.resetAfterDeath(); //This automatically increases its number of deaths.
	}

    private synchronized void awardPoints(PlayerBoard deadPlayerBoard, ArrayList<Player> sortedPlayers){
		int offset = 0;

		//TODO: The implementation is WRONG. A player should give FRENZY_SCORES only if the playerBoard is flipped. @Marchingegno

		//This method relies on the "SCORES" array defined in GameConstants.
		if(deadPlayerBoard.isFlipped()) {
			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(FRENZY_SCORES.get(offset));
				offset++;
			}
		}

		else{
			//AWARD FIRST BLOOD POINT
			sortedPlayers.get(0).getPlayerBoard().addPoints(1);

			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(SCORES.get(deadPlayerBoard.getNumberOfDeaths() + offset));
				offset++;
				/*Se con questa morte si attiva la frenzy, o la frenzy è già attivata,
				 *si deve swappare la damageBoard e il damageStatus del player.
				 *
				 * Fatto dal gameController!
				 */
			}

		}
	}
	public void drawPowerupCard(Player player) {
		updateReps();
	}

	public void drawWeaponCard(Player player, WeaponCard weapon) {
		updateReps();
	}

	public ArrayList<Coordinates> getReachableCoordinates(Player player, int distance) {
		return gameMap.reachableCoordinates(player, distance);
	}

	public Map<Player, Coordinates> getPlayersCoordinates() { return gameMap.getPlayersCoordinates();	}

	public List<Player> getPlayers(){return gameBoard.getPlayers();}

	public void updateReps(){
		gameBoard.notifyObservers();
		Utils.logInfo("Notified all Game Board Observers");
		gameMap.notifyObservers();
		Utils.logInfo("Notified all Game Map Observers");
		for (Player player: gameBoard.getPlayers() ) {
			player.notifyObservers();
			Utils.logInfo("Notified all Player " + player.getPlayerName() +"Observers");
		}
	}
}

/**
 * Data structure that helps with counting players and damage done on damage boards.
 * @author Marchingegno
 */
class DamageDone{
	private ArrayList<Player> players;
	private ArrayList<Integer> damages;


	DamageDone() {
		this.players = new ArrayList<>();
		this.damages = new ArrayList<>();
	}

	/**
	 * The following two method are for testing purposes only.
	 */
	public ArrayList<Integer> getDamages() {
		return new ArrayList<>(damages);
	}


	public ArrayList<Player> getPlayers() {
		return new ArrayList<>(players);
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
		return new ArrayList<Player>(players);
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