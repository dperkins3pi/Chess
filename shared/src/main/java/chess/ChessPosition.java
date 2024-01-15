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
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override  // Positions are equal if they are the same row and column
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj == this) {
            return true;
        }
        else if (obj.getClass() != this.getClass()) {
            return false;
        }
        ChessPosition o = (ChessPosition) obj; //to allow access of attribtube
        return (this.getRow() == o.getRow() && this.getColumn() == o.getColumn());
    }

    @Override
    public int hashCode() {
        int hash = this.getRow();
        hash = hash * 13 + this.getColumn();
        return hash;
    }
}
