package chess;
import java.util.Collection;
import java.util.ArrayList;


public class PieceMovesCalculator {
    // Abstract interface class
    // instance variables and constructor used in child classes
    protected ChessBoard board;
    protected ChessPosition myPosition;

    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    static public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var pieceType =  board.getPiece(myPosition).getPieceType();
        switch (pieceType) {
            case KING:
                return new KingMovesCalculator(board, myPosition).calculateKingMoves();
            case QUEEN:
                return new QueenMovesCalculator(board, myPosition).calculateQueenMoves();
            case BISHOP:
                return new BishopMovesCalculator(board, myPosition).calculateBishopMoves();
            case KNIGHT:
                return new KnightMovesCalculator(board, myPosition).calculateKnightMoves();
            case ROOK:
                return new RookMovesCalculator(board, myPosition).calculateRookMoves();
            case PAWN:
                return new PawnMovesCalculator(board, myPosition).calculatePawnMoves();
        }
//        System.out.println("ERROR: switch statement didn't work properly");
        return new ArrayList<>();
    }

    //=================== Helper Functions for calculating possible moves=======================
    //-------------------Exploring Directions (Bishop, Rook, Queen)-----------------------------

    protected Collection<ChessMove> exploreDirectionAcrossBoard(int rowRelative, int colRelative) {
        ChessPosition previousPosition = myPosition;
        ChessPosition nextPosition = myPosition.createRelativePosition(rowRelative, colRelative);

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        while(endPositionIsPossible(nextPosition)) {
            ChessMove possibleChessMove = new ChessMove(myPosition, nextPosition, null);
            possibleMoves.add(possibleChessMove);

            if (! isOpenPosition(nextPosition) && isEnemyPosition(nextPosition)) {
                // end position on enemy is valid, but can't go past
                break;
            }
            previousPosition = nextPosition;
            nextPosition = nextPosition.createRelativePosition(rowRelative, colRelative);
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> exploreAllDiagonals() {
        Collection<Collection<ChessMove>> NestedCollection = new ArrayList<>();
        NestedCollection.add(exploreDirectionAcrossBoard(1, 1));
        NestedCollection.add(exploreDirectionAcrossBoard(-1, 1));
        NestedCollection.add(exploreDirectionAcrossBoard(-1, -1));
        NestedCollection.add(exploreDirectionAcrossBoard(1, -1));

        return mergeChessMoveCollections(NestedCollection);
    }

    protected Collection<ChessMove> exploreAllStraights() {
        Collection<Collection<ChessMove>> NestedCollection = new ArrayList<>();
        NestedCollection.add(exploreDirectionAcrossBoard(1, 0));
        NestedCollection.add(exploreDirectionAcrossBoard(0, 1));
        NestedCollection.add(exploreDirectionAcrossBoard(-1, 0));
        NestedCollection.add(exploreDirectionAcrossBoard(0, -1));

        return mergeChessMoveCollections(NestedCollection);
    }

    // --------------------Exploring Positions (Kings, pawns, and knights)-------------------------
    protected Collection<ChessMove> exploreRelativePositions(int[][] relativeCoordinates, ChessPiece.PieceType promotionPieceType) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int[] curRelativeCoordinates : relativeCoordinates) {
            ChessPosition curEndPosition = myPosition.createRelativePosition(curRelativeCoordinates[0], curRelativeCoordinates[1]);
            if (endPositionIsPossible(curEndPosition)) {
                ChessMove possibleChessMove = new ChessMove(myPosition, curEndPosition, promotionPieceType);
                possibleMoves.add(possibleChessMove);
            }
        }

        return possibleMoves;
    }

    // -----------------------Condensing Possible Moves and Debugging-------------------------------

        protected Collection<ChessMove> mergeChessMoveCollections(Collection<Collection<ChessMove>> nestedCollection) {
            // Note: a collection of moveCollections makes it easier to debug
            // It maintains each direction separately
        Collection<ChessMove> allPossibleMoves = new ArrayList<>();
            // DEBUG
            // System.out.print("starting position: ");
            // System.out.println(myPosition);
        for (Collection<ChessMove> moveCollection : nestedCollection) {
            allPossibleMoves.addAll(moveCollection);
//            System.out.println(moveCollection); // DEBUG
        }
        return allPossibleMoves;
    }

    // ------------------------Boolean Methods for Possible Moves-------------------------

    protected boolean endPositionIsPossible(ChessPosition endPosition) {
//        System.out.print("\nChecking if following position is possible: ");
//        System.out.println(endPosition);
        if (isOutOfBounds(endPosition)) {
//            System.out.println("NOT POSSIBLE: Position was out of bounds");
            return false;
        }

        if (isOpenPosition(endPosition)) {
//            System.out.println("POSSIBLE: Position was in bounds and empty");

            return true;
        }
        // else if you can capture the piece, you can move there
        if (isEnemyPosition(endPosition)) {
//            System.out.println("POSSIBLE: Position was in bounds, and occupied by an enemy");
            return true;
        }
//        System.out.println("NOT POSSIBLE: Position was in bounds, but occupied by an ally");
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
