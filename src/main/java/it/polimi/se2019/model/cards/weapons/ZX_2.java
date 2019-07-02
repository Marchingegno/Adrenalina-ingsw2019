package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the weapon ZX_2.
 */
public class ZX_2 extends AlternateFireWeapon {
    private List<Player> secondaryTargets;

    public ZX_2(JsonObject parameters) {
        super(parameters);
        this.setSecondaryDamage(parameters.get("secondaryDamage").getAsInt());
        this.setSecondaryMarks(parameters.get("secondaryMarks").getAsInt());
        getStandardDamagesAndMarks().add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
        for (int i = 0; i < 3; i++) {
            this.getSecondaryDamagesAndMarks().add(new DamageAndMarks(getSecondaryDamage(), getSecondaryMarks()));
        }
    }


    @Override
    QuestionContainer handlePrimaryFire(int choice) {
        if (getCurrentStep() == 2) {
            setCurrentTargets(getPrimaryTargets());
            return getTargetPlayersQnO(getCurrentTargets());
        } else if (getCurrentStep() == 3) {
            setTarget(getCurrentTargets().get(choice));
            primaryFire();
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
                secondaryTargets = new ArrayList<>();
                secondaryTargets.add(getCurrentTargets().remove(choice));
                Utils.logWeapon("This is currentTargets: ");
                getCurrentTargets().forEach(target -> Utils.logWeapon(target.getPlayerName()));
                if (getCurrentTargets().isEmpty()) {
                    terminateSecondaryFire();
                    return null;
                }
                return getTargetPlayersQnO(getCurrentTargets());
            case 4:
                secondaryTargets.add(getCurrentTargets().remove(choice));
                Utils.logWeapon("This is currentTargets: ");
                getCurrentTargets().forEach(target -> Utils.logWeapon(target.getPlayerName()));
                if (getCurrentTargets().isEmpty()) {
                    terminateSecondaryFire();
                    return null;
                }
                return getTargetPlayersQnO(getCurrentTargets());
            case 5:
                secondaryTargets.add(getCurrentTargets().remove(choice));
                secondaryFire();
                return null;
            default:
                throw new IllegalStateException("Reached an illegal state.");
        }
    }

    @Override
    protected void primaryFire() {
        dealDamageAndConclude(getStandardDamagesAndMarks(), getTarget());
    }

    @Override
    public void secondaryFire() {
        dealDamageAndConclude(getSecondaryDamagesAndMarks(), secondaryTargets);
    }

    @Override
    public List<Player> getPrimaryTargets() {
        return getGameMap().getVisiblePlayers(getOwner());
    }

    @Override
    public List<Player> getSecondaryTargets() {
        return getPrimaryTargets();
    }

    /**
     * Instantly fires the weapon. Called if there are no more target to choose from in secondary fire mode.
     */
    private void terminateSecondaryFire() {
        secondaryFire();
    }


    @Override
    public void reset() {
        super.reset();
        secondaryTargets = new ArrayList<>();
    }


}