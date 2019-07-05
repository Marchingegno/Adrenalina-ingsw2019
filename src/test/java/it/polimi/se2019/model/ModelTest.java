package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.Newton;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.powerups.TargetingScope;
import it.polimi.se2019.model.cards.powerups.Teleporter;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.HighDamage;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.PlayerRepPosition;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {
    private Model model;
    private final static String player1Name = "player1";
    private final static String player2Name = "player2";
    private final static String player3Name = "player3";
    private final static String player4Name = "player4";
    private final static String player5Name = "player5";


    @Before
    public void setUp() throws Exception {
        List<String> playersNames = new ArrayList<>();
        playersNames.add(player1Name);
        playersNames.add(player2Name);
        playersNames.add(player3Name);
        playersNames.add(player4Name);
        playersNames.add(player5Name);

		model = new Model(GameConstants.MapType.MEDIUM1_MAP.getMapName(), playersNames, 6);
    }

    @Test (expected = IllegalArgumentException.class)
    public void model_invalidSkulls_shouldThrowException() {
        List<String> players = new ArrayList<>();
        for (int i = 0; i < GameConstants.MIN_PLAYERS; i++) {
            players.add("" + i);
        }
        new Model(GameConstants.MapType.SMALL_MAP.getMapName(), players, GameConstants.MAX_SKULLS + 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void model_invalidPlayers_shouldThrowException() {
        List<String> players = new ArrayList<>();
        for (int i = 0; i < GameConstants.MIN_PLAYERS - 1; i++) {
            players.add("" + i);
        }
        new Model(GameConstants.MapType.SMALL_MAP.getMapName(), players, GameConstants.MAX_SKULLS);
    }

    @Test
    public void addGameBoardObserver_correctInput_correctOutput() {
        assertEquals(0, model.getGameBoard().countObservers());
        VirtualView virtualView = new VirtualView(null);
        model.addGameBoardObserver(virtualView.getGameBoardObserver());
        assertEquals(1, model.getGameBoard().countObservers());
    }

    @Test
    public void addGameMapObserver_correctInput_correctOutput() {
        assertEquals(0, model.getGameBoard().getGameMap().countObservers());
        VirtualView virtualView = new VirtualView(null);
        model.addGameMapObserver(virtualView.getGameBoardObserver());
        assertEquals(1, model.getGameBoard().getGameMap().countObservers());
    }

    @Test
    public void addPlayersObserver_correctInput_correctOutput() {
        assertEquals(0, model.getGameBoard().countObservers());
        VirtualView virtualView = new VirtualView(null);
        model.addGameBoardObserver(virtualView.getGameBoardObserver());
        assertEquals(1, model.getGameBoard().countObservers());
    }

    @Test
    public void canGameContinue_correctInput_correctOutput() {
        assertTrue(model.canGameContinue());
    }


    @Test
    public void isGameEnded_correctInput_correctOutput() {
        assertFalse(model.isGameEnded());
    }

    @Test
    public void isFrenzyStarted_correctInput_correctOutput() {
        assertFalse(model.isGameEnded());
    }

    @Test
    public void startFrenzy_correctInput_correctOutput() {
        assertFalse(model.isFrenzyStarted());
        model.startFrenzy();
        assertTrue(model.isFrenzyStarted());
    }

    @Test
    public void areSkullsFinished_correctInput_correctOutput() {
        assertFalse(model.areSkullsFinished());
    }

    @Test
    public void scoreDeadPlayers_correctInput_correctOutput() {
        List<Player> playerList = model.getGameBoard().getPlayers();
        assertEquals(0, playerList.get(0).getPlayerBoard().getPoints());
        playerList.get(0).addDamage(playerList.get(1), 6);
        playerList.get(0).addDamage(playerList.get(2), 6);
        model.scoreDeadPlayers();
        assertEquals(9, playerList.get(1).getPlayerBoard().getPoints());
        assertEquals(6, playerList.get(2).getPlayerBoard().getPoints());
    }

    @Test(expected = IllegalStateException.class)
    public void isTheWeaponConcluded_correctInput_throwsException() {
        model.isTheWeaponConcluded("player1");
    }

    @Test(expected = IllegalStateException.class)
    public void isThePowerupConcluded_correctInput_throwsException() {
        model.isThePowerupConcluded("player1");
    }

    @Test
    public void fillGameMap_correctInput_correctOutput() {
        model.fillGameMap();
    }

    @Test
    public void flipPlayersWithNoDamage_correctInput_correctOutput() {
        model.flipPlayersWithNoDamage();
        for (Player player : model.getGameBoard().getPlayers()) {
            assertTrue(player.getPlayerBoard().isFlipped());
        }
    }

    @Test
    public void payment_correctInput_correctOutput() {
        assertFalse(model.hasCurrentPlayerPayed());
        model.setPayed(true);
        assertTrue(model.hasCurrentPlayerPayed());
    }

    @Test
    public void saveEvent_correctInput_correctOutput() {
        Event event = new Event(null, null);
        assertNull(model.resumeAction());
        model.saveEvent(event);
        assertEquals(event, model.resumeAction());
    }

    @Test
    public void getPriceOfTheChosenWeapon_correctInput_correctOutput() {
        Player player1 = model.getGameBoard().getCurrentPlayer();
        model.getGameBoard().getGameMap().movePlayerTo(player1, new Coordinates(0, 2));
        assertNotNull(model.getPriceOfTheChosenWeapon(1));
    }

    @Test
	public void macroAction_correctInput_correctOutput() {
		model.setNextMacroAction(model.getCurrentPlayerName(), 0);
        assertTrue(model.isInAMacroAction(model.getCurrentPlayerName()));
        model.endAction(model.getCurrentPlayerName());
        assertFalse(model.isInAMacroAction(model.getCurrentPlayerName()));
    }

    @Test
    public void getPriceOfTheSelectedWeapon_correctInput_correctOutput() {
        model.getGameBoard().getCurrentPlayer().getPlayerBoard().addWeapon((WeaponCard) model.getGameBoard().getGameMap().grabCard(new Coordinates(0, 2), 0));
        assertNotNull(model.getPriceOfTheSelectedWeapon(0));
    }

    @Test
    public void setAsDisconnected_correctInput_correctOutput() {
        model.setAsDisconnected(model.getCurrentPlayerName());
        assertFalse(model.getGameBoard().getCurrentPlayer().isConnected());
    }

    @Test
    public void setAsDisconnected_numOfConnectedPlayerLessThanMINPLAYERS_endingTheGame() {
        model.setAsDisconnected(player1Name);
        model.setAsDisconnected(player2Name);
        model.setAsDisconnected(player3Name);
        model.setAsDisconnected(player4Name);
        assertTrue(model.isGameEnded());
    }

    @Test
    public void setAsReconnected_correctInput_correctOutput() {
        model.setAsDisconnected(model.getCurrentPlayerName());
        assertFalse(model.getGameBoard().getCurrentPlayer().isConnected());
        model.setAsReconnected(model.getCurrentPlayerName());
        assertTrue(model.getGameBoard().getCurrentPlayer().isConnected());
    }

    @Test
    public void cancelAction_correctInput_correctOutput() {
        model.cancelAction(model.getCurrentPlayerName());
        assertFalse(model.getGameBoard().getCurrentPlayer().isShootingWeapon());
        assertFalse(model.getGameBoard().getCurrentPlayer().isPowerupInExecution());
        assertFalse(model.getGameBoard().getCurrentPlayer().getDamageStatus().hasMacroActionLeft());
    }

    @Test
    public void movePlayerTo_correctInput_correctOutput() {
        model.movePlayerTo(model.getCurrentPlayerName(), new Coordinates(0, 0));
        assertEquals(new Coordinates(0, 0), model.getGameBoard().getGameMap().getPlayerCoordinates(model.getGameBoard().getCurrentPlayer()));
    }

    @Test
    public void spawnPlayer_correctInput_correctOutput() {
        model.spawnPlayer(model.getCurrentPlayerName(), 0);
        assertNotNull(model.getGameBoard().getGameMap().getPlayerCoordinates(model.getGameBoard().getCurrentPlayer()));
    }

    @Test
    public void addPowerupCardTo_correctInput_correctOutput() {
        int numOfStartingPowerups = model.getGameBoard().getCurrentPlayer().getPlayerBoard().getPowerupCards().size();
        model.addPowerupCardTo(model.getCurrentPlayerName());
        int numOfPowerupsAtTheEnd = model.getGameBoard().getCurrentPlayer().getPlayerBoard().getPowerupCards().size();
        assertEquals(numOfStartingPowerups + 1, numOfPowerupsAtTheEnd);
    }

    @Test
    public void nextPlayerTurn_correctInput_correctOutput() {
        List<Player> playerList = model.getGameBoard().getPlayers();
        model.spawnPlayer(playerList.get(0).getPlayerName(), 0);
        assertEquals(TurnStatus.YOUR_TURN, model.getTurnStatus(playerList.get(0).getPlayerName()));
        assertEquals(TurnStatus.PRE_SPAWN, model.getTurnStatus(playerList.get(1).getPlayerName()));
        model.nextPlayerTurn();
        model.spawnPlayer(playerList.get(1).getPlayerName(), 0);
        assertEquals(TurnStatus.IDLE, model.getTurnStatus(playerList.get(0).getPlayerName()));
        assertEquals(TurnStatus.YOUR_TURN, model.getTurnStatus(playerList.get(1).getPlayerName()));
    }

    @Test
    public void setCorrectDamageStatus_correctInput_correctOutput() {
        List<Player> playerList = model.getGameBoard().getPlayers();
        model.spawnPlayer(model.getCurrentPlayerName(), 0);
        playerList.get(0).addDamage(playerList.get(1), 5);
        playerList.get(0).addDamage(playerList.get(2), 5);
        model.setCorrectDamageStatus(playerList.get(0).getPlayerName());
        assertEquals(new HighDamage().toString(), playerList.get(0).getDamageStatus().toString());
    }

    @Test
    public void getTurnStatus_correctInput_correctOutput() {
        assertEquals(TurnStatus.PRE_SPAWN, model.getTurnStatus(model.getCurrentPlayerName()));
        model.spawnPlayer(model.getCurrentPlayerName(), 0);
        assertEquals(TurnStatus.YOUR_TURN, model.getTurnStatus(model.getCurrentPlayerName()));
        model.getGameBoard().getPlayers().get(1).addDamage(model.getGameBoard().getPlayers().get(0), 11);
        assertEquals(TurnStatus.DEAD, model.getTurnStatus(model.getGameBoard().getPlayers().get(1).getPlayerName()));
        model.nextPlayerTurn();
        assertEquals(TurnStatus.IDLE, model.getTurnStatus(model.getGameBoard().getPlayers().get(0).getPlayerName()));
    }

    @Test
    public void setTurnStatusOfCurrentPlayer_correctInput_correctOutput() {
        model.setTurnStatusOfCurrentPlayer(TurnStatus.YOUR_TURN);
        assertEquals(TurnStatus.YOUR_TURN, model.getGameBoard().getCurrentPlayer().getTurnStatus());
    }

    @Test
    public void currPlayerHasWeaponInventoryFull_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        assertFalse(model.currPlayerHasWeaponInventoryFull());
        player.getPlayerBoard().addWeapon((WeaponCard) model.getGameBoard().getGameMap().grabCard(new Coordinates(0, 2), 0));
        assertFalse(model.currPlayerHasWeaponInventoryFull());

        player.getPlayerBoard().addWeapon((WeaponCard) model.getGameBoard().getGameMap().grabCard(new Coordinates(0, 2), 0));
        assertFalse(model.currPlayerHasWeaponInventoryFull());

        player.getPlayerBoard().addWeapon((WeaponCard) model.getGameBoard().getGameMap().grabCard(new Coordinates(0, 2), 0));
        assertTrue(model.currPlayerHasWeaponInventoryFull());

    }

    @Test
    public void getCoordinatesWherePlayerCanMove_3Movement_correctOutput() {
        model.movePlayerTo(model.getCurrentPlayerName(), new Coordinates(0, 0));
        List<Coordinates> correctAnswer = new ArrayList<>();
        correctAnswer.add(new Coordinates(0, 3));
        correctAnswer.add(new Coordinates(1, 2));
        correctAnswer.add(new Coordinates(0, 1));
        correctAnswer.add(new Coordinates(0, 2));
        correctAnswer.add(new Coordinates(1, 0));
        correctAnswer.add(new Coordinates(0, 0));
        correctAnswer.add(new Coordinates(1, 1));
        correctAnswer.add(new Coordinates(2, 1));
		model.setNextMacroAction(model.getCurrentPlayerName(), 0);
        List<Coordinates> answer = model.getCoordinatesWherePlayerCanMove();

        for (Coordinates coordinates : answer) {
            assertTrue(correctAnswer.contains(coordinates));
        }
        for (Coordinates coordinates : correctAnswer) {
            assertTrue(answer.contains(coordinates));
        }
    }

    @Test
    public void getCoordinatesWherePlayerCanMove_cantGrabEverywhere_correctOutput() {
        model.movePlayerTo(model.getCurrentPlayerName(), new Coordinates(0, 0));
        model.getGameBoard().getGameMap().grabCard(new Coordinates(0, 1), 0);
        model.getGameBoard().getCurrentPlayer().getDamageStatus().setCurrentMacroActionIndex(1);
        List<Coordinates> correctAnswer = new ArrayList<>();
        correctAnswer.add(new Coordinates(0, 0));
        correctAnswer.add(new Coordinates(1, 0));
        List<Coordinates> answer = model.getCoordinatesWherePlayerCanMove();

        for (Coordinates coordinates : answer) {
            assertTrue(correctAnswer.contains(coordinates));
        }
        for (Coordinates coordinates : correctAnswer) {
            assertTrue(answer.contains(coordinates));
        }
    }

    @Test
    public void pay_PriceYRR_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        player.getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.RED_AMMO);
        player.getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.YELLOW_AMMO);
        player.getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.BLUE_AMMO);
        player.getPlayerBoard().removePowerup(0);
        List<AmmoType> price = new ArrayList<>();
        price.add(AmmoType.YELLOW_AMMO);
        price.add(AmmoType.RED_AMMO);
        price.add(AmmoType.RED_AMMO);

        player.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO);
        player.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO);
        player.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.BLUE_AMMO);
        player.getPlayerBoard().addPowerup(new Newton(AmmoType.YELLOW_AMMO));
        player.getPlayerBoard().addPowerup(new Newton(AmmoType.RED_AMMO));

        List<Integer> indexesOfThePowerups = new ArrayList<>();
        indexesOfThePowerups.add(0);

        model.pay(player.getPlayerName(), price, indexesOfThePowerups);

        assertEquals(0, player.getPlayerBoard().getAmmoContainer().getAmmo(AmmoType.YELLOW_AMMO));
        assertEquals(0, player.getPlayerBoard().getAmmoContainer().getAmmo(AmmoType.RED_AMMO));
        assertEquals(1, player.getPlayerBoard().getAmmoContainer().getAmmo(AmmoType.BLUE_AMMO));
        assertEquals(1, player.getPlayerBoard().getPowerupCards().size());
    }

    @Test
    public void canUsePowerupToPay_correctInput_playerCannotPayWithPowerups() {
        Player player = model.getGameBoard().getCurrentPlayer();
        player.getPlayerBoard().removePowerup(0);
        List<AmmoType> price = new ArrayList<>();
        price.add(AmmoType.YELLOW_AMMO);
        price.add(AmmoType.RED_AMMO);
        price.add(AmmoType.RED_AMMO);
        player.getPlayerBoard().addPowerup(new Newton(AmmoType.BLUE_AMMO));

        assertFalse(model.canUsePowerupToPay(player.getPlayerName(), price));
    }

    @Test
    public void canUsePowerupToPay_correctInput_playerCanPayWithPowerups() {
        Player player = model.getGameBoard().getCurrentPlayer();
        player.getPlayerBoard().removePowerup(0);
        List<AmmoType> price = new ArrayList<>();
        price.add(AmmoType.YELLOW_AMMO);
        price.add(AmmoType.RED_AMMO);
        price.add(AmmoType.RED_AMMO);
        player.getPlayerBoard().addPowerup(new Newton(AmmoType.RED_AMMO));

        assertTrue(model.canUsePowerupToPay(player.getPlayerName(), price));
    }

    @Test
    public void canAffordWithOnlyAmmo_correcrtInput_playerCannotAfford() {
        Player player = model.getGameBoard().getCurrentPlayer();
        player.getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.RED_AMMO);
        player.getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.YELLOW_AMMO);
        player.getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.BLUE_AMMO);
        List<AmmoType> price = new ArrayList<>();
        price.add(AmmoType.YELLOW_AMMO);
        price.add(AmmoType.RED_AMMO);
        price.add(AmmoType.RED_AMMO);
        assertFalse(model.canAffordWithOnlyAmmo(player.getPlayerName(), price));
    }

    @Test
    public void canAffordWithOnlyAmmo_correcrtInput_playerCanAfford() {
        Player player = model.getGameBoard().getCurrentPlayer();
        player.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO);
        List<AmmoType> price = new ArrayList<>();
        price.add(AmmoType.YELLOW_AMMO);
        price.add(AmmoType.RED_AMMO);
        price.add(AmmoType.RED_AMMO);
        assertTrue(model.canAffordWithOnlyAmmo(player.getPlayerName(), price));
    }

    @Test
    public void getIndexesOfTheGrabbableWeaponCurrentPlayer_correctInput_correctOutput() {
        model.movePlayerTo("player1", new Coordinates(0, 2));
        List<Integer> indexes = new ArrayList<>();
        indexes.add(0);
        indexes.add(1);
        indexes.add(2);
        assertEquals(indexes, model.getIndexesOfTheGrabbableWeaponCurrentPlayer());
    }

    @Test()
    public void getIndexesOfTheGrabbableWeaponCurrentPlayer_playerCanPayOnlyWithPowerups_canAfford() {
        WeaponCard weaponCard = model.getGameBoard().getWeaponDeck().drawCard();
        List<AmmoType> weaponPrice = weaponCard.getGrabPrice();
        Player player = model.getGameBoard().getCurrentPlayer();

        while (weaponPrice.isEmpty()) {
            weaponCard = model.getGameBoard().getWeaponDeck().drawCard();
            weaponPrice = weaponCard.getGrabPrice();
            if (model.getGameBoard().getWeaponDeck().isEmpty())
                return;
        }

        player.getPlayerBoard().removePowerup(0);
        player.getPlayerBoard().addPowerup(new Newton(weaponPrice.get(0)));
        if (weaponPrice.size() > 1)
            player.getPlayerBoard().addPowerup(new Newton(weaponPrice.get(1)));
        player.getPlayerBoard().getAmmoContainer().removeAmmo(weaponPrice);
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.getGameBoard().getGameMap().getPlayerSquare(player).grabCard(0);
        model.getGameBoard().getGameMap().getPlayerSquare(player).grabCard(0);
        model.getGameBoard().getGameMap().getPlayerSquare(player).grabCard(0);
        model.getGameBoard().getGameMap().getPlayerSquare(player).addCard(weaponCard);
        assertTrue(model.getIndexesOfTheGrabbableWeaponCurrentPlayer().contains(0));
    }

    @Test
    public void getGrabMessageType_playerInAmmoSquare_grabAmmoMessage() {
        model.movePlayerTo(model.getCurrentPlayerName(), new Coordinates(0, 0));
        assertEquals(MessageType.GRAB_AMMO, model.getGrabMessageType());
    }

    @Test
    public void getGrabMessageType_playerInSpawnSquare_grabWeaponMessage() {
        model.movePlayerTo(model.getCurrentPlayerName(), new Coordinates(0, 2));
        assertEquals(MessageType.GRAB_WEAPON, model.getGrabMessageType());
    }

    @Test
    public void grabWeaponCard_correctInput_correctOutput() {
        model.movePlayerTo(model.getCurrentPlayerName(), new Coordinates(0, 2));
        WeaponCard weaponToGrab = (WeaponCard) model.getGameBoard().getGameMap().getPlayerSquare(model.getGameBoard().getCurrentPlayer()).getCards().get(1);
        model.grabWeaponCard(model.getCurrentPlayerName(), 1);
        assertEquals(2, model.getGameBoard().getGameMap().getPlayerSquare(model.getGameBoard().getCurrentPlayer()).getCards().size());
        assertEquals(weaponToGrab, model.getGameBoard().getCurrentPlayer().getPlayerBoard().getWeaponCards().get(0));
    }

    @Test
    public void grabAmmoCard_correctInput_correctOutput() {
        model.movePlayerTo(model.getCurrentPlayerName(), new Coordinates(0, 0));
        model.getGameBoard().getCurrentPlayer().getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.BLUE_AMMO);
        model.getGameBoard().getCurrentPlayer().getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.RED_AMMO);
        model.getGameBoard().getCurrentPlayer().getPlayerBoard().getAmmoContainer().removeAmmo(AmmoType.YELLOW_AMMO);
        AmmoCard ammoCardToGrab = (AmmoCard) model.getGameBoard().getGameMap().getPlayerSquare(model.getGameBoard().getCurrentPlayer()).getCards().get(0);
        List<AmmoType> ammo = ammoCardToGrab.getAmmo();

        model.grabAmmoCard(model.getCurrentPlayerName());

        assertTrue(model.canAffordWithOnlyAmmo(model.getCurrentPlayerName(), ammo));
        if (ammoCardToGrab.hasPowerup())
            assertEquals(2, model.getGameBoard().getCurrentPlayer().getPlayerBoard().getPowerupCards().size());
    }

    @Test
    public void swapWeapons_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.grabWeaponCard(player.getPlayerName(), 0);
        WeaponCard playerWeapon = player.getPlayerBoard().getWeaponCards().get(0);
        model.movePlayerTo(player.getPlayerName(), new Coordinates(1, 0));
        WeaponCard weaponCardInSquare = (WeaponCard) model.getGameBoard().getGameMap().getPlayerSquare(player).getCards().get(0);
        model.swapWeapons(0, 0);

        assertEquals(player.getPlayerBoard().getWeaponCards().get(0), weaponCardInSquare);
        assertEquals(model.getGameBoard().getGameMap().getPlayerSquare(player).getCards().get(2), playerWeapon);
    }

    @Test
    public void reloadWeapon_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.grabWeaponCard(player.getPlayerName(), 0);
        player.getPlayerBoard().getWeaponCards().get(0).reset();
        assertFalse(player.getPlayerBoard().getWeaponCards().get(0).isLoaded());
        model.reloadWeapon(player.getPlayerName(), 0);
        assertTrue(player.getPlayerBoard().getWeaponCards().get(0).isLoaded());
    }


    @Test
    public void endGameAndFindWinner_noTies_correctOutput() {
        List<PlayerRepPosition> finalLeaderboard;

        model.getGameBoard().addKillShot(model.getPlayerFromName(player1Name), true);
        model.getGameBoard().addKillShot(model.getPlayerFromName(player1Name), true);
        model.getGameBoard().addKillShot(model.getPlayerFromName(player2Name), true);
        model.getGameBoard().addKillShot(model.getPlayerFromName(player3Name), false);
        model.endGameAndFindWinner();
        finalLeaderboard = model.getFinalPlayersInfo();

        assertEquals((int) GameConstants.KILLSHOT_SCORES.get(0), model.getPlayerFromName(player1Name).getPlayerBoard().getPoints());
        assertEquals((int) GameConstants.KILLSHOT_SCORES.get(1), model.getPlayerFromName(player2Name).getPlayerBoard().getPoints());
        assertEquals((int) GameConstants.KILLSHOT_SCORES.get(2), model.getPlayerFromName(player3Name).getPlayerBoard().getPoints());
        assertEquals(0, model.getPlayerFromName(player4Name).getPlayerBoard().getPoints());

        assertEquals(finalLeaderboard.get(0).getPlayerReps().get(0), model.getPlayerFromName(player1Name).getRep());
        assertEquals(finalLeaderboard.get(1).getPlayerReps().get(0), model.getPlayerFromName(player2Name).getRep());
        assertEquals(finalLeaderboard.get(2).getPlayerReps().get(0), model.getPlayerFromName(player3Name).getRep());
        assertEquals(finalLeaderboard.get(3).getPlayerReps().get(0), model.getPlayerFromName(player4Name).getRep());
        assertEquals(finalLeaderboard.get(3).getPlayerReps().get(1), model.getPlayerFromName(player5Name).getRep());
    }

    @Test
    public void endGameAndFindWinner_resolvingTies_correctOutput() {
        List<PlayerRepPosition> finalLeaderboard;

        model.getGameBoard().addKillShot(model.getPlayerFromName(player2Name), true);
        model.getGameBoard().addKillShot(model.getPlayerFromName(player1Name), true);
        model.getGameBoard().addKillShot(model.getPlayerFromName(player1Name), true);
        model.getGameBoard().addKillShot(model.getPlayerFromName(player3Name), false);
        int gapPoints = GameConstants.KILLSHOT_SCORES.get(0) - GameConstants.KILLSHOT_SCORES.get(1);
        model.getPlayerFromName(player2Name).getPlayerBoard().addPoints(gapPoints);


        model.endGameAndFindWinner();
        finalLeaderboard = model.getFinalPlayersInfo();

        assertEquals(model.getPlayerFromName(player1Name).getPlayerBoard().getPoints(), model.getPlayerFromName(player2Name).getPlayerBoard().getPoints());
        assertEquals((int) GameConstants.KILLSHOT_SCORES.get(2), model.getPlayerFromName(player3Name).getPlayerBoard().getPoints());

        assertEquals(0, model.getPlayerFromName(player4Name).getPlayerBoard().getPoints());

        assertEquals(finalLeaderboard.get(0).getPlayerReps().get(0), model.getPlayerFromName(player2Name).getRep());
        assertEquals(finalLeaderboard.get(1).getPlayerReps().get(0), model.getPlayerFromName(player1Name).getRep());
        assertEquals(finalLeaderboard.get(2).getPlayerReps().get(0), model.getPlayerFromName(player3Name).getRep());
        assertEquals(finalLeaderboard.get(3).getPlayerReps().get(0), model.getPlayerFromName(player4Name).getRep());
        assertEquals(finalLeaderboard.get(3).getPlayerReps().get(1), model.getPlayerFromName(player5Name).getRep());
    }

    @Test
    public void doesThePlayerHaveActionsLeft_correctInput_correctOutput() {
        assertTrue(model.doesThePlayerHaveActionsLeft(model.getCurrentPlayerName()));
        model.getGameBoard().getCurrentPlayer().getDamageStatus().decreaseMacroActionsToPerform();
        model.getGameBoard().getCurrentPlayer().getDamageStatus().decreaseMacroActionsToPerform();
        assertFalse(model.doesThePlayerHaveActionsLeft(model.getCurrentPlayerName()));

    }

    @Test
    public void getCurrentAction_correctInput_correctOutput() {
		model.setNextMacroAction(model.getCurrentPlayerName(), 0);
        assertNotNull(model.getCurrentAction());
    }

    @Test
    public void getNextActionToExecuteAndAdvance_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.setNextMacroAction(player.getPlayerName(), 0);
        ActionType nextAction = model.getNextActionToExecuteAndAdvance(player.getPlayerName());
        assertEquals(ActionType.MOVE, nextAction);
        nextAction = model.getNextActionToExecuteAndAdvance(player.getPlayerName());
        assertEquals(ActionType.END, nextAction);
    }

    @Test
    public void canWeaponBeActivated_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.grabWeaponCard(player.getPlayerName(), 0);
        assertFalse(model.canWeaponBeActivated(player.getPlayerName(), -1));
        assertFalse(model.canWeaponBeActivated(player.getPlayerName(), 1));
        player.getPlayerBoard().getWeaponCards().get(0).reset();
        assertFalse(model.canWeaponBeActivated(player.getPlayerName(), 0));
    }

    @Test
    public void getActivableWeapons_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.grabWeaponCard(player.getPlayerName(), 0);
        model.grabWeaponCard(player.getPlayerName(), 0);
        model.grabWeaponCard(player.getPlayerName(), 0);
        assertTrue(model.getActivableWeapons(player.getPlayerName()).isEmpty());
    }

    @Test
    public void getLoadableWeapons_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.grabWeaponCard(player.getPlayerName(), 0);
        model.grabWeaponCard(player.getPlayerName(), 0);
        model.grabWeaponCard(player.getPlayerName(), 0);
        player.getPlayerBoard().getWeaponCards().get(0).reset();
        player.getPlayerBoard().getWeaponCards().get(2).reset();
        player.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO, 2);
        player.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.BLUE_AMMO, 2);
        player.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.YELLOW_AMMO, 2);
        List<Integer> correctAnswer = new ArrayList<>();
        correctAnswer.add(0);
        correctAnswer.add(2);
        assertEquals(correctAnswer, model.getLoadableWeapons(player.getPlayerName()));
    }

    @Test
    public void doesPlayerHaveLoadedWeapons_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.grabWeaponCard(player.getPlayerName(), 0);
        player.getPlayerBoard().getWeaponCards().get(0).reset();
        assertFalse(model.doesPlayerHaveLoadedWeapons(player.getPlayerName()));
        model.grabWeaponCard(player.getPlayerName(), 0);
        assertTrue(model.doesPlayerHaveLoadedWeapons(player.getPlayerName()));
    }

    @Test
    public void isShootingWeapon_correctInput_correctOutput() {
        Player player = model.getGameBoard().getCurrentPlayer();
        model.movePlayerTo(player.getPlayerName(), new Coordinates(0, 2));
        model.grabWeaponCard(player.getPlayerName(), 0);
        assertFalse(model.isShootingWeapon(player.getPlayerName()));
        model.initialWeaponActivation(player.getPlayerName(), 0);
        assertTrue(model.isShootingWeapon(player.getPlayerName()));
    }

    @Test
    public void canPowerupBeActivated_wrongIndex_shouldGiveFalse() {
        Player player = model.getGameBoard().getCurrentPlayer();
        assertFalse(model.canPowerupBeActivated(player.getPlayerName(), -1));
    }

    @Test
    public void onShootPowerups_correctInput_correctOutput() {
        // Prepare powerups.
        Player player1 = model.getGameBoard().getPlayers().get(0);
        Player player2 = model.getGameBoard().getPlayers().get(1);

        for(PowerupCard powerupCard : player1.getPlayerBoard().getPowerupCards()) {
            player1.getPlayerBoard().removePowerup(0);
        }
        PowerupCard powerupCard = new TargetingScope(AmmoType.RED_AMMO);
        player1.getPlayerBoard().addPowerup(powerupCard);
        powerupCard.setOwner(player1);
        powerupCard.setGameBoard(model.getGameBoard());
        List<Player> shotPlayers = new ArrayList<>();
        shotPlayers.add(player2);
        powerupCard.setShotPlayers(shotPlayers);

        player1.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO);

        // Asserts.
        assertTrue(model.doesPlayerHaveActivableOnShootPowerups(player1.getPlayerName()));
        assertEquals(1, model.getActivableOnShootPowerups(player1.getPlayerName()).size());
    }

    @Test
    public void onTurnPowerup_correctInput_correctOutput() {
        // Prepare powerups.
        Player player1 = model.getGameBoard().getPlayers().get(0);

        for(PowerupCard powerupCard : player1.getPlayerBoard().getPowerupCards()) {
            player1.getPlayerBoard().removePowerup(0);
        }
        PowerupCard powerupCard = new Teleporter(AmmoType.RED_AMMO);
        player1.getPlayerBoard().addPowerup(powerupCard);
        powerupCard.setOwner(player1);
        powerupCard.setGameBoard(model.getGameBoard());

        model.getGameBoard().getGameMap().movePlayerTo(model.getGameBoard().getPlayers().get(0), new Coordinates(0, 0));

        // Asserts.
        assertTrue(model.doesPlayerHaveActivableOnTurnPowerups(player1.getPlayerName()));
        assertEquals(1, model.getActivableOnTurnPowerups(player1.getPlayerName()).size());

        // Start utilization.
        assertFalse(model.isPowerupInExecution(player1.getPlayerName()));
        model.initialPowerupActivation(player1.getPlayerName(), 0);
        assertTrue(model.isPowerupInExecution(player1.getPlayerName()));
        assertFalse(model.isThePowerupConcluded(player1.getPlayerName()));
        model.doPowerupStep(player1.getPlayerName(), 0);
        assertTrue(model.isPowerupInExecution(player1.getPlayerName()));
        assertTrue(model.isThePowerupConcluded(player1.getPlayerName()));
        model.handlePowerupEnd(player1.getPlayerName());
        assertFalse(model.isPowerupInExecution(player1.getPlayerName()));
    }

	@Test
	public void addSpawnPowerupCardTp_correctBehaviour() {
		Player player1 = model.getGameBoard().getPlayers().get(0);
		int powerupsCount = player1.getPlayerBoard().getPowerupCards().size();
		model.addSpawnPowerupCardTo(player1.getPlayerName());
		assertEquals(powerupsCount + 1, player1.getPlayerBoard().getPowerupCards().size());
	}
}