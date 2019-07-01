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
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CyberbladeTest {

	private static String name = "Cyberblade";
	private WeaponCard cyberblade;
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
				cyberblade = new Cyberblade(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		cyberblade.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		cyberblade.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(2), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(3), new Coordinates(0, 1));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = cyberblade.doActivationStep(0);
		//Select optional effect 1 + 2.
		QuestionContainer nextQC;
		nextQC = cyberblade.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1 + Optional effect 2."));
		//Select base
		nextQC = cyberblade.doActivationStep(0);
		//Select target
		nextQC = cyberblade.doActivationStep(0);
		//Select move
		nextQC = cyberblade.doActivationStep(0);
		//Move to (0,1)
		assertEquals(1, nextQC.getCoordinates().size());
		nextQC = cyberblade.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(0, 1)));
		//Select extra
		nextQC = cyberblade.doActivationStep(0);
		//Select target
		nextQC = cyberblade.doActivationStep(0);

		assertTrue(cyberblade.isActivationConcluded());
		assertEquals(2, cyberblade.getPlayersHit().size());

		cyberblade.reset();
		assertFalse(cyberblade.isLoaded());
		assertTrue(cyberblade.getCurrentTargets().isEmpty());
	}
}