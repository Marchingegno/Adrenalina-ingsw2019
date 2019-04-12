package it.polimi.se2019.controller;

import it.polimi.se2019.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerQueueTest {

	private Player player1;
	private Player player2;
	private Player player3;
	private ArrayList<Player> players;

	@Before
	public void setUp() throws Exception {
		player1 = new Player("Test 1", 0, Color.GREEN);
		player2 = new Player("Test 2", 1, Color.BLUE);
		player3 = new Player("Test 3", 2, Color.RED);
		players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void moveFirstToLast_initialState_correctOutput() {
		PlayerQueue pq = new PlayerQueue(players);
		assertEquals(players.get(0), pq.peekFirst());
		assertEquals(players.get(players.size() - 1), pq.peekLast());
		pq.moveFirstToLast();
		assertEquals(players.get(1), pq.peekFirst());
		assertEquals(players.get(0), pq.peekLast());
	}
}