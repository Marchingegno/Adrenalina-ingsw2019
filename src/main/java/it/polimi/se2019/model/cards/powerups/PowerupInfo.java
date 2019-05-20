package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.gamemap.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class PowerupInfo {

	private String question;
	private ArrayList<String> options;
	private boolean askOption = false;

	private ArrayList<Coordinates> coordinates;
	private boolean askCoordinates;


	void setAskOptions(String question, List<String> options) {
		this.question = question;
		this.options = new ArrayList<>(options);
		askOption = true;
	}

	void setAskCoordinates(String question, List<Coordinates> coordinates) {
		this.question = question;
		this.coordinates = new ArrayList<>(coordinates);
		askCoordinates = true;
	}

	public boolean isAskOption() {
		return askOption;
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
