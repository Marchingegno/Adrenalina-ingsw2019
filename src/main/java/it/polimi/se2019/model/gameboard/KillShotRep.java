package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Color;

/**
 * A sharable version of the killshot.
 *
 * @author MarcerAndrea
 */
public class KillShotRep implements Representation {

    private String playerName;
    private Color.CharacterColorType playerColor;
    private boolean overkill;
    private String pgName;


    KillShotRep(Player killer, boolean overkill) {
        playerName = killer.getPlayerName();
        playerColor = killer.getPlayerColor();
        pgName = killer.getPgName();
        this.overkill = overkill;
    }

    /**
     * Returns the name of the player how has killed.
     *
     * @return the name of the player how has killed.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the color of the player how has killed.
     *
     * @return the color of the player how has killed.
     */
    public Color.CharacterColorType getPlayerColor() {
        return playerColor;
    }

    /**
     * Returns true if and only if this kill is also an overkill.
     *
     * @return true if and only if this kill is also an overkill.
     */
    public boolean isOverkill() {
        return overkill;
    }

    public String getPgName() {
        return pgName;
    }
}
