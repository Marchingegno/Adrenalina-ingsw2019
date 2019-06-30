package it.polimi.se2019.model.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.se2019.model.ModelDriver;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.powerups.Teleporter;
import it.polimi.se2019.model.cards.weapons.Shotgun;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.damagestatus.DamageStatus;
import it.polimi.se2019.model.player.damagestatus.HighDamage;
import it.polimi.se2019.utils.GameConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {

	private static WeaponCard weapon1;

	private Player player1;
	private Player player2;

	@BeforeClass
	public static void oneTimeSetUp() {
		Reader reader = new BufferedReader(new InputStreamReader(PlayerRepTest.class.getResourceAsStream("/decks/WeaponDeckWhole.json")));
		JsonArray weapons = new JsonParser().parse(reader).getAsJsonObject().getAsJsonArray("weapons");
		for (JsonElement weapon : weapons) {
			if (weapon.getAsJsonObject().get("className").getAsString().equals("Shotgun")) {
				weapon1 = new Shotgun(weapon.getAsJsonObject());
				break;
			}
		}
	}

	@Before
	public void setUp() throws Exception {
		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("player" + i);
		}

		ModelDriver model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		GameBoard gameBoard = model.getGameBoard();
		weapon1.setGameBoard(model.getGameBoard());


		player1 = model.getGameBoard().getPlayers().get(0);
		player2 = model.getGameBoard().getPlayers().get(1);
		weapon1.setOwner(player1);
		player1.getPlayerBoard().addWeapon(weapon1);


		for(PowerupCard powerupCard : player1.getPlayerBoard().getPowerupCards()) {
			player1.getPlayerBoard().removePowerup(0);
		}
		PowerupCard powerupCard = new Teleporter(AmmoType.RED_AMMO);
		player1.getPlayerBoard().addPowerup(powerupCard);
		powerupCard.setOwner(player1);
		powerupCard.setGameBoard(gameBoard);


		GameMap gameMap = gameBoard.getGameMap();
		gameMap.movePlayerTo(gameBoard.getPlayers().get(0), new Coordinates(0, 0));
		gameMap.movePlayerTo(gameBoard.getPlayers().get(1), new Coordinates(0, 0));
		gameMap.movePlayerTo(gameBoard.getPlayers().get(2), new Coordinates(0, 1));
		gameMap.movePlayerTo(gameBoard.getPlayers().get(3), new Coordinates(0, 1));
	}

	@Test
	public void setConnected_correctInput_correctOutput() {
		player1.setConnected(true);
		Assert.assertTrue(player1.isConnected());
		player1.setConnected(false);
		Assert.assertFalse(player1.isConnected());
	}

	@Test
	public void getPlayerName_correctInput_correctOutput() {
		Assert.assertEquals("player0", player1.getPlayerName());
	}

	@Test
	public void getPgName_correctInput_correctOutput() {
		Assert.assertEquals(player1.getPlayerColor().getPgName(), player1.getPgName());
	}

	@Test
	public void getPlayerID_correctInput_correctOutput() {
		Assert.assertEquals(0, player1.getPlayerID());
	}

	@Test
	public void setDamageStatus_correctInput_correctOutput() {
		DamageStatus ds = new HighDamage();
		player1.setDamageStatus(ds);
		Assert.assertEquals(ds, player1.getDamageStatus());
		Assert.assertEquals(ds.getAvailableMacroActions(), player1.getAvailableActions());
	}

	@Test
	public void addDamage_correctInput_correctOutput() {
		player1.addDamage(player2, 1);
		assertNotSame(TurnStatus.DEAD, player1.getTurnStatus());
		player1.addDamage(player2, GameConstants.DEATH_DAMAGE - 1);
		assertEquals(TurnStatus.DEAD, player1.getTurnStatus());
		player1.setTurnStatus(TurnStatus.IDLE);
		assertEquals(TurnStatus.IDLE, player1.getTurnStatus());
	}

	@Test
	public void shootProcess_correctInput_correctOutput() {
		assertFalse(player1.isShootingWeapon());
		player1.initialWeaponActivation(0);
		assertTrue(player1.isShootingWeapon());
		assertFalse(player1.isTheWeaponConcluded());
		player1.doWeaponStep(0);
		assertTrue(player1.isShootingWeapon());
		assertFalse(player1.isTheWeaponConcluded());
		assertEquals(0, player1.getPlayersHitWithWeapon().size());
		player1.handleWeaponEnd();
		assertFalse(player1.isShootingWeapon());
		assertFalse(player1.getPlayerBoard().getWeaponCards().get(0).isLoaded());
		player1.reload(0);
		assertTrue(player1.getPlayerBoard().getWeaponCards().get(0).isLoaded());
	}

	@Test
	public void powerupActivationProcess_correctInput_correctOutput() {
		assertFalse(player1.isPowerupInExecution());
		player1.initialPowerupActivation(0);
		assertTrue(player1.isPowerupInExecution());
		assertFalse(player1.isThePowerupConcluded());
		player1.doPowerupStep(0);
		assertTrue(player1.isPowerupInExecution());
		assertTrue(player1.isThePowerupConcluded());
		player1.handlePowerupEnd();
		assertFalse(player1.isPowerupInExecution());
	}

	@Test
	public void getRep_askedByAnotherPlayer_shouldHideSensitiveInformation() {
		player1.updateRep();
		assertTrue(((PlayerRep) player1.getRep(player2.getPlayerName())).isHidden());
	}
}