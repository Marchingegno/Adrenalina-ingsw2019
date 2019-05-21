package it.polimi.se2019.utils;

import it.polimi.se2019.model.gamemap.Coordinates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionContainer implements Serializable {

	private final String question;
	private final boolean askString;
	private final boolean askCoordinates;

	private ArrayList<String> options;
	private ArrayList<Coordinates> coordinates;


	private QuestionContainer(String question, boolean askString, boolean askCoordinates) {
		this.question = question;
		this.askString = askString;
		this.askCoordinates = askCoordinates;
	}


	public static QuestionContainer createStringQuestionContainer(String question, List<String> options) {
		QuestionContainer questionContainer = new QuestionContainer(question, true, false);
		questionContainer.options = new ArrayList<>(options);
		return questionContainer;
	}

	public static QuestionContainer createCoordinatesQuestionContainer(String question, List<Coordinates> coordinates) {
		QuestionContainer questionContainer = new QuestionContainer(question, false, true);
		questionContainer.coordinates = new ArrayList<>(coordinates);
		return questionContainer;
	}


	public boolean isAskString() {
		return askString;
	}

	public boolean isAskCoordinates() {
		return askCoordinates;
	}

	public String getQuestion() {
		return question;
	}

	public List<String> getOptions() {
		return options;
	}

	public List<Coordinates> getCoordinates() {
		return coordinates;
	}
}
