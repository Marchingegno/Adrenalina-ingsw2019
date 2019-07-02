package it.polimi.se2019.network.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Message used to send the current players waiting in the wating room.
 */
public class WaitingPlayersMessage extends Message {

    private final ArrayList<String> waitingPlayersNames;


    /**
     * Constructs a message.
     *
     * @param waitingPlayersNames a list of the names of players waiting in the waiting room.
     */
    public WaitingPlayersMessage(List<String> waitingPlayersNames) {
        super(MessageType.WAITING_PLAYERS, MessageSubtype.INFO);
        this.waitingPlayersNames = new ArrayList<>(waitingPlayersNames);
    }


    /**
     * Returns a list of names of the players waiting in the waiting room.
     *
     * @return a list of names of the players waiting in the waiting room.
     */
    public List<String> getWaitingPlayersNames() {
        return waitingPlayersNames;
    }

}
