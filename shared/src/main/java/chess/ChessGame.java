package chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

import static chess.ChessPiece.PieceType.KING;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
    }
    // ----------------------------------------- Getters, Setters, & Enum ----------------------------------------------------
    /**
     * Gets which teams turn it is
     * CS240 Interface Method (name cannot be changed)
     *
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     * CS240 Interface Method (name cannot be changed)
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Sets this game's chessboard with a given board
     * CS240 Interface Method (name cannot be changed)
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     * CS240 Interface Method (name cannot be changed)
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     * CS240 Interface Method (name cannot be changed)
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    // ----------------------------------------- Move Methods (Main)----------------------------------------------------

    /**
     * Gets a valid moves for a piece at the given location
     * CS240 Interface Method (name cannot be changed)
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // if there is no piece at startPosition, return null
        ChessPiece myPiece = board.getPiece(startPosition);
        if (myPiece == null) {
            return null;
        }

        // get possible moves
        Collection<ChessMove> possibleMoves = myPiece.pieceMoves(board, startPosition);

        // try each possible move to see if it's valid
        Collection<ChessMove> allValidMoves = new HashSet<>();
        for (ChessMove currentMove : possibleMoves) {
            if (isValidMove(currentMove)) {
                allValidMoves.add(currentMove);
            }
        }
        return allValidMoves;
    }

    /**
     * Makes a move in a chess game
     * CS240 Interface Method (name cannot be changed)
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // store the piece
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());

        // The move is illegal if it's not the team turn OR it's an invalid move
        if (movingPiece.getTeamColor() != teamTurn || ! isValidMove(move)) {
            throw new InvalidMoveException("This move is illegal");
        }

        // clear the start and end positions (even if the end position was already empty)
        // then place the piece at the end position
        board.clearPosition(move.getStartPosition());
        board.clearPosition(move.getEndPosition());
        board.addPiece(move.getEndPosition(), movingPiece);

        // it is now the opponent's turn
        switchTeamTurn();
    }

    // ----------------------------------------- Move Methods (Helper) ----------------------------------------------------


    private boolean isValidMove(ChessMove move) {
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());

        // use a duplicate board for testing
        ChessBoard testBoard = new ChessBoard(board);
        ChessBoard liveBoard = board;
        board = testBoard;

        // make the move on the duplicate board
        board.clearPosition(move.getStartPosition());
        board.clearPosition(move.getEndPosition());
        board.addPiece(move.getEndPosition(), movingPiece);

        // check board status
        if (isInCheck(movingPiece.getTeamColor())) {
            board = liveBoard;
            return false;
        } else {
            board = liveBoard;
            return true;
        }
    }

    private void switchTeamTurn() {
        // switch the teamTurn
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }


    // ----------------------------------------- Game Status Methods (Main) ---------------------------------------------------

    /**
     * Determines if the given team is in check
     * CS240 Interface Method (name cannot be changed)
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                ChessPosition curPosition = new ChessPosition(curRow, curCol);

                if (isEnemyPiece(teamColor, curPosition) && canCaptureKing(curPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     * CS240 Interface Method (name cannot be changed)
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                ChessPosition curPosition = new ChessPosition(curRow, curCol);
                if (! isEnemyPiece(teamColor, curPosition) && hasValidMoves(curPosition)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     * CS240 Interface Method (name cannot be changed)
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    // ----------------------------------------- Game Status Methods (Helper) ---------------------------------------------------

    private boolean isEnemyPiece(TeamColor teamColor, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        return piece != null && piece.getTeamColor() != teamColor;
    }

    private boolean canCaptureKing(ChessPosition position) {
        ChessPiece curPiece = board.getPiece(position);

        Collection<ChessMove> allPossibleMoves = curPiece.pieceMoves(board, position);
        for (ChessMove possibleMove : allPossibleMoves) {
            ChessPiece enemyPiece = board.getPiece(possibleMove.getEndPosition());
            if (enemyPiece != null && enemyPiece.getPieceType() == KING) {
                return true;
            }
        }
        return false;
    }

    private boolean hasValidMoves(ChessPosition position) {
        Collection<ChessMove> allValidMoves = validMoves(position);
        return allValidMoves != null && !allValidMoves.isEmpty();
    }
}
