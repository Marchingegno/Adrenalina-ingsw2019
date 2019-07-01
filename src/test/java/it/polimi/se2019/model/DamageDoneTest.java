package it.polimi.se2019.model;

import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.DamageDone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DamageDoneTest {

	@Test
	public void damageUp_newPlayer_correctOutput() {
		Player player1 = new Player("name1", 0);
		Player player2 = new Player("name2", 1);
		DamageDone damageDoneTest = new DamageDone();

		damageDoneTest.damageUp(player1);
		damageDoneTest.damageUp(player1);
		damageDoneTest.damageUp(player2);

		List<Player> players = damageDoneTest.getPlayers();
		List<Integer> damages = damageDoneTest.getDamages();

		assertEquals(player1, players.get(0));
		assertEquals(player2, players.get(1));
		assertEquals(2, (int) damages.get(0));
	}

	@Test
	public void getSortedPlayers_correctArray_correctOutput() {
		Player player1 = new Player("name1", 0);
		Player player2 = new Player("name2", 1);
		Player player3 = new Player("name3", 2);
		Player player4 = new Player("name4", 3);
		DamageDone damageDoneTest = new DamageDone();

		damageDoneTest.damageUp(player1);
		damageDoneTest.damageUp(player1);
		damageDoneTest.damageUp(player2);
		damageDoneTest.damageUp(player4);
		damageDoneTest.damageUp(player4);
		damageDoneTest.damageUp(player4);
		damageDoneTest.damageUp(player4);
		damageDoneTest.damageUp(player4);
		damageDoneTest.damageUp(player4);
		damageDoneTest.damageUp(player4);
		damageDoneTest.damageUp(player3);

		ArrayList<Player> expectedArray = new ArrayList<>();
		expectedArray.add(player4);
		expectedArray.add(player1);
		expectedArray.add(player2);
		expectedArray.add(player3);
		ArrayList<Player> sortedArray = damageDoneTest.getSortedPlayers();
		assertArrayEquals(new ArrayList[]{expectedArray}, new ArrayList[]{sortedArray});
	}

}