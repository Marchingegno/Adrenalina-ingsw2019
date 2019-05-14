package it.polimi.se2019.network.message;

import java.util.ArrayList;
import java.util.List;

public class AskOptionsMessage extends Message {

	private String question;
	private ArrayList<String> options;

	public AskOptionsMessage(String question, List<String> options, MessageType messageType){
		super(messageType, MessageSubtype.ANSWER);
		this.question = question;
		this.options = new ArrayList<>(options);
	}

	public List<String> getOptions() {
		return options;
	}

	public String getQuestion() {
		return question;
	}
}
