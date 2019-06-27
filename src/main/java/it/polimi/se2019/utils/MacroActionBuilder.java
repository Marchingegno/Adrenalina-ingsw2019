package it.polimi.se2019.utils;

import java.security.InvalidParameterException;

/**
 * The job of this class is to generate a new MacroAction.
 * @author Marchingegno
 */
public class MacroActionBuilder {

	private String name;
	private int moveDistance;
	private boolean grabActive;
	private boolean reloadActive;
	private boolean shootActive;


	public MacroActionBuilder() {
		moveDistance = 0;
		grabActive = false;
		reloadActive = false;
		shootActive = false;
		//TODO: Implement name.
		name = "placeHolder";
	}


	public void setMovementDistance(int distance) {
		if (distance < 0) throw new InvalidParameterException("Distance cannot be negative!");
		this.moveDistance = distance;
	}

	public void setGrabAction(boolean active) {
		this.grabActive = active;
	}

	public void setReloadAction(boolean active) {
		this.reloadActive = active;
	}

	public void setShootAction(boolean active) {
		this.shootActive = active;
	}

	public void setName(String name) {
		this.name = name;
	}



	public MacroAction build() {
		Utils.logInfo("MacroActionBuilder -> build(): Building " + name + ": moves = " + moveDistance + (grabActive ? " grab" : "") + (reloadActive ? " reload" : "") + (shootActive ? " shoot" : ""));
		return new MacroAction(moveDistance, grabActive, reloadActive, shootActive, name);
	}

}