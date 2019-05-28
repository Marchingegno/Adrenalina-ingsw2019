package it.polimi.se2019.model.player;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Desno365
 */
public class PlayerQueueTest {

	private static final Player player1 = new Player("Test 1", 0);
	private static final Player player2 = new Player("Test 2", 1);
	private static final Player player3 = new Player("Test 3", 2);
	private static final ArrayList<Player> players = new ArrayList<>();

	private PlayerQueue playerQueue;

	@BeforeClass
	public static void oneTimeSetUp() {
		players.add(player1);
		players.add(player2);
		players.add(player3);
	}

	@Before
	public void setUp() throws Exception {
		playerQueue = new PlayerQueue(players);
	}

	@Test
	public void moveFirstToLast_initialState_correctOutput() {
		assertEquals(players.get(0), playerQueue.peekFirst());
		assertEquals(players.get(players.size() - 1), playerQueue.peekLast());
		playerQueue.moveFirstToLast();
		assertEquals(players.get(1), playerQueue.peekFirst());
		assertEquals(players.get(0), playerQueue.peekLast());
	}
}