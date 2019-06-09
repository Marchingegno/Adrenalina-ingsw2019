package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class VortexCannonTest {
	private WeaponCard vortexCannon;
	private GameMap gameMap;
	private Model model;
	private List<Player> players;

	//@Before
	public void setUp() throws Exception {
		//The first player is the shooter.
		Reader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/decks/Weapon.json")));
		JsonArray weapons = new JsonParser().parse(reader).getAsJsonArray();
		for (JsonElement weapon : weapons) {
			if (weapon.getAsJsonObject().get("className").getAsString().equals(VortexCannon.class.getName())) {
				vortexCannon = new VortexCannon(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new Model(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);

		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		gameMap.movePlayerTo(players.get(0), new Coordinates(2, 1));
		gameMap.movePlayerTo(players.get(1), new Coordinates(1, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(1, 0));
		vortexCannon.setOwner(players.get(0));
	}

	//@Test
	public void primaryFireShooting_correctBehaviour() {
		//Without optional effects.
		QuestionContainer initialQuestion = vortexCannon.initialQuestion();
		//Select standard fire.
		QuestionContainer vortexCoordinates = vortexCannon.doActivationStep(0);
		//Select vortex coordinates (1,1).
		assertTrue(vortexCoordinates.getCoordinates().get(0).toString().equals("(1,1)"));
		vortexCannon.doActivationStep(0);
	}


}