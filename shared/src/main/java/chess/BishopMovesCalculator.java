package chess;
import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {

    // instance variables are inherited

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        super(board, myPosition);
    }



     public Collection<ChessMove> calculateBishopMoves() {
//         System.out.print("starting position: ");
//         System.out.println(myPosition);


        Collection<ChessMove> upRightMoves = exploreDirectionAcrossBoard(1, 1);
        Collection<ChessMove> downRightMoves = exploreDirectionAcrossBoard(-1, 1);
        Collection<ChessMove> downLeftMoves = exploreDirectionAcrossBoard(-1, -1);
        Collection<ChessMove> upLeftMoves = exploreDirectionAcrossBoard(1, -1);

        Collection<ChessMove> allPossibleMoves = new ArrayList<>();
        allPossibleMoves.addAll(upRightMoves);
        allPossibleMoves.addAll(downRightMoves);
        allPossibleMoves.addAll(downLeftMoves);
        allPossibleMoves.addAll(upLeftMoves);

         // DEBUG
//         System.out.println(upRightMoves);
//         System.out.println(downRightMoves);
//         System.out.println(downLeftMoves);
//         System.out.println(upLeftMoves);


        return allPossibleMoves;
    }
    //=================== Helper Functions for calculating possible moves=======================

    protected Collection<ChessMove> exploreDirectionAcrossBoard(int rowRelative, int colRelative) {
        ChessPosition previousPosition = myPosition;
        ChessPosition nextPosition = myPosition.createNewPosition(rowRelative, colRelative);

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        while(endPositionIsPossible(nextPosition)) {
            ChessMove possibleChessMove = new ChessMove(myPosition, nextPosition, null);
            possibleMoves.add(possibleChessMove);
            previousPosition = nextPosition;
            nextPosition = nextPosition.createNewPosition(rowRelative, colRelative);
        }
        return possibleMoves;
    }


    protected boolean endPositionIsPossible(ChessPosition endPosition) {
        System.out.print("\nChecking if following position is possible: ");
        System.out.println(endPosition);
        if (isOutOfBounds(endPosition)) {
            System.out.println("NOT POSSIBLE: Position was out of bounds");
            return false;
        }

        if (isOpenPosition(endPosition)) {
            System.out.println("POSSIBLE: Position was in bounds and empty");

            return true;
        }
        // else if you can capture the piece, you can move there
        if (isEnemyPosition(endPosition)) {
            System.out.println("POSSIBLE: Position was in bounds, and occupied by an enemy");
            return true;
        }
        System.out.println("NOT POSSIBLE: Position was in bounds, but occupied by an ally");
        return false;
    }

    protected boolean isOutOfBounds(ChessPosition endPosition) {
        int x = endPosition.getRow();
        int y = endPosition.getColumn();

        if (x < 1 || x > 8) {
            return true;
        }
        if (y < 1 || y > 8) {
            return true;
        }
        // else...
        return false;
    }

    protected boolean isOpenPosition(ChessPosition endPosition) {
        return board.getPiece(endPosition) == null;
    }

    protected boolean isEnemyPosition(ChessPosition endPosition) {
        return board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor();
    }


}
