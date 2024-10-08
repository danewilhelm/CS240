package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    // Question: was this meant to be package visible?
    ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     * CS240 Interface Method (name cannot be changed)
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * CS240 Interface Method (name cannot be changed)
     *
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * CS240 Interface Method (name cannot be changed)
     *
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     * CS240 Interface Method (name cannot be changed)
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return PieceMovesCalculator.pieceMoves(board, myPosition);
    }

    //===============================Override Methods===========================================

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ChessPiece)) {
            return false;
        }

        ChessPiece otherPiece = (ChessPiece) object;
        return this.pieceColor == otherPiece.getTeamColor() && this.type == otherPiece.getPieceType();
    }

    @Override
    public int hashCode() {
        return 42 * type.hashCode() + 13 * pieceColor.hashCode();
    }
}
