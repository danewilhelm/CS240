package chess;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

     protected Collection<ChessMove> calculateBishopMoves() {
        return exploreAllDiagonals();
    }
}
