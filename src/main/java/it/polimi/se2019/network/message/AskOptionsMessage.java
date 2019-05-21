package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.QuestionContainer;

public class AskOptionsMessage extends Message {

	private QuestionContainer questionContainer;

	public AskOptionsMessage(QuestionContainer questionContainer, MessageType messageType, MessageSubtype messageSubtype){
		super(messageType, messageSubtype);
		this.questionContainer = questionContainer;
	}

	public QuestionContainer getQuestionContainer() {
		return questionContainer;
	}
}
