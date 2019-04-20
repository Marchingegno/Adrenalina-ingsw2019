package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.utils.CardinalDirection;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinatesTest {

	@Test (expected = NegativeCoordinatesException.class)
	public void Coordinates_negativeRowIndex_throwsNegativeCoordinatesException() {
		new Coordinates(1,-1);
	}

	@Test (expected = NegativeCoordinatesException.class)
	public void Coordinates_negativeColumnIndex_throwsNegativeCoordinatesException() {
		new Coordinates(-1,0);
	}

	@Test
	public void Coordinates_correctInput_correctOutput() {
		new Coordinates(1,2);
	}

	@Test
	public void getRow_correctInput_correctOutput() {
		Coordinates coordinates = new Coordinates( 1 , 2);
		assertEquals(1, coordinates.getRow());
	}

	@Test
	public void getColumn_correctInput_correctOutput() {
		Coordinates coordinates = new Coordinates( 1 , 5);
		assertEquals(5, coordinates.getColumn());
	}

	@Test
	public void distance_correctInput_correctOutput() {
		Coordinates coordinates1 = new Coordinates( 0, 0);
		Coordinates coordinates2 = new Coordinates( 3 , 4);
		Coordinates coordinates3 = new Coordinates( 2 , 1);
		Coordinates coordinates4 = new Coordinates( 3, 0);
		assertEquals(7, coordinates1.distance(coordinates2));
		assertEquals(0, coordinates4.distance(coordinates4));
		assertEquals(4, coordinates2.distance(coordinates3));
		assertEquals(coordinates2.distance(coordinates1), coordinates1.distance(coordinates2));
	}

	@Test
	public void getDirectionCoordinates_correctInput_correctOutput() {
		Coordinates coordinates = new Coordinates( 2 , 1);
		assertEquals(new Coordinates(1,1), Coordinates.getDirectionCoordinates(coordinates, CardinalDirection.UP));
		assertEquals(new Coordinates(2,2), Coordinates.getDirectionCoordinates(coordinates, CardinalDirection.RIGHT));
		assertEquals(new Coordinates(3,1), Coordinates.getDirectionCoordinates(coordinates, CardinalDirection.DOWN));
		assertEquals(new Coordinates(2,0), Coordinates.getDirectionCoordinates(coordinates, CardinalDirection.LEFT));
	}

	@Test (expected = NegativeCoordinatesException.class)
	public void getDirectionCoordinates_coordinatesOnTheEdgeUP_throwsNegativeCoordinatesException() {
		Coordinates coordinates = new Coordinates( 0, 0);
		Coordinates.getDirectionCoordinates(coordinates, CardinalDirection.UP);
	}

	@Test (expected = NegativeCoordinatesException.class)
	public void getDirectionCoordinates_coordinatesOnTheEdgeLEFT_throwsNegativeCoordinatesException() {
		Coordinates coordinates = new Coordinates( 0, 0);
		Coordinates.getDirectionCoordinates(coordinates, CardinalDirection.LEFT);
	}

	@Test
	public void toString_correctInput_correctOutput() {
		assertEquals("(2,1)", new Coordinates( 2 , 1).toString());
	}

	@Test
	public void equals_correctInput_correctOutput() {
		assertEquals(new Coordinates( 2 , 1), new Coordinates( 2 , 1));
	}

	@Test
	public void equals_notCoordinate_correctOutput() {
		assertFalse(new Coordinates( 2 , 1).equals(new Object()));
	}

	@Test
	public void equals_differentColumn_correctOutput() {
		assertFalse(new Coordinates( 2 , 1).equals(new Coordinates(2,2)));
	}

	@Test
	public void equals_differentRow_correctOutput() {
		assertFalse(new Coordinates( 2 , 1).equals(new Coordinates(1,1)));
	}

	@Test
	public void equals_dirrefentCoordinate_correctOutput() {
		assertFalse(new Coordinates( 2 , 1).equals(new Coordinates(5,5)));
	}

	@Test
	public void hashCode_correctInput_correctOutput() {
		assertEquals(320 , new Coordinates( 7 , 3).hashCode());
	}
}
