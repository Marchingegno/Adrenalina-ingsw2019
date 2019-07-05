package it.polimi.se2019.utils;

import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents position in a leaderboard.
 *
 * @author Marchingegno
 */
public class LeaderboardSlot {
    private final List<Player> playersInThisPosition;


    public LeaderboardSlot() {
        playersInThisPosition = new ArrayList<>();
    }


    /**
     * Returns all the players in this slot.
     *
     * @return the players in this slot.
     */
    public List<Player> getPlayersInThisPosition() {
        return playersInThisPosition;
    }

    /**
     * Returns the single player in this slot, if it is not a tie.
     * @return the player.
     */
    public Player getPlayer() {
        if (isATie()) throw new IllegalStateException("This slot is a tie, therefore getPlayer should not be called.");
        return playersInThisPosition.get(0);
    }

    /**
     * Add this player to this slot.
     * @param player the player to be added.
     */
    public void addInPosition(Player player) {
        playersInThisPosition.add(player);
    }

    /**
     * Checks if there is a tie on this slot.
     * @return
     */
    public boolean isATie() {
        return playersInThisPosition.size() > 1;
    }


}
