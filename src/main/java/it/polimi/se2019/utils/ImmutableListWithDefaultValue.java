package it.polimi.se2019.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a custom created class. Create an instance of this with "new ImmutableListWithDefaultValue(new int[]{1,2,3});".
 * Once instantiated, the list cannot be modified.
 * @author Marchingegno
 * @author Desno365
 */
public class ImmutableListWithDefaultValue<E> {
	private final int size;
	private final E defaultValue;
	private ArrayList<E> elements;


	/**
	 * Constructs an immutable list containing the elements of the specified array and with the specified default value.
	 * @param elements the array whose elements are to be placed into this list.
	 * @param defaultValue the desired default value.
	 */
	public ImmutableListWithDefaultValue(E[] elements, E defaultValue) {
		this.elements = new ArrayList<>(Arrays.asList(elements));
		this.defaultValue = defaultValue;
		size = elements.length;
	}


	/**
	 * This method returns the value present at the specified index or the default value if the value of the index wasn't specified.
	 * @param index index of the element to return.
	 * @return the value present at the specified index or the default value.
	 */
	public E get(int index){
		if(index >= elements.size())
			return defaultValue;
		return elements.get(index);
	}

	/**
	 * This method returns the default value of the list.
	 * @return the default value of the list.
	 */
	public E getDefaultValue(){
		return defaultValue;
	}

	/**
	 * Returns the size of the list.
	 * @return the size of the list.
	 */
	public int getSize(){
		return size;
	}




}
