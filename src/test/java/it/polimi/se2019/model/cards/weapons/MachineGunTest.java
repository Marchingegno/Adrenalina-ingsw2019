package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.se2019.model.ModelDriver;
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

public class MachineGunTest {
	private static String name = "MachineGun";
	private WeaponCard machinegun;
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
				machinegun = new MachineGun(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		machinegun.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		machinegun.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(3), new Coordinates(0, 2));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = machinegun.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = machinegun.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1 + Optional effect 2."));
		//Select a target.
		assertEquals(3, nextQC.getOptions().size());
		nextQC = machinegun.doActivationStep(0);

		//Select a target.
		nextQC = machinegun.doActivationStep(0);

		//Select a target
		nextQC = machinegun.doActivationStep(0);

		assertTrue(machinegun.isActivationConcluded());
		assertEquals(3, machinegun.getPlayersHit().size());

		machinegun.reset();
		assertFalse(machinegun.isLoaded());
		assertTrue(machinegun.getCurrentTargets().isEmpty());

	}

}