package it.polimi.se2019.model.cards.weapons;


import com.google.gson.JsonObject;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class of the weapon Tractor beam.
 */
public class TractorBeam extends AlternateFireWeapon {

    private List<Coordinates> enemyRelocationCoordinates;

    public TractorBeam(JsonObject parameters) {
        super(parameters);
        this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
        this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));

    }

    @Override
    QuestionContainer handlePrimaryFire(int choice) {
        switch (getCurrentStep()) {
            case 2:
                setCurrentTargets(getPrimaryTargets());
                if (getCurrentTargets().isEmpty()) {
                    Utils.logError("currentTargets is null! See TractorBeam", new IllegalStateException());
                    return null;
                }
                return getTargetPlayersQnO(getCurrentTargets());
            case 3:
                setTarget(getCurrentTargets().get(choice));
                enemyRelocationCoordinates = getEnemyRelocationCoordinates();
                return getMovingTargetEnemyCoordinatesQnO(getTarget(), enemyRelocationCoordinates);
            case 4:
                relocateEnemy(getTarget(), enemyRelocationCoordinates.get(choice));
                primaryFire();
                break;
            default:
                throw new IllegalStateException("Reached an illegal state.");
        }
        return null;
    }

    @Override
    QuestionContainer handleSecondaryFire(int choice) {
        switch (getCurrentStep()) {
            case 2:
                setCurrentTargets(getSecondaryTargets());
                return getTargetPlayersQnO(getCurrentTargets());
            case 3:
                setTarget(getCurrentTargets().get(choice));
                getGameMap().movePlayerTo(getTarget(), getGameMap().getPlayerCoordinates(getOwner()));
                secondaryFire();
                break;
            default:
                throw new IllegalStateException("Reached an illegal state.");
        }
        return null;
    }

    @Override
    public void primaryFire() {
        dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
    }

    @Override
    public void secondaryFire() {
        dealDamageAndConclude(getSecondaryDamagesAndMarks(), getTarget());
    }


    @Override
    public List<Player> getPrimaryTargets() {
        List<Coordinates> visibleCoordinates = getGameMap().getVisibleCoordinates(getOwner());
        //Only players in the map
        List<Player> players = getAllPlayers().stream().filter(player -> getGameMap().isInTheMap(player)).collect(Collectors.toList());
        players.remove(getOwner());
        List<Player> targettablePlayers = new ArrayList<>();
        for (Player player : players) {
            List<Coordinates> intersectionCoordinates = getGameMap().reachableCoordinates(player, 2);
            intersectionCoordinates.retainAll(visibleCoordinates);
            if (!intersectionCoordinates.isEmpty()) {
                targettablePlayers.add(player);
            }

        }
        return targettablePlayers;
    }

    @Override
    public List<Player> getSecondaryTargets() {
        return getGameMap().reachablePlayers(getOwner(), 2);
    }

    /**
     * Find the coordinate in which the enemy can be moved.
     *
     * @return the list of available coordinates.
     */
    private List<Coordinates> getEnemyRelocationCoordinates() {
        List<Coordinates> intersectionCoordinates = getGameMap().reachableCoordinates(getTarget(), 2);
        intersectionCoordinates.retainAll(getGameMap().getVisibleCoordinates(getOwner()));
        return intersectionCoordinates;
    }

    @Override
    public void reset() {
        super.reset();
        enemyRelocationCoordinates = new ArrayList<>();
    }


}