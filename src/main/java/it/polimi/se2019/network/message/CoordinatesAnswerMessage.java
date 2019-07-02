package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

/**
 * Message used as answer with a Coordinates data.
 */
public class CoordinatesAnswerMessage extends Message {

    private final Coordinates singleCoordinates;


    /**
     * Constructs a message.
     *
     * @param coordinates the Coordinates data to send as answer.
     * @param messageType the messageType of the message.
     */
    public CoordinatesAnswerMessage(Coordinates coordinates, MessageType messageType) {
        super(messageType, MessageSubtype.ANSWER);
        this.singleCoordinates = coordinates;
    }


    /**
     * Returns the Coordinates sent as answer.
     *
     * @return the Coordinates sent as answer.
     */
    public Coordinates getSingleCoordinates() {
        return singleCoordinates;
    }
}
