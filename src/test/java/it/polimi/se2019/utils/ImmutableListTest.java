package it.polimi.se2019.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImmutableListTest {

	private ImmutableList listToTest;

	@Before
	public void setUp() throws Exception {
		listToTest = new ImmutableList(new int[]{8,6,4});
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void get_indexInBounds_correctOutput() {
		int firstElement, secondElement, thirdElement;
		firstElement = listToTest.get(1);
		secondElement = listToTest.get(2);
		thirdElement = listToTest.get(3);
		assertEquals(8, firstElement);
		assertEquals(6, secondElement);
		assertEquals(4, thirdElement);
	}

	@Test (expected = IndexOutOfBoundsException.class)
	public void get_indexOutOfBounds_shouldThrowException() {
		int pad = listToTest.get(listToTest.getSize());



	}

	@Test
	public void getLast_shouldGiveLastElement() {
		int lastElement = listToTest.getLast();
		assertEquals(4, lastElement);
	}


}