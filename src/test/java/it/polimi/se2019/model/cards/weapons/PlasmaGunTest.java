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

public class PlasmaGunTest {

	private static String name = "PlasmaGun";
	private WeaponCard plasmaGun;
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
				plasmaGun = new PlasmaGun(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		plasmaGun.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		plasmaGun.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(1, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(2), new Coordinates(2, 3));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = plasmaGun.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select optional effect 1 + 2.
		QuestionContainer nextQC;
		nextQC = plasmaGun.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1 + Optional effect 2."));
		//Select move
		nextQC = plasmaGun.doActivationStep(1);
		//Move to (1,2)
		nextQC = plasmaGun.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(1, 2)));
		//Select base
		nextQC = plasmaGun.doActivationStep(0);
		//Choose target
		nextQC = plasmaGun.doActivationStep(0);

		assertTrue(plasmaGun.isActivationConcluded());
		assertEquals(1, plasmaGun.getPlayersHit().size());

		plasmaGun.reset();
		assertFalse(plasmaGun.isLoaded());
		assertTrue(plasmaGun.currentTargets.isEmpty());
	}
}