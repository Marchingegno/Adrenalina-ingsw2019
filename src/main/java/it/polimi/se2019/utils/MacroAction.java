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
		refill();
	}



	public int getNumOfMovements() {
		return numOfMovements;
	}

	public boolean isMove(){
		if(numOfMovements == 0)
			return false;
		return true;
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


	public ActionType execute() {
		if(isMove() && !moved){
			moved = true;
			return ActionType.MOVE;
		}
		else if(isGrab() && !grabbed){
			grabbed = true;
			return ActionType.GRAB;
		}
		else if(isReload() && !reloaded){
			reloaded = true;
			return ActionType.RELOAD;
		}
		else if(isShoot() && !shot){
			shot = true;
			return ActionType.SHOOT;
		}
		refill();
		return ActionType.END;
	}

	private void refill(){
		if(isMove())
			moved = false;
		if(isGrab())
			grabbed = false;
		if(isReload())
			reloaded = false;
		if(isShoot())
			shot = false;
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