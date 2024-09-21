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


    //=================== Helper Functions for calculating possible moves=======================

    protected Collection<ChessMove> exploreDirectionAcrossBoard(int rowRelative, int colRelative) {
        ChessPosition previousPosition = myPosition;
        ChessPosition nextPosition = myPosition.createNewPosition(rowRelative, colRelative);

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        while(endPositionIsPossible(nextPosition)) {
            ChessMove possibleChessMove = new ChessMove(myPosition, nextPosition, null);
            possibleMoves.add(possibleChessMove);

            if (! isOpenPosition(nextPosition) && isEnemyPosition(nextPosition)) {
                // end position on enemy is valid, but can't go past
                break;
            }
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
