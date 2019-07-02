package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class of the weapon Heatseeker.
 */
public class Heatseeker extends WeaponCard {

    public Heatseeker(JsonObject parameters) {
        super(parameters);
        this.getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
    }

    @Override
    public QuestionContainer doActivationStep(int choice) {
        incrementCurrentStep();
        return handlePrimaryFire(choice);
    }

    @Override
    public void primaryFire() {
        dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
    }


    @Override
    public List<Player> getPrimaryTargets() {
        List<Player> allPlayers = getAllPlayers();
        List<Player> nonVisiblePlayers = allPlayers.stream()
                .filter(player -> getGameMap().isInTheMap(player))
                .collect(Collectors.toList());
        nonVisiblePlayers.removeAll(getGameMap().getVisiblePlayers(getOwner()));
        nonVisiblePlayers.remove(getOwner());
        return nonVisiblePlayers;
    }

    @Override
    QuestionContainer handlePrimaryFire(int choice) {
        if (getCurrentStep() == 1) {
            setCurrentTargets(getPrimaryTargets());
            return getTargetPlayersQnO(getCurrentTargets());
        } else if (getCurrentStep() == 2) {
            setTarget(getCurrentTargets().get(choice));
            primaryFire();
        }
        return null;
    }

    @Override
    public QuestionContainer initialQuestion() {
        return null;
    }

}