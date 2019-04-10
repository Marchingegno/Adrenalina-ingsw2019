package it.polimi.se2019.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a custom created class. Create an instance of this with "new ImmutableList(new int[]{1,2,3});".
 * Once instantiated, the array cannot be modified.
 * @author Marchingegno
 */
public class ImmutableList {
	private final int size;
	private ArrayList<Integer> elements;


	public ImmutableList(int[] elements) {
		this.elements = new ArrayList(Arrays.asList(elements));
		size = elements.length;
	}


	/**
	 * This method returns the value present at the specified index.
	 * @param index
	 * @return the value present at the specified index.
	 */
	public int get(int index){
		rangeCheck(index);

		return elements.get(index);
	}

	/**
	 * This method returns the last value of the array.
	 * @return the last value of the array.
	 */
	public int getLast(){
		return elements.get(size - 1);
	}

	/**
	 * This method checks if the index is out of bounds.
	 * @param index
	 * @throws IndexOutOfBoundsException
	 */
	private void rangeCheck(int index) throws IndexOutOfBoundsException {
		if (index >= size)
			throw new IndexOutOfBoundsException("Index out of bounds!");
	}

	public int getSize(){
		return size;
	}




}
