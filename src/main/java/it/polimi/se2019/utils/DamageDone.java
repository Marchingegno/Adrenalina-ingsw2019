package it.polimi.se2019.utils;

import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure that helps with counting players and damage done on damage boards.
 *
 * @author Marchingegno
 */
public class DamageDone {
	private ArrayList<Player> players;
	private ArrayList<Integer> damages;


	public DamageDone() {
		this.players = new ArrayList<>();
		this.damages = new ArrayList<>();
	}

	/**
	 * Only for testing purposes.
	 */
	public List<Integer> getDamages() {
		return new ArrayList<>(damages);
	}


	/**
	 * Only for testing purposes.
	 */
	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}

	/**
	 * Increments the damage of the player by one.
	 *
	 * @param player the player that did the damage.
	 */
	public void damageUp(Player player) {

		int indexOfPlayer;
		int oldDamage;

		if (!players.contains(player)) {
			addPlayer(player);
		}

		indexOfPlayer = players.indexOf(player);
		oldDamage = damages.get(indexOfPlayer);
		damages.set(indexOfPlayer, (oldDamage + 1));
	}

	/**
	 * Sort all players and return the list.
	 * @return the sorted list.
	 */
	public List<Player> getSortedPlayers() {
		sort();
		return new ArrayList<>(players);
	}


	private void addPlayer(Player player) {
		players.add(player);
		damages.add(0);
	}


	private void sort() {
		//Lazy bubble sort implementation
		if (damages.size() <= 1) {
			return;
		}
		Player pToSwap;
		Integer iToSwap;

		while (!isSorted()) {
			for (int i = damages.size() - 1; i > 0; i--) {
				if (damages.get(i) > damages.get(i - 1)) {
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
			if (damages.get(i) < damages.get(i + 1)) {
				return false;
			}
		}
		return true;
	}
}
