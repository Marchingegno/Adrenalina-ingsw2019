package it.polimi.se2019.Model.Player;

import java.util.*;

/**
 * This class implements the State Pattern
 */
public abstract class Status {

	/**
	 * Default constructor
	 */
	public Status() {
	}

	/**
	 *
	 */
	private Player player;

	/**
	 *
	 */
	public abstract void getAvailableActions();

	/**
	 *
	 */
	public abstract void runAround();

	/**
	 *
	 */
	public abstract void grabStuff();

	/**
	 *
	 */
	public abstract void shootPeople();

}