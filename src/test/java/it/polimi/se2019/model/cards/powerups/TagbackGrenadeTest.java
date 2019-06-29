package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.ModelDriver;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TagbackGrenadeTest {

	private PowerupCard tagback;
	private GameMap gameMap;
	private ModelDriver model;

	@Before
	public void setUp() {
		tagback = new TagbackGrenade(AmmoType.RED_AMMO);

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, GameConstants.MIN_SKULLS);
		tagback.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		tagback.setOwner(model.getGameBoard().getPlayers().get(0));


		for(String playerName : playerNicknames) {
			model.spawnPlayer(playerName, 0);
		}
	}

	@Test
	public void canBeActivated_noShootingPlayer_correctOutput() {
		tagback.setShootingPlayer(null);
		Assert.assertFalse(tagback.canBeActivated());
	}

	@Test
	public void allSteps_initialState_correctOutput() {
		// Set a player as shooting.
		Player shootingPlayer = model.getGameBoard().getPlayers().get(1);
		Player ownerPlayer = model.getGameBoard().getPlayers().get(0);
		gameMap.movePlayerTo(shootingPlayer, gameMap.getPlayerCoordinates(ownerPlayer));
		tagback.setShootingPlayer(shootingPlayer);
		Assert.assertTrue(tagback.canBeActivated());

		QuestionContainer questionContainer1 = tagback.firstStep();
		Assert.assertNull(questionContainer1);
		Assert.assertTrue(shootingPlayer.getPlayerBoard().getMarks().contains(ownerPlayer));
		Assert.assertTrue(tagback.isActivationConcluded());
	}

	@Test (expected = UnsupportedOperationException.class)
	public void secondStep_noInput_shouldThrowException() {
		tagback.secondStep(0);
	}

	@Test (expected = UnsupportedOperationException.class)
	public void thirdStep_noInput_shouldThrowException() {
		tagback.thirdStep(0);
	}
}