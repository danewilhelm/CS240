package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
    }

    public ChessBoard(ChessBoard otherBoard) {
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                ChessPosition curPosition = new ChessPosition(curRow, curCol);
                ChessPiece curPiece = otherBoard.getPiece(curPosition);
                addPiece(curPosition, curPiece);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     * CS240 Interface Method (name cannot be changed)
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }


    // TASK: add Javadoc
    public void clearPosition(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     * CS240 Interface Method (name cannot be changed)
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     * CS240 Interface Method (name cannot be changed)
     */
    public void resetBoard() {
        // wipe the board
        squares = new ChessPiece[8][8];

        // put pieces onto the board
        insertRowOfSpecialPieces(ChessGame.TeamColor.BLACK);
        insertRowOfPawns(ChessGame.TeamColor.BLACK);
        insertRowOfPawns(ChessGame.TeamColor.WHITE);
        insertRowOfSpecialPieces(ChessGame.TeamColor.WHITE);
    }


    // TASK: Create JavaDoc
    protected ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                ChessPosition curPosition = new ChessPosition(curRow, curCol);
                ChessPiece curPiece = getPiece(curPosition);
                if (curPiece.getPieceType() == ChessPiece.PieceType.KING && curPiece.getTeamColor() == teamColor) {
                    return curPosition;
                }
            }
        }
        System.out.println("ERROR: getKingPosition() didn't find the king");
        return null;
    }

    /**
     * Inserts a row of pawns based on the given color
     * A helper method for resetBoard()
     * @param givenColor The color of pawns to be added
     */
    private void insertRowOfPawns(ChessGame.TeamColor givenColor) {
        // find the correct pawn row from the given color
        int colorRow;
        if (givenColor == ChessGame.TeamColor.BLACK) {
            colorRow = 7;
        } else {
            colorRow = 2;
        }

        // add pawns into the row
        for (int curCol = 1; curCol < 9; curCol++) {
            ChessPosition curPosition = new ChessPosition(colorRow, curCol);
            ChessPiece curPiece = new ChessPiece(givenColor, ChessPiece.PieceType.PAWN);
            addPiece(curPosition, curPiece);
        }
    }

    /**
     * Inserts a row of special pieces based on the given color
     * A helper method for resetBoard()
     * @param givenColor The color of special pieces to be added
     */
    private void insertRowOfSpecialPieces(ChessGame.TeamColor givenColor) {
        // find row to insert special pieces based on color
        int colorRow;
        if (givenColor == ChessGame.TeamColor.BLACK) {
           colorRow = 8;
        } else {
            colorRow = 1;
        }

        // create order of special pieces
        ChessPiece.PieceType[] specialPiecesOrder = new ChessPiece.PieceType[]{
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        // add special pieces to the row
        int curCol = 1;
        for (ChessPiece.PieceType curPieceType : specialPiecesOrder) {
            ChessPosition curPosition = new ChessPosition(colorRow, curCol);
            ChessPiece curPiece = new ChessPiece(givenColor, curPieceType);
            addPiece(curPosition, curPiece);
            curCol++;
        }
    }

    //===============================Override Methods===========================================

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ChessBoard)) {
            return false;
        }

        // deep scan the chessboard to verify if each position has identical pieces
        ChessBoard otherBoard = (ChessBoard) object;
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                // get pieces from both boards at current position
                ChessPosition curPosition = new ChessPosition(curRow, curCol);
                ChessPiece thisCurPiece = this.getPiece(curPosition);
                ChessPiece otherCurPiece = otherBoard.getPiece(curPosition);

                // NOTE: .equals() does not work with null
                // if both positions are empty, they are equal
                if (thisCurPiece == null && otherCurPiece == null) {
                    continue;
                }

                // if only one position is empty, they are not equal
                if (thisCurPiece == null || otherCurPiece == null) {
                    return false;
                }

                // if both positions are occupied, the pieces must be compared
                if (! this.getPiece(curPosition).equals(otherBoard.getPiece(curPosition))) {
                    return false;
                }
            }
        }
        // all positions were verified to have equal pieces
        return true;
    }

    @Override
    public int hashCode() {
        // Note: HashCode() does not work with null
        int result = 42;
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                // get piece at current position
                ChessPosition curPosition = new ChessPosition(curRow, curCol);
                ChessPiece curPiece = getPiece(curPosition);

                // null protection for HashCode()
                if (curPiece != null) {
                    result *= curPiece.hashCode();
                }
            }
        }
        return result;
    }
}
