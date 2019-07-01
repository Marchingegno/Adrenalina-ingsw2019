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


public class VortexCannonTest {
	private static String name = "VortexCannon";
	private WeaponCard vortexCannon;
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
				vortexCannon = new VortexCannon(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		vortexCannon.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		vortexCannon.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(2, 1));
		gameMap.movePlayerTo(players.get(1), new Coordinates(1, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(1, 0));
		gameMap.movePlayerTo(players.get(3), new Coordinates(1, 2));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		//With optional effect.
		QuestionContainer initialQuestion = vortexCannon.doActivationStep(0);
		//Select optional effect 1.
		QuestionContainer vortexCoordinates = vortexCannon.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1."));
		//Select vortex coordinates (1,1).
//		assertEquals("(1,1)", vortexCoordinates.getCoordinates().get(0).toString());
		QuestionContainer targets = vortexCannon.doActivationStep(vortexCoordinates.getCoordinates().indexOf(new Coordinates(1, 1)));
		while (!vortexCannon.isActivationConcluded()) {
			targets.getOptions().forEach(Utils::logWeapon);
			targets = vortexCannon.doActivationStep(0);
		}

		assertTrue(vortexCannon.getPlayersHit().size() >= 3);
		for (int i = 1; i <= 3; i++) {
			assertEquals(gameMap.getPlayerCoordinates(players.get(i)), new Coordinates(1, 1));
		}

		vortexCannon.reset();
		assertTrue(vortexCannon.getCurrentTargets().isEmpty());
		assertFalse(vortexCannon.isLoaded());
	}


}