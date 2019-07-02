package it.polimi.se2019.network.message;

/**
 * Message used to exchange information with just one string.
 */
public class StringMessage extends Message {

    private final String content;


    /**
     * Constructs a message.
     *
     * @param content        the string to set as data.
     * @param messageType    the messageType of the message.
     * @param messageSubtype the messageSubtype of the message.
     */
    StringMessage(String content, MessageType messageType, MessageSubtype messageSubtype) {
        super(messageType, messageSubtype);
        this.content = content;
    }


    /**
     * Returns the string contained in the message.
     *
     * @return the string contained in the message.
     */
    public String getContent() {
        return content;
    }
}
