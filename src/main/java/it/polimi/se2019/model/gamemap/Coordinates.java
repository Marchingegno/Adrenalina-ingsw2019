package it.polimi.se2019.model.gamemap;

/**
 * Class with two integers representing the positions in the map matrix
 * @author MsrcerAndrea
 */
public class Coordinates {

	private int row;
	private int column;

	public Coordinates(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * @return row index
	 */
	public int getRow() { return row; }

	/**
	 * @return column index
	 */
	public int getColumn() { return column;	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Coordinates) && (((Coordinates) obj).getRow() == this.row) && (((Coordinates) obj).getColumn() == this.column));
	}
}