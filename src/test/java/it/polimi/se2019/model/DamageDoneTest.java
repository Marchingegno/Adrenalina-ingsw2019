package it.polimi.se2019.model;

import it.polimi.se2019.model.player.Player;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class DamageDoneTest {

	@Test
	public void damageUp_newPlayer_correctOutput() {
		Player player1 = new Player("name1", 0, Color.blue);
		Player player2 = new Player("name2", 1, Color.red);
		DamageDone damageDoneTest = new DamageDone();

		damageDoneTest.damageUp(player1);
		damageDoneTest.damageUp(player1);
		damageDoneTest.damageUp(player2);

		ArrayList<Player> players = damageDoneTest.getPlayers();
		ArrayList<Integer> damages = damageDoneTest.getDamages();

		assertEquals(player1, players.get(0));
		assertEquals(player2, players.get(1));
		assertEquals(2, java.util.Optional.ofNullable(damages.get(0)));

	}



	@Test
	public void getSortedPlayers_correctArray_correctOutput() {
		Player player1 = new Player("name1", 0, Color.blue);
		Player player2 = new Player("name2", 1, Color.red);
		Player player3 = new Player("name3", 0, Color.yellow);
		Player player4 = new Player("name4", 1, Color.pink);
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