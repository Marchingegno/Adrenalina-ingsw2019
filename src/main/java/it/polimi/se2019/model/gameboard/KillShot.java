package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.player.Player;

/**
 * Class that represent the marks on the kill shot track.
 *
 * @author Desno365
 */
public class KillShot implements Representable {

    private Player player;
    private boolean overkill;


    KillShot(Player shootingPlayer, boolean overkill) {
        player = shootingPlayer;
        this.overkill = overkill;
    }

    /**
     * Returns the points associated with this KillShot.
     * 2 points if is an overkill, otherwise 1 point.
     *
     * @return the points associated with this KillShot.
     */
    public int getPoints() {
        return isOverkill() ? 2 : 1;
    }

    /**
     * Returns the Player who has done the kill.
     *
     * @return the Player who has done the kill.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns true if and only if the kill is also an overkill.
     *
     * @return true if and only if the kill is also an overkill.
     */
    public boolean isOverkill() {
        return overkill;
    }

    @Override
    public Representation getRep() {
        return new KillShotRep(player, overkill);
    }
}
