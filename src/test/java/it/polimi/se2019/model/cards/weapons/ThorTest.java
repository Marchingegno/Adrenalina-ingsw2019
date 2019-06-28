package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.se2019.model.ModelDriver;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ThorTest {

	private static String name = "Thor";
	private WeaponCard thor;
	private GameMap gameMap;
	private ModelDriver model;
	private List<Player> players;

	@Before
	public void setUp() {
		//The first player is the shooter.
		Reader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/decks/WeaponDeckWhole.json")));
		JsonArray weapons = new JsonParser().parse(reader).getAsJsonObject().getAsJsonArray("weapons");
		for (JsonElement weapon : weapons) {
			if (weapon.getAsJsonObject().get("className").getAsString().equals(name)) {
				thor = new Thor(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		thor.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		thor.setOwner(players.get(0));

		players.get(0).getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.BLUE_AMMO);


		gameMap.movePlayerTo(players.get(0), new Coordinates(2, 3));
		gameMap.movePlayerTo(players.get(1), new Coordinates(2, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(1, 0));
		gameMap.movePlayerTo(players.get(3), new Coordinates(0, 2));
	}

	@Test
	public void chainsOfThreePlayersShooting_correctBehaviour() {
		QuestionContainer initialQuestion = thor.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select optional effect 1 + 2.
		QuestionContainer nextQC;
		nextQC = thor.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1 + Optional effect 2."));
		//Choose chain.
		thor.doActivationStep(0);

		assertTrue(thor.isActivationConcluded());
		assertEquals(3, thor.getPlayersHit().size());

		thor.reset();
		assertFalse(thor.isLoaded());
		assertTrue(thor.currentTargets.isEmpty());

	}

	@Test
	public void chainsOfTwoPlayerShooting_correctBehaviour() {
		QuestionContainer initialQuestion = thor.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select optional effect 1.
		QuestionContainer nextQC;
		nextQC = thor.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1."));
		//Choose chain.
		thor.doActivationStep(0);

		assertTrue(thor.isActivationConcluded());
		assertEquals(2, thor.getPlayersHit().size());

		thor.reset();
		assertFalse(thor.isLoaded());
		assertTrue(thor.currentTargets.isEmpty());
	}

	@Test
	public void onePlayerShooting_correctBehaviour() {
		QuestionContainer initialQuestion = thor.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select no optional effect.
		QuestionContainer nextQC;
		nextQC = thor.doActivationStep(initialQuestion.getOptions().indexOf("No optional effects."));
		//Choose player.
		thor.doActivationStep(0);

		assertTrue(thor.isActivationConcluded());
		assertEquals(1, thor.getPlayersHit().size());

		thor.reset();
		assertFalse(thor.isLoaded());
		assertTrue(thor.currentTargets.isEmpty());
	}


}