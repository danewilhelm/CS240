package chess;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }

    protected Collection<ChessMove> calculatePawnMoves() {

        // Possibility factors
            // TeamColor COMPLETE
            // Initial Move COMPLETE
            // Capturing pieces COMPLETE
            // Promotion


        Collection<ChessPosition> allPossibleEndPositions = new ArrayList<>();
        allPossibleEndPositions.addAll(exploreForwardPositions());
        allPossibleEndPositions.addAll(exploreCapturePositions());

        ArrayList<ChessPiece.PieceType> promotionOptions = new ArrayList<>();
        promotionOptions.add(ChessPiece.PieceType.QUEEN);
        promotionOptions.add(ChessPiece.PieceType.KNIGHT);
        promotionOptions.add(ChessPiece.PieceType.BISHOP);
        promotionOptions.add(ChessPiece.PieceType.ROOK);

        Collection<ChessMove> allPossibleMoves = new ArrayList<>();
        for (ChessPosition curEndPosition: allPossibleEndPositions) {
            if (willPromote()) {
                for (ChessPiece.PieceType promoteOption: promotionOptions) {
                    allPossibleMoves.add(new ChessMove(myPosition, curEndPosition, promoteOption));
                }
            } else {
                allPossibleMoves.add(new ChessMove(myPosition, curEndPosition, null));
            }
        }
        return allPossibleMoves;
    }

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

    private boolean isInitialMove() {
        return findDistancefromPromotion() == 6;
    }

    private Collection<ChessPosition> exploreCapturePositions() {
        int colorDirection = findColorDirection();
        // create the end positions
        ChessPosition captureBoardRight = myPosition.createRelativePosition(colorDirection, 1);
        ChessPosition captureBoardLeft = myPosition.createRelativePosition(colorDirection, -1);

        // determine if the pawn movement conditions are met
        Collection<ChessPosition> possibleCapturePositions = new ArrayList<>();
        if (isCapturableAsPawn(captureBoardRight)) {
            possibleCapturePositions.add(captureBoardRight);
        }
        if (isCapturableAsPawn(captureBoardLeft)) {
            possibleCapturePositions.add(captureBoardLeft);
        }
        return possibleCapturePositions;
    }

    boolean isCapturableAsPawn(ChessPosition endPosition) {
        return ! isOutOfBounds(endPosition) && ! isOpenPosition(endPosition) && isEnemyPosition(endPosition);
    }

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






    private boolean willPromote() {
        return findDistancefromPromotion() == 1;
    }
}
