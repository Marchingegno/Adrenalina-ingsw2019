package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.QuestionContainer;

/**
 * Message used to display a question and possible answer to a player.
 */
public class AskOptionsMessage extends Message {

	private final QuestionContainer questionContainer;


	/**
	 * Constructs a message.
	 *
	 * @param questionContainer the QuestionContainer to associate to the message.
	 * @param messageType       the messageType of the message.
	 * @param messageSubtype    the messageSubtype of the message.
	 */
	public AskOptionsMessage(QuestionContainer questionContainer, MessageType messageType, MessageSubtype messageSubtype) {
		super(messageType, messageSubtype);
		this.questionContainer = questionContainer;
	}


	/**
	 * Returns the QuestionContainer associated with the message.
	 *
	 * @return the QuestionContainer associated with the message.
	 */
	public QuestionContainer getQuestionContainer() {
		return questionContainer;
	}
}
