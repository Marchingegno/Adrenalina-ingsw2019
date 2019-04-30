package it.polimi.se2019.model;

import java.util.Observable;

public abstract class Representable extends Observable {
	public abstract Representation getRep();
}
