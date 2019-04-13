package it.polimi.se2019.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImmutableListWithDefaultValueTest {

	private ImmutableListWithDefaultValue listToTest;

	@Before
	public void setUp() throws Exception {
		listToTest = new ImmutableListWithDefaultValue<>(new Integer[]{8, 6, 4, 2}, 1);
	}

	@Test
	public void get_indexInBounds_correctOutput() {
		assertEquals(8, listToTest.get(0));
		assertEquals(6, listToTest.get(1));
		assertEquals(4, listToTest.get(2));
		assertEquals(2, listToTest.get(listToTest.getSize() - 1));
	}

	@Test
	public void get_indexOutOfBounds_shouldGiveDefaultValue() {
		assertEquals(1, listToTest.get(listToTest.getSize()));
		assertEquals(1, listToTest.get(listToTest.getSize() + 1));
	}

	@Test
	public void getDefaultValue_shouldGiveLastElement() {
		assertEquals(1, listToTest.getDefaultValue());
		assertEquals(1, (int) GameConstants.SCORES.getDefaultValue());
		assertEquals(1, (int) GameConstants.FRENZY_SCORES.getDefaultValue());
	}


}