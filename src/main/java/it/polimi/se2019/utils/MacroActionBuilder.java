package it.polimi.se2019.utils;

import java.security.InvalidParameterException;

/**
 * The job of this class is to generate a new MacroAction.
 * @author Marchingegno
 */
public class MacroActionBuilder {

	private int moveDistance;
	private boolean grabActive;
	private boolean reloadActive;
	private boolean shootActive;


	public MacroActionBuilder() {
		moveDistance = 0;
		grabActive = false;
		reloadActive = false;
		shootActive = false;
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

	public MacroAction build() {
		isCorrect();
		return new MacroAction(moveDistance, grabActive, reloadActive, shootActive);
	}

	private void isCorrect(){
		return; //TODO: Implement isCorrect. It's not clear what this method should do.
	}

}