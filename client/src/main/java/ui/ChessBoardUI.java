package ui;

import chess.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    private final String whiteColumnHeader = "    a  b  c  d  e  f  g  h    ";
    private final String blackColumnHeader = "    h  g  f  e  d  c  b  a    ";
    private final String sideHeader = "87654321";

    private PrintStream out;

    private ChessBoard board;
    private final boolean IS_WHITE_PERSPECTIVE;

    private int sideHeaderIndex;

    private int curRow;
    private int curCol;

    ChessPosition highlightedPosition;
    Collection<ChessPosition> legalMovePositions = new ArrayList<>();

    public ChessBoardUI (ChessGame game, ChessGame.TeamColor teamColor, ChessPosition highlightedPosition) {
        out = new PrintStream(System.out, true);
        out.print(SET_TEXT_BOLD);

        board = game.getBoard();
        IS_WHITE_PERSPECTIVE = teamColor.equals(ChessGame.TeamColor.WHITE);

        sideHeaderIndex = IS_WHITE_PERSPECTIVE ? 0 : 7;

        if (IS_WHITE_PERSPECTIVE) {
            curRow = 8;
            curCol = 1;
        } else {
            curRow = 1;
            curCol = 8;
        }

        this.highlightedPosition = highlightedPosition;
        if (highlightedPosition != null) {
            Collection<ChessMove> legalMoves = board.getPiece(highlightedPosition).pieceMoves(board, highlightedPosition);
            for (ChessMove move : legalMoves) {
                this.legalMovePositions.add(move.getEndPosition());
            }
        }
    }

    // ------------------------------------ TESTING AND MAIN ----------------------------------------------------------
    public static void main(String[] args) {
        testHighlightLegalMoves();
    }

    public static void observePhase5() {
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();

        ChessBoardUI blackPlayerPrinter = new ChessBoardUI(game, ChessGame.TeamColor.BLACK, null);
        ChessBoardUI whitePlayerPrinter = new ChessBoardUI(game, ChessGame.TeamColor.WHITE, null);
        blackPlayerPrinter.drawChessBoardUI();
        whitePlayerPrinter.drawChessBoardUI();
    }

    public static void testHighlightLegalMoves() {
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();

        ChessBoardUI blackPlayerPrinter = new ChessBoardUI(game, ChessGame.TeamColor.BLACK, new ChessPosition(2, 2));
        blackPlayerPrinter.drawChessBoardUI();

        ChessBoardUI otherBlackPlayerPrinter = new ChessBoardUI(game, ChessGame.TeamColor.BLACK, new ChessPosition(2, 3));
        otherBlackPlayerPrinter.drawChessBoardUI();
    }

    // ---------------------------------- Highlighting Moves helper methods ------------------------

    private boolean isWhiteSquare(ChessPosition pos) {
        boolean isOddRow = pos.getRow() % 2 == 1;
        boolean isOddColumn = pos.getColumn() % 2 == 1;

        if ((isOddRow && isOddColumn) || (!isOddRow && !isOddColumn)) {
            return false;
        } else {
            return true;
        }
    }


    // ---------------------------------- Creating the entire board---------------------------------

    private void drawChessBoardUI() {
        printNewLine();
        drawColumnHeader();
        drawMiddleRows();
        drawColumnHeader();
        printNewLine();
    }

    private void drawMiddleRows() {
        for (int i = 0; i < 4; i++) {
            drawMiddleRow(true);
            drawMiddleRow(false);
        }
    }

    // ---------------------------------- Drawing Single Rows-----------------------------------
    private void drawMiddleRow(boolean startWithWhite) {
        drawSideHeader();
        for (int i = 0; i < 4; i++) {
            if (startWithWhite) {
                drawWhiteSquare();
                drawBlackSquare();
            } else {
                drawBlackSquare();
                drawWhiteSquare();
            }
        }
        drawSideHeader();
        printNewLine();
        incrementSideHeader();
    }

    private void drawSideHeader() {
        setHeaderColoring();
        out.print(" " + sideHeader.charAt(sideHeaderIndex) + " ");
    }



    // ------------------------------------- Drawing Headers --------------------------------------------
    private void drawColumnHeader() {
        setHeaderColoring();
        out.print(IS_WHITE_PERSPECTIVE ? whiteColumnHeader : blackColumnHeader);
        printNewLine();
    }

    private void printNewLine() {
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void setHeaderColoring() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BRIGHT_GOLD);
    }

    // ------------------------------------- ChessBoard Drawing Helper Methods -----------------------------------------
    private void drawWhiteSquare() {
        if (isStartPosition()) {
            out.print(SET_BG_COLOR_BLUE);
        } else if (isLegalMovePosition()) {
            out.print(SET_BG_COLOR_YELLOW);
            out.print(SET_TEXT_COLOR_BLACK);
        } else {
            out.print(SET_BG_COLOR_GOLD);
        }

        out.print(" ");
        drawChessPiece();
        out.print(" ");
    }

    private void drawBlackSquare() {
        if (isStartPosition()) {
            out.print(SET_BG_COLOR_BLUE);
        } else if (isLegalMovePosition()) {
            out.print(SET_BG_COLOR_GREEN);
            out.print(SET_TEXT_COLOR_BLACK);
        } else {
            out.print(SET_BG_COLOR_DARK_GREEN);
        }

        out.print(" ");
        drawChessPiece();
        out.print(" ");
    }

    private boolean isLegalMovePosition() {
        ChessPosition curPosition = new ChessPosition(curRow, curCol);
        return legalMovePositions != null && legalMovePositions.contains(curPosition);
    }

    private boolean isStartPosition() {
        return highlightedPosition != null && highlightedPosition.equals(new ChessPosition(curRow, curCol));
    }

    private void drawChessPiece() {
        ChessPosition curPosition = new ChessPosition(curRow, curCol);
        ChessPiece curPiece = board.getPiece(curPosition);
        incrementChessPosition();
        if (curPiece == null) {
            out.print(" ");
            return;
        }

        setPieceColoring(curPiece);
        switch (curPiece.getPieceType()) {
            case KING -> out.print("K");
            case PAWN -> out.print("P");
            case ROOK -> out.print("R");
            case QUEEN -> out.print("Q");
            case BISHOP -> out.print("B");
            case KNIGHT -> out.print("N");
        }
    }

    private void setPieceColoring(ChessPiece curPiece) {
        if (curPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            out.print(SET_TEXT_COLOR_WHITE);
        } else {
            out.print(SET_TEXT_COLOR_BLACK);
        }
    }



    // ---------------------------------- Incrementing Helper Methods ----------------------------------------
    private void incrementChessPosition() {
        if (IS_WHITE_PERSPECTIVE) {
            if (curCol == 8) {
                    curCol = 1;
                    curRow--;
                } else {
                    curCol++;
                }
        } else {
            if (curCol == 1) {
                curCol = 8;
                curRow++;
            } else {
                curCol--;
            }
        }
    }

    private void incrementSideHeader() {
        if (IS_WHITE_PERSPECTIVE) {
            sideHeaderIndex++;
        } else {
            sideHeaderIndex--;
        }
    }

    private void helperMethod () {
        while(true) {
            System.out.println("Hello");
            System.out.println("Goodbye");
            if (IS_WHITE_PERSPECTIVE) {
                break;
            } else {
                break;
            }
        }
    }

    private void helperMethod2 () {
        while(IS_WHITE_PERSPECTIVE) {
            System.out.println("Goodbye");
            System.out.println("Hello");
            if (! IS_WHITE_PERSPECTIVE) {
                break;
            } else {
                break;
            }
        }
    }

}


