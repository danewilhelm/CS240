package chess;
import java.util.Collection;
import java.util.ArrayList;


public class PieceMovesCalculator {
    // instance varaibles

    protected ChessBoard board;
    protected ChessPosition myPosition;

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    static public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {


        ChessPiece CurChessPiece = board.getPiece(myPosition);
        var pieceType = CurChessPiece.getPieceType();

        switch (pieceType) {
            case KING:
            case QUEEN:
            case BISHOP:
                BishopMovesCalculator bishopCalculator = new BishopMovesCalculator(board, myPosition);
                return bishopCalculator.calculateBishopMoves();
            case KNIGHT:
            case ROOK:
            case PAWN:
        }
        return new ArrayList<>();

    }

}
