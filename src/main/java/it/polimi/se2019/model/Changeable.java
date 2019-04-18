package it.polimi.se2019.model;

/**
 * Implemented from all classes that
 */
public interface Changeable {

	/**
	 * Returns true if and only if the class has been changed.
	 * @returntrue if and only if the class has been changed.
	 */
	boolean isChanged();

	/**
	 * If the status is changed it remains changed, if it not changed it change to changed.
	 */
	void change();

	/**
	 * If the status is not changed it remains not changed, if it changed it change to not changed.
	 */
	void reset();

}
