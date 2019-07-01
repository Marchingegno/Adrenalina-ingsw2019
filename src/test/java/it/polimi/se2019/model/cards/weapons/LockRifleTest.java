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

public class LockRifleTest {

	private static String name = "LockRifle";
	private WeaponCard lockRifle;
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
				lockRifle = new LockRifle(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		lockRifle.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		lockRifle.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(1, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(2), new Coordinates(1, 2));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = lockRifle.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select optional effect 1.
		QuestionContainer nextQC;
		nextQC = lockRifle.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1."));
		//Select first target.
		nextQC = lockRifle.doActivationStep(0);
		//Select second target.
		nextQC = lockRifle.doActivationStep(0);

		assertTrue(lockRifle.isActivationConcluded());
		//playersHit must have only one player, because the second player will be dealt only a mark.
		assertEquals(1, lockRifle.getPlayersHit().size());


		lockRifle.reset();
		assertFalse(lockRifle.isLoaded());
		assertTrue(lockRifle.getCurrentTargets().isEmpty());

	}
}