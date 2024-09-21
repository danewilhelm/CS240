package chess;
import java.util.Collection;

public class PieceMovesCalculator {

    public PieceMovesCalculator() {
    }

    static public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        BishopMovesCalculator bishopCalculator = new BishopMovesCalculator(board, myPosition);
        return bishopCalculator.calculateBishopMoves();

//        ChessPiece CurChessPiece = board.getPiece(myPosition);
//
//        var pieceType = CurChessPiece.getPieceType();
//        switch (pieceType) {
//            case KING:
//            case QUEEN:
//            case BISHOP:
//            case KNIGHT:
//            case ROOK:
//            case PAWN:
//        }
//        return new ArrayList<>();

    }

}
