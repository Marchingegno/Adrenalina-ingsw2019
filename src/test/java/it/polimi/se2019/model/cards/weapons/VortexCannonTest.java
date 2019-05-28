package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class VortexCannonTest {
	private WeaponCard vortexCannon;
	private GameMap gameMap;
	private Model model;
	private List<Player> players;

	@Before
	public void setUp() throws Exception {
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

		model = new Model(GameConstants.MapType.MEDIUM_MAP.getMapName(), playerNicknames, 8);
	}

	@Test
	public void handlePrimaryFire() {
	}

	@Test
	public void primaryFire() {
	}

	@Test
	public void getPrimaryTargets() {
	}

	@Test
	public void handleOptionalEffect1() {
	}

	@Test
	public void reset() {
	}

	@Test
	public void canBeActivated() {
	}
}