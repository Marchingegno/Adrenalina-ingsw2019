package it.polimi.se2019.controller;

import it.polimi.se2019.model.player.Status;

import java.util.ArrayList;
import java.util.Set;

public class MacroAction {

	public int numOfMovements;
	public boolean grab;
	public boolean reload;
	public boolean shoot;
	private Set<Status> possibleActions;


	public void MacroAction(int numOfMovements, boolean grab, boolean reload, boolean shoot) {
	}


	public void execute() {
	}

	public int getNumOfMovements() {
		return 0;
	}

	public boolean getGrab() {
		return false;
	}

	public boolean getReload() {
		return false;
	}

	public boolean getShoot() {
		return false;
	}

	public void move(int row, int column) {
	}

	public void grab(int row, int column) {
	}

	public void reload(int indexOfWeapon) {
	}

	public void shoot(int indexOfWeapon, ArrayList<String> playersToShoot) {
	}

}