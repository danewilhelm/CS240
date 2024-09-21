package chess;
import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    protected Collection<ChessMove> calculateQueenMoves() {
        Collection<ChessMove> allPossibleMoves = new ArrayList<>();
        allPossibleMoves.addAll(exploreAllStraights());
        allPossibleMoves.addAll(exploreAllDiagonals());
        return allPossibleMoves;
    }
}
