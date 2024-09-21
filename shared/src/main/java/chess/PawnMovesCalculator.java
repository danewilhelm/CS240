package chess;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    protected Collection<ChessMove> calculatePawnMoves() {

        // Possibility factors
            // TeamColor
            // Initial Move
            // Capturing pieces
            // Promotion


        int[][] relativePawnCoordinates = new int[][]{
                {, }, {, },
                {, }, {, },
        };
        return exploreRelativePositions(relativePawnCoordinates, null);
    }
}
