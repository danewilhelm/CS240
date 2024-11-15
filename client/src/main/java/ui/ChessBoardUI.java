package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    private final String whiteColumnHeader = "    a  b  c  d  e  f  g  h    ";
    private final String blackColumnHeader = "    h  g  f  e  d  c  b  a    ";
    private final String sideHeader = "87654321";

    private PrintStream out;

    private ChessBoard board;
    private boolean isWhitePerspective;

    private int sideHeaderIndex;

    private int curRow;
    private int curCol;


    public ChessBoardUI (ChessGame game, ChessGame.TeamColor teamColor) {
        out = new PrintStream(System.out, true);
        out.print(SET_TEXT_BOLD);

        board = game.getBoard();
        isWhitePerspective = teamColor.equals(ChessGame.TeamColor.WHITE);

        sideHeaderIndex = isWhitePerspective ? 0 : 7;

        if (isWhitePerspective) {
            curRow = 8;
            curCol = 1;
        } else {
            curRow = 1;
            curCol = 8;
        }
    }
    
    
    public static void main(String[] args) {
        observe();
    }

    public static void observe() {
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();

        ChessBoardUI blackPlayerPrinter = new ChessBoardUI(game, ChessGame.TeamColor.BLACK);
        ChessBoardUI whitePlayerPrinter = new ChessBoardUI(game, ChessGame.TeamColor.WHITE);
        blackPlayerPrinter.drawChessBoardUI();
        whitePlayerPrinter.drawChessBoardUI();
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
        out.print(isWhitePerspective ? whiteColumnHeader : blackColumnHeader);
        printNewLine();
    }

    private void printNewLine() {
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void setHeaderColoring() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    // ------------------------------------- ChessBoard Drawing Helper Methods -----------------------------------------
    private void drawBlackSquare() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(" ");
        drawChessPiece();
        out.print(" ");
    }

    private void drawWhiteSquare() {
        out.print(SET_BG_COLOR_WHITE);
        out.print(" ");
        drawChessPiece();
        out.print(" ");
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
            out.print(SET_TEXT_COLOR_RED);
        } else {
            out.print(SET_TEXT_COLOR_BLUE);
        }
    }



    // ---------------------------------- Incrementing Helper Methods ----------------------------------------
    private void incrementChessPosition() {
        if (isWhitePerspective) {
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
        if (isWhitePerspective) {
            sideHeaderIndex++;
        } else {
            sideHeaderIndex--;
        }
    }
}
