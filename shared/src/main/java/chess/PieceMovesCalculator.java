package chess;
import java.util.Collection;
import java.util.ArrayList;

/**
 * This is an abstract interface class
 * It provides helper methods to help calculate moves for all types of pieces
 */
public class PieceMovesCalculator {

    // instance variables and constructor used in child classes
    protected ChessBoard board;
    protected ChessPosition myPosition;

    // Constructor
    public PieceMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    /**
     * Calculates all the possible moves for a piece on the given board.
     * Used by other classes as an interface method
     *
     * @param board The given chessboard
     * @param myPosition the position of the piece attempting to move
     * @return a collection of ChessMoves that represent all the possible moves for that piece
     */
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
        // ERROR: switch statement didn't work properly
        return new ArrayList<>();
    }

    //=================== Helper Functions for calculating possible moves=======================
    //-------------------Exploring Directions (Bishop, Rook, Queen)-----------------------------

    /**
     * Calculates the possible moves for a piece in a continuous direction (straight or diagonal)
     *
     * @param rowRelative The row position relative to myPosition
     * @param colRelative The column position relative to myPosition
     * @return All possible moves in that direction
     */
    protected Collection<ChessMove> exploreDirectionAcrossBoard(int rowRelative, int colRelative) {
        ChessPosition nextPosition = myPosition.createRelativePosition(rowRelative, colRelative);

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        while(endPositionIsPossible(nextPosition)) {
            // add the position to the list
            possibleMoves.add(new ChessMove(myPosition, nextPosition, null));

            // if the endPosition contains an enemy, we cannot continue past it
            if (! isOpenPosition(nextPosition) && isEnemyPosition(nextPosition)) {
                break;
            }

            // update nextPosition along the direction
            nextPosition = nextPosition.createRelativePosition(rowRelative, colRelative);
        }
        return possibleMoves;
    }

    /**
     * Explores all four diagonal directions from the piece's position
     * A helper function for exploreDirectionAcrossBoard()
     *
     * @return All possible moves in diagonal directions
     */
    protected Collection<ChessMove> exploreAllDiagonals() {
        Collection<Collection<ChessMove>> NestedCollection = new ArrayList<>();
        NestedCollection.add(exploreDirectionAcrossBoard(1, 1));
        NestedCollection.add(exploreDirectionAcrossBoard(-1, 1));
        NestedCollection.add(exploreDirectionAcrossBoard(-1, -1));
        NestedCollection.add(exploreDirectionAcrossBoard(1, -1));

        return mergeChessMoveCollections(NestedCollection);
    }

    /**
     * Explores all four straight directions from the piece's position
     * A helper function for exploreDirectionAcrossBoard()
     *
     * @return All possible moves in straight directions
     */
    protected Collection<ChessMove> exploreAllStraights() {
        Collection<Collection<ChessMove>> NestedCollection = new ArrayList<>();
        NestedCollection.add(exploreDirectionAcrossBoard(1, 0));
        NestedCollection.add(exploreDirectionAcrossBoard(0, 1));
        NestedCollection.add(exploreDirectionAcrossBoard(-1, 0));
        NestedCollection.add(exploreDirectionAcrossBoard(0, -1));

        return mergeChessMoveCollections(NestedCollection);
    }

    // --------------------Exploring Positions (King, Knight)-------------------------

    /**
     *  Calculates possible moves from a list of given coordinates
     *  The coordinates are relative to myPosition
     *
     * @param relativeCoordinates a list of coordinates that pieceType can move to
     * @return All possible moves from the given coordinates
     */
    protected Collection<ChessMove> exploreRelativePositions(int[][] relativeCoordinates) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        // for each coordinate
        for (int[] curRelativeCoordinates : relativeCoordinates) {
            ChessPosition curEndPosition = myPosition.createRelativePosition(curRelativeCoordinates[0], curRelativeCoordinates[1]);

            // if the position is possible, add it to the list
            if (endPositionIsPossible(curEndPosition)) {
                possibleMoves.add(new ChessMove(myPosition, curEndPosition, null));
            }
        }

        return possibleMoves;
    }

    // -----------------------Condensing Possible Moves and Debugging-------------------------------

    /**
     * Converts a nested collection of ChessMoves into a single collection
     * Very useful for debugging because each direction is in a separate collection
     *
     * @param nestedCollection A collection of collections that contain ChessMoves
     * @return a single collection of ChessMoves
     */
    protected Collection<ChessMove> mergeChessMoveCollections(Collection<Collection<ChessMove>> nestedCollection) {
        Collection<ChessMove> allPossibleMoves = new ArrayList<>();

        for (Collection<ChessMove> moveCollection : nestedCollection) {
            allPossibleMoves.addAll(moveCollection);
        }

        return allPossibleMoves;
    }

    // ------------------------Boolean Methods for Possible Moves-------------------------

    /**
     * Checks if the endPosition is possible, meaning:
     * 1. It is in bounds
     * 2. The endPosition is either:
     *      2a. Empty
     *      2b. Occupied by an opponent's piece
     * Note: Is not compatible with the pawn ruleset
     *
     * @param endPosition the position the piece will end at
     * @return a boolean whether the endPosition is possible
     */
    protected boolean endPositionIsPossible(ChessPosition endPosition) {
        if (isOutOfBounds(endPosition)) {
            return false;
        }

        if (isOpenPosition(endPosition)) {
            return true;
        }

        // else if you can capture the piece, you can move there
        if (isEnemyPosition(endPosition)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the endPosition is out of bounds
     * Note: Chess lingo uses 1 through 8 as in bounds
     *
     * @param endPosition the position the piece will end at
     * @return a boolean whether the endPosition is out of bounds
     */
    protected boolean isOutOfBounds(ChessPosition endPosition) {
        int x = endPosition.getRow();
        int y = endPosition.getColumn();

        if (x < 1 || x > 8) {
            return true;
        }
        if (y < 1 || y > 8) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the endPosition is empty
     * @param endPosition the position the piece will end at
     * @return a boolean whether the endPosition is empty
     */
    protected boolean isOpenPosition(ChessPosition endPosition) {
        return board.getPiece(endPosition) == null;
    }

    /**
     * Checks if the endPosition is occupied by an enemy
     * @param endPosition the position the piece will end at
     * @return a boolean whether the endPosition contains an enemy piece
     */
    protected boolean isEnemyPosition(ChessPosition endPosition) {
        return board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor();
    }
}
