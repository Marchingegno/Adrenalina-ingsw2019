package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

public class AskOptionsMessage extends Message {

	private QuestionContainer questionContainer;

	public AskOptionsMessage(QuestionContainer questionContainer, MessageType messageType, MessageSubtype messageSubtype){
		super(messageType, messageSubtype);
		this.questionContainer = questionContainer;
	}

	public List<String> getOptions() {
		return questionContainer.getOptions();
	}

	public List<Coordinates> getCoordinates(){
		return questionContainer.getCoordinates();
	}

	public String getQuestion() {
		return questionContainer.getQuestion();
	}

	public boolean isAskCoordinates(){
		return questionContainer.isAskCoordinates();
	}

	public boolean isAskString(){
		return questionContainer.isAskString();
	}
}
