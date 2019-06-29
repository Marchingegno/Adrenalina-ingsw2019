package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.ModelDriver;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NewtonTest {

	private PowerupCard newton;
	private GameMap gameMap;
	private ModelDriver model;

	@Before
	public void setUp() {
		newton = new Newton(AmmoType.RED_AMMO);

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, GameConstants.MIN_SKULLS);
		newton.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		newton.setOwner(model.getGameBoard().getPlayers().get(0));


		for(String playerName : playerNicknames) {
			model.spawnPlayer(playerName, 0);
		}
	}

	@Test
	public void canBeActivated_initialState_correctOutput() {
		Assert.assertTrue(newton.canBeActivated());
	}

	@Test
	public void allSteps_initialState_correctOutput() {
		// Do first step.
		QuestionContainer questionContainer1 = newton.firstStep();
		Assert.assertTrue(questionContainer1.isAskString());
		Assert.assertEquals(4, questionContainer1.getOptions().size());
		Assert.assertFalse(newton.isActivationConcluded());
		Player playerChosen = model.getPlayerFromName(questionContainer1.getOptions().get(0));

		// Do second step.
		QuestionContainer questionContainer2 = newton.secondStep(0);
		Assert.assertTrue(questionContainer2.isAskCoordinates());
		Assert.assertFalse(newton.isActivationConcluded());
		Coordinates coordinatesChosen = questionContainer2.getCoordinates().get(0);

		// Do thrid step.
		QuestionContainer questionContainer3 = newton.thirdStep(0);
		Assert.assertNull(questionContainer3);
		Assert.assertEquals(coordinatesChosen, gameMap.getPlayerCoordinates(playerChosen));
		Assert.assertTrue(newton.isActivationConcluded());
	}

	@Test
	public void secondStep_wrongChoice_shouldConclude() {
		QuestionContainer questionContainer1 = newton.firstStep();

		QuestionContainer questionContainer2 = newton.secondStep(questionContainer1.getOptions().size());
		Assert.assertTrue(newton.isActivationConcluded());
	}

	@Test
	public void thirdStep_wrongChoice_shouldConclude() {
		QuestionContainer questionContainer1 = newton.firstStep();

		QuestionContainer questionContainer2 = newton.secondStep(0);

		QuestionContainer questionContainer3 = newton.thirdStep(questionContainer2.getCoordinates().size());
		Assert.assertTrue(newton.isActivationConcluded());
	}
}