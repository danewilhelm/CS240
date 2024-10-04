package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * CS240 Interface Method (name cannot be changed)
     *
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * CS240 Interface Method (name cannot be changed)
     *
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     * CS240 Interface Method (name cannot be changed)
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    //===============================Override Methods===========================================

    @Override
    public String toString() {
//        return "ChessMove{" +
//                "startPosition=" + startPosition +
//                ", endPosition= " + endPosition +
//                ", promotionPiece=" + promotionPiece +
//                '}';
        return endPosition.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ChessMove)) {
            return false;
        }

        // initializing variables
        ChessMove otherMove = (ChessMove) object;
        boolean sameStart = this.startPosition.equals(otherMove.getStartPosition());
        boolean sameEnd = this.endPosition.equals(otherMove.getEndPosition());
        boolean samePromotion;

        // NOTE: .equals() does not work with null
        // if both pieces are null, they are equal
        if (this.promotionPiece == null && otherMove.getPromotionPiece() == null) {
            samePromotion = true;
        }

        // if only one piece is null, they are not equal
        else if (this.promotionPiece == null || otherMove.getPromotionPiece() == null) {
            samePromotion = false;
        }

        // if neither piece is null, the pieces must be compared
        else {
            samePromotion = this.promotionPiece.equals(otherMove.getPromotionPiece());
        }

        // combined boolean statement
        return sameStart && sameEnd && samePromotion;
    }


    @Override
    public int hashCode() {
        int result = 42 * (startPosition.hashCode() + endPosition.hashCode());
        if (promotionPiece != null) {
            result *= promotionPiece.hashCode();
        }
        return result;
    }
}
