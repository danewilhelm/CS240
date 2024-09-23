package chess;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    protected Collection<ChessMove> calculatePawnMoves() {
        // Explore possible squares to move to
        Collection<ChessPosition> allPossibleEndPositions = new ArrayList<>();
        allPossibleEndPositions.addAll(exploreForwardPositions());
        allPossibleEndPositions.addAll(exploreCapturePositions());

        // Preps the list of possible pieces to promote to (it has 4 options)
        ArrayList<ChessPiece.PieceType> promotionOptions = new ArrayList<>();
        promotionOptions.add(ChessPiece.PieceType.QUEEN);
        promotionOptions.add(ChessPiece.PieceType.KNIGHT);
        promotionOptions.add(ChessPiece.PieceType.BISHOP);
        promotionOptions.add(ChessPiece.PieceType.ROOK);

        // create ChessMoves based on if it will promote or not
        Collection<ChessMove> allPossibleMoves = new ArrayList<>();
        for (ChessPosition curEndPosition: allPossibleEndPositions) {

            // if the pawn will promote, each promotion choice is a different move
            if (willPromote()) {
                for (ChessPiece.PieceType promoteOption: promotionOptions) {
                    allPossibleMoves.add(new ChessMove(myPosition, curEndPosition, promoteOption));
                }
            }

            // else, it is a normal move without promote choices
            else {
                allPossibleMoves.add(new ChessMove(myPosition, curEndPosition, null));
            }
        }
        return allPossibleMoves;
    }

    // ===============================Helper Methods===============================================
    // -------------------------------Explore Pawn Positions---------------------------------------

    /**
     * Calculates possible endPositions for a pawn
     * Pawns can move forward one square if it is empty
     * Pawns can move two squares forward if both squares are empty and it's the pawn's first move
     *
     * @return Possible forward positions to move to
     */
    private Collection<ChessPosition> exploreForwardPositions() {
        Collection<ChessPosition> possibleForwardPositions = new ArrayList<>();

        int colorDirection = findColorDirection();
        ChessPosition normalForward = myPosition.createRelativePosition(colorDirection, 0);
        ChessPosition initialForward = myPosition.createRelativePosition(colorDirection * 2, 0);
        if (! isOutOfBounds(normalForward) && isOpenPosition(normalForward)) {
            possibleForwardPositions.add(normalForward);
            // you can only make an initial move if the normal move position is open
            if (isInitialMove() && isOpenPosition(initialForward)) {
                possibleForwardPositions.add(initialForward);
            }
        }
        return possibleForwardPositions;
    }

    /**
     * Calculates possible capture positions for a pawn
     * A pawn can move one square diagonally forward (left or right) if it's occupied by an enemy
     *
     * @return Possible capture positions
     */
    private Collection<ChessPosition> exploreCapturePositions() {
        int colorDirection = findColorDirection();
        // create the end positions
        ChessPosition captureBoardRight = myPosition.createRelativePosition(colorDirection, 1);
        ChessPosition captureBoardLeft = myPosition.createRelativePosition(colorDirection, -1);

        // determine if the pawn movement conditions are met
        Collection<ChessPosition> possibleCapturePositions = new ArrayList<>();
        if (willCapture(captureBoardRight)) {
            possibleCapturePositions.add(captureBoardRight);
        }
        if (willCapture(captureBoardLeft)) {
            possibleCapturePositions.add(captureBoardLeft);
        }
        return possibleCapturePositions;
    }

    // -----------------------Distance and Direction Methods----------------------------

    /**
     * Finds the direction the pawn can move based on color
     * Black pawns travel downwards (towards white)
     * White pawns travel upwards (towards black)
     *
     * @return an integer representing the direction the pawn can move
     */
    private int findColorDirection() {
        var pawnColor = board.getPiece(myPosition).getTeamColor();
        int colorDirection;
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            colorDirection = 1;
        } else if (pawnColor == ChessGame.TeamColor.BLACK) {
            colorDirection = -1;
        } else {
            System.out.println("ERROR: piece is neither black or white");
            colorDirection = 0;
        }
        return colorDirection;
    }

    /**
     * Finds the distance of rows a pawn is from it's promotion row
     * A pawn's promotion row is on the opposite end of the board
     * For a white pawn, the promotion row is at the top
     * For a black pawn, the promotion row is at the bottom
     *
     * @return an integer representing a the distance a pawn is promoting
     */
    private int findDistancefromPromotion() {
        var pawnColor = board.getPiece(myPosition).getTeamColor();
        int distancefromPromotion = -1;
        switch(pawnColor) {
            case WHITE:
                distancefromPromotion = 8 - myPosition.getRow();
                break;
            case BLACK:
                distancefromPromotion = myPosition.getRow() - 1;
                break;
        }
        return distancefromPromotion;
    }

    // ----------------------------------Boolean Methods-------------------------------------

    /**
     * Checks if the pawn is in the starting pawn row
     *
     * @return a boolean
     */
    private boolean isInitialMove() {
        return findDistancefromPromotion() == 6;
    }

    /**
     * Checks if an endPosition will result in a capture
     * It will return false if the position is empty
     *
     * @param endPosition
     * @return a boolean
     */
    boolean willCapture(ChessPosition endPosition) {
        return ! isOutOfBounds(endPosition) && ! isOpenPosition(endPosition) && isEnemyPosition(endPosition);
    }

    /**
     * Checks if the pawn is one row away from promoting
     *
     * @return a boolean
     */
    private boolean willPromote() {
        return findDistancefromPromotion() == 1;
    }
}
