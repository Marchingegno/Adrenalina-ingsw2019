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

public class TargetingScopeTest {

	private PowerupCard targetingScope;
	private GameMap gameMap;
	private ModelDriver model;
	private Player ownerPlayer;

	@Before
	public void setUp() {
		targetingScope = new TargetingScope(AmmoType.RED_AMMO);

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, GameConstants.MIN_SKULLS);
		targetingScope.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		ownerPlayer = model.getGameBoard().getPlayers().get(0);
		targetingScope.setOwner(ownerPlayer);


		for(String playerName : playerNicknames) {
			model.spawnPlayer(playerName, 0);
		}
	}

	@Test
	public void canBeActivated_noShootedPlayers_correctOutput() {
		targetingScope.setShootedPlayers(new ArrayList<>());
		Assert.assertFalse(targetingScope.canBeActivated());
	}

	@Test
	public void canBeActivated_noAmmos_correctOutput() {
		for(AmmoType ammoType : AmmoType.values()) {
			ownerPlayer.getPlayerBoard().getAmmoContainer().removeAmmo(ammoType, ownerPlayer.getPlayerBoard().getAmmoContainer().getAmmo(ammoType));
		}
		List<Player> shotPlayers = new ArrayList<>();
		shotPlayers.add(model.getGameBoard().getPlayers().get(1));
		targetingScope.setShootedPlayers(shotPlayers);
		Assert.assertFalse(targetingScope.canBeActivated());
	}

	@Test
	public void allSteps_initialState_correctOutput() {
		// Prepare players.
		ownerPlayer.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO); // Has at least one ammo.
		List<Player> shotPlayers = new ArrayList<>();
		shotPlayers.add(model.getGameBoard().getPlayers().get(1));
		shotPlayers.add(model.getGameBoard().getPlayers().get(2));
		targetingScope.setShootedPlayers(shotPlayers); // Has 2 shot players.
		Assert.assertTrue(targetingScope.canBeActivated());

		// First step
		QuestionContainer questionContainer1 = targetingScope.firstStep();
		Assert.assertTrue(questionContainer1.isAskString());
		Assert.assertEquals(2, questionContainer1.getOptions().size());
		Assert.assertFalse(targetingScope.isActivationConcluded());
		Player playerChosen = model.getPlayerFromName(questionContainer1.getOptions().get(0));

		// Do second step.
		QuestionContainer questionContainer2 = targetingScope.secondStep(0);
		Assert.assertTrue(questionContainer2.isAskString());
		Assert.assertFalse(targetingScope.isActivationConcluded());

		// Do third step.
		QuestionContainer questionContainer3 = targetingScope.thirdStep(0);
		Assert.assertNull(questionContainer3);
		Assert.assertTrue(playerChosen.getPlayerBoard().getDamageBoard().contains(ownerPlayer));
	}

	@Test
	public void thirdStep_wrongChoice_shouldConclude() {
		// Prepare players.
		ownerPlayer.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO); // Has at least one ammo.
		List<Player> shotPlayers = new ArrayList<>();
		shotPlayers.add(model.getGameBoard().getPlayers().get(1));
		shotPlayers.add(model.getGameBoard().getPlayers().get(2));
		targetingScope.setShootedPlayers(shotPlayers); // Has 2 shot players.
		Assert.assertTrue(targetingScope.canBeActivated());

		QuestionContainer questionContainer1 = targetingScope.firstStep();

		QuestionContainer questionContainer2 = targetingScope.secondStep(0);

		QuestionContainer questionContainer3 = targetingScope.thirdStep(questionContainer2.getOptions().size());
		Assert.assertTrue(targetingScope.isActivationConcluded());
	}

	@Test
	public void thirdStep_lessThan0Choice_shouldConclude() {
		// Prepare players.
		ownerPlayer.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO); // Has at least one ammo.
		List<Player> shotPlayers = new ArrayList<>();
		shotPlayers.add(model.getGameBoard().getPlayers().get(1));
		shotPlayers.add(model.getGameBoard().getPlayers().get(2));
		targetingScope.setShootedPlayers(shotPlayers); // Has 2 shot players.
		Assert.assertTrue(targetingScope.canBeActivated());

		QuestionContainer questionContainer1 = targetingScope.firstStep();

		QuestionContainer questionContainer2 = targetingScope.secondStep(0);

		QuestionContainer questionContainer3 = targetingScope.thirdStep(-1);
		Assert.assertTrue(targetingScope.isActivationConcluded());
	}
}