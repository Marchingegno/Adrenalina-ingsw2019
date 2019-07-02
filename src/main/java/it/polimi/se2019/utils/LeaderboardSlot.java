package it.polimi.se2019.utils;

import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardSlot {
    private final List<Player> playersInThisPosition;


    public LeaderboardSlot() {
        playersInThisPosition = new ArrayList<>();
    }


    public List<Player> getPlayersInThisPosition() {
        return playersInThisPosition;
    }

    public Player getPlayer() {
        if (isATie()) throw new IllegalStateException("This slot is a tie, therefore getPlayer should not be called.");
        return playersInThisPosition.get(0);
    }

    public void addInPosition(Player player) {
        playersInThisPosition.add(player);
    }

    public boolean isATie() {
        return playersInThisPosition.size() > 1;
    }


}
