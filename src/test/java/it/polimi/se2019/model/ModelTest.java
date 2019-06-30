package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.HighDamage;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ModelTest {
    private Model model;

    @Before
    public void setUp() throws Exception {
        List<String> playersNames = new ArrayList<>();
        playersNames.add("player1");
        playersNames.add("player2");
        playersNames.add("player3");
        playersNames.add("player4");
        playersNames.add("player5");

        model = new Model(GameConstants.MapType.MEDIUM_MAP.getMapName(), playersNames, 6);
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
        assertFalse(model.canGameContinue());
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
    public void macroAcrtion_correctInput_correctOutput() {
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
        model.setAsDisconnected("player1");
        model.setAsDisconnected("player2");
        model.setAsDisconnected("player3");
        model.setAsDisconnected("player4");
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
    public void pay() {
    }

    @Test
    public void pay1() {
    }

    @Test
    public void canUsePowerupToPay() {
    }

    @Test
    public void canAffordWithOnlyAmmo() {
    }

    @Test
    public void getIndexesOfTheGrabbableWeaponCurrentPlayer() {
    }

    @Test
    public void getGrabMessageType() {
    }

    @Test
    public void grabWeaponCard() {
    }

    @Test
    public void grabAmmoCard() {
    }

    @Test
    public void swapWeapons() {
    }

    @Test
    public void reloadWeapon() {
    }

    @Test
    public void getReachableCoordinates() {
    }

    @Test
    public void endGameAndFindWinner() {
    }

    @Test
    public void getFinalPlayersInfo() {
    }

    @Test
    public void doesThePlayerHaveActionsLeft() {
    }

    @Test
    public void getCurrentAction() {
    }

    @Test
    public void getNextActionToExecuteAndAdvance() {
    }

    @Test
    public void setNextMacroAction() {
    }

    @Test
    public void canWeaponBeActivated() {
    }

    @Test
    public void getActivableWeapons() {
    }

    @Test
    public void getLoadableWeapons() {
    }

    @Test
    public void doesPlayerHaveLoadedWeapons() {
    }

    @Test
    public void isShootingWeapon() {
    }

    @Test
    public void initialWeaponActivation() {
    }

    @Test
    public void doWeaponStep() {
    }

    @Test
    public void handleWeaponEnd() {
    }

    @Test
    public void canPowerupBeActivated() {
    }

    @Test
    public void doesPlayerHaveActivableOnShootPowerups() {
    }

    @Test
    public void getActivableOnShootPowerups() {
    }

    @Test
    public void getNextPlayerWaitingForDamagePowerups() {
    }

    @Test
    public void isPlayerWaitingForDamagePowerupsEmpty() {
    }

    @Test
    public void getActivableOnDamagePowerups() {
    }

    @Test
    public void getActivableOnTurnPowerups() {
    }

    @Test
    public void doesPlayerHaveActivableOnTurnPowerups() {
    }

    @Test
    public void isPowerupInExecution() {
    }

    @Test
    public void initialPowerupActivation() {
    }

    @Test
    public void doPowerupStep() {
    }

    @Test
    public void handlePowerupEnd() {
    }

    @Test
    public void getPlayerFromName() {
    }

    @Test
    public void getGameBoard() {
    }
}