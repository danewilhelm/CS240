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

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public String toString() {
        return "ChessMove{" +
//                "startPosition=" + startPosition +
                ", endPosition= " + endPosition +
                ", promotionPiece=" + promotionPiece +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ChessMove)) {
            return false;
        }

        ChessMove otherMove = (ChessMove) object;

        boolean sameStart = this.startPosition.equals(otherMove.getStartPosition());
        boolean sameEnd = this.endPosition.equals(otherMove.getEndPosition());
        boolean samePromotion;

        // if null exists, we cannot use .equals()
        if (this.promotionPiece == null || otherMove.getPromotionPiece() == null) {
            // if AT LEAST ONE is null...
            if (this.promotionPiece == null && otherMove.getPromotionPiece() == null) {
                // if BOTH are null, they are equal
                samePromotion = true;
            } else {
                // if ONE is null, they are not equal
                samePromotion = false;
            }
        } else {
            // if NEITHER are null, the pieces must be compared
            samePromotion = this.promotionPiece.equals(otherMove.getPromotionPiece());
        }

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
