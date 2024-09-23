package chess;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    protected Collection<ChessMove> calculateKingMoves() {
    // Represents the 8 squares a king can move to
        int[][] relativeKingCoordinates = new int[][]{
                {1, 0}, {1, 1},
                {0, 1}, {-1, 1},
                {-1, 0}, {-1, -1},
                {0, -1}, {1, -1}
        };

        return exploreRelativePositions(relativeKingCoordinates);
    }
}
