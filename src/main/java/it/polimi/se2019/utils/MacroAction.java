package it.polimi.se2019.utils;

import it.polimi.se2019.network.message.MessageType;

import java.util.ArrayList;
import java.util.Optional;

/**
 * This class contains all the information to execute an action, and it does so by splitting it into atomic actions.
 * @author Marchingegno
 */
public class MacroAction {

	private final String name;
	private final int numOfMovements;
	private final boolean grab;
	private final boolean reload;
	private final boolean shoot;
	private boolean moved;
	private boolean grabbed;
	private boolean reloaded;
	private boolean shot;

	public MacroAction(int numOfMovements, boolean grab, boolean reload, boolean shoot, String name) {
		this.numOfMovements = numOfMovements;
		this.grab = grab;
		this.reload = reload;
		this.shoot = shoot;
		this.name = name;
	}



	public int getNumOfMovements() {
		return numOfMovements;
	}

	public boolean isGrab() {
		return grab;
	}

	public boolean isReload() {
		return reload;
	}

	public boolean isShoot() {
		return shoot;
	}


	public void execute() {
	}


	public void move() {
	}

	public void grab() {
	}

	public void reload() {
	}

	public void shoot() {
	}



	@Override
	public String toString() {
		return  name + "{" +
				"numOfMovements=" + numOfMovements +
				", grab=" + grab +
				", reload=" + reload +
				", shoot=" + shoot +
				'}';
	}

	public String printAction() {
		StringBuilder myBuilder = new StringBuilder();

		for (int i = 0; i < this.numOfMovements; i++) {
			myBuilder.append(">");
		}

		if(isGrab()){
			myBuilder.append("G");
		}

		if (isReload()){
			myBuilder.append("R");
		}

		if (isShoot()){
			myBuilder.append("S");
		}

		return myBuilder.toString();
	}
}