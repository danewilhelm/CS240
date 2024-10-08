package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * CS240 Interface Method (name cannot be changed)
     *
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * CS240 Interface Method (name cannot be changed)
     *
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    public ChessPosition createRelativePosition(int rowRelative, int colRelative) {
        return new ChessPosition(row + rowRelative, col + colRelative);
    }

    //===============================Override Methods===========================================

    @Override
    public String toString() {
        return String.format("{%d, %d}", row, col);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ChessPosition)) {
            return false;
        }

        ChessPosition otherPosition = (ChessPosition) object;
        return this.row == otherPosition.getRow() && this.col == otherPosition.getColumn();
    }

    @Override
    public int hashCode() {
        return (42 * row) + (13 * col);
    }
}
