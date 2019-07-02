package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.PlayerRepPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Message used to report the end of the game and the winners.
 */
public class EndGameMessage extends Message {

    private final ArrayList<PlayerRepPosition> finalPlayersInfo;


    /**
     * Constructs a message.
     *
     * @param finalPlayersInfo a list containing the final ranking of the players.
     * @param messageSubtype   the messageSubtype of the message.
     */
    public EndGameMessage(List<PlayerRepPosition> finalPlayersInfo, MessageSubtype messageSubtype) {
        super(MessageType.END_GAME, messageSubtype);
        this.finalPlayersInfo = new ArrayList<>(finalPlayersInfo);
    }


    /**
     * Returns the list containing the final ranking of the players.
     *
     * @return the list containing the final ranking of the players.
     */
    public List<PlayerRepPosition> getFinalPlayersInfo() {
        return finalPlayersInfo;
    }
}
