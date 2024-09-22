package chess;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    protected Collection<ChessMove> calculateKnightMoves() {

        int[][] relativeKnightCoordinates = new int[][]{
                {2, 1}, {1, 2},
                {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2},
                {1, -2}, {2, -1}
        };
        return exploreRelativePositions(relativeKnightCoordinates);
    }
}
