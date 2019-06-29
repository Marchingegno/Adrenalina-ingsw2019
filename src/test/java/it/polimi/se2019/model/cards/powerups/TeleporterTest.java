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

import static org.junit.Assert.*;

public class TeleporterTest {

	private PowerupCard teleporter;
	private GameMap gameMap;
	private ModelDriver model;
	private Player ownerPlayer;

	@Before
	public void setUp() {
		teleporter = new Teleporter(AmmoType.RED_AMMO);

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, GameConstants.MIN_SKULLS);
		teleporter.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		ownerPlayer = model.getGameBoard().getPlayers().get(0);
		teleporter.setOwner(ownerPlayer);


		for(String playerName : playerNicknames) {
			model.spawnPlayer(playerName, 0);
		}
	}

	@Test
	public void canBeActivated_noInput_correctOutput() {
		Assert.assertTrue(teleporter.canBeActivated());
	}

	@Test
	public void allSteps_initialState_correctOutput() {
		// First step
		QuestionContainer questionContainer1 = teleporter.firstStep();
		Assert.assertTrue(questionContainer1.isAskCoordinates());
		Assert.assertEquals(gameMap.getAllCoordinatesExceptPlayer(ownerPlayer), questionContainer1.getCoordinates());
		Assert.assertFalse(teleporter.isActivationConcluded());
		Coordinates coordinatesChosen = questionContainer1.getCoordinates().get(0);

		// Do second step.
		QuestionContainer questionContainer2 = teleporter.secondStep(0);
		Assert.assertNull(questionContainer2);
		Assert.assertTrue(teleporter.isActivationConcluded());
		Assert.assertEquals(coordinatesChosen, gameMap.getPlayerCoordinates(ownerPlayer));
	}

	@Test
	public void thirdStep_wrongChoice_shouldConclude() {
		QuestionContainer questionContainer1 = teleporter.firstStep();

		QuestionContainer questionContainer2 = teleporter.secondStep(questionContainer1.getCoordinates().size());
		Assert.assertTrue(teleporter.isActivationConcluded());
	}

	@Test
	public void thirdStep_lessThan0Choice_shouldConclude() {
		QuestionContainer questionContainer1 = teleporter.firstStep();

		QuestionContainer questionContainer2 = teleporter.secondStep(-1);
		Assert.assertTrue(teleporter.isActivationConcluded());
	}

	@Test (expected = UnsupportedOperationException.class)
	public void thirdStep_noInput_shouldThrowException() {
		teleporter.thirdStep(0);
	}
}