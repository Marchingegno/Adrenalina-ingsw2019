package it.polimi.se2019.network.message;

/**
 * Message used for player nicknames.
 */
public class NicknameMessage extends StringMessage {

    /**
     * Constructs a message.
     *
     * @param content        the nickname.
     * @param messageSubtype the messageSubtype of the message.
     */
    public NicknameMessage(String content, MessageSubtype messageSubtype) {
        super(content, MessageType.NICKNAME, messageSubtype);
    }
}
