package it.polimi.se2019.network.message;

/**
 * Message used to exchange information with just one integer.
 */
public class IntMessage extends Message {

    private int content;


    /**
     * Constructs a message.
     *
     * @param content        the integer to set as data.
     * @param messageType    the messageType of the message.
     * @param messageSubtype the messageSubtype of the message.
     */
    public IntMessage(int content, MessageType messageType, MessageSubtype messageSubtype) {
        super(messageType, messageSubtype);
        this.content = content;
    }


    /**
     * Returns the integer contained in the message.
     *
     * @return the integer contained in the message.
     */
    public int getContent() {
        return content;
    }
}
