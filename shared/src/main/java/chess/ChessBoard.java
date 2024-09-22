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

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
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

    private void insertRowOfPawns(ChessGame.TeamColor givenColor) {
        int colorRow;
        if (givenColor == ChessGame.TeamColor.BLACK) {
            colorRow = 7;
        } else {
            colorRow = 2;
        }

        for (int curCol = 1; curCol < 9; curCol++) {
            ChessPosition curPosition = new ChessPosition(colorRow, curCol);
            ChessPiece curPiece = new ChessPiece(givenColor, ChessPiece.PieceType.PAWN);
            addPiece(curPosition, curPiece);
        }
    }

    private void insertRowOfSpecialPieces(ChessGame.TeamColor givenColor) {
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

        int colorRow;
        if (givenColor == ChessGame.TeamColor.BLACK) {
           colorRow = 8;
        } else {
            colorRow = 1;
        }

        int curCol = 1;
        for (ChessPiece.PieceType curPieceType : specialPiecesOrder) {
            ChessPosition curPosition = new ChessPosition(colorRow, curCol);
            ChessPiece curPiece = new ChessPiece(givenColor, curPieceType);
            addPiece(curPosition, curPiece);
            curCol++;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ChessBoard)) {
            return false;
        }

        ChessBoard otherBoard = (ChessBoard) object;
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                ChessPosition curPosition = new ChessPosition(curRow, curCol);
                ChessPiece thisCurPiece = this.getPiece(curPosition);
                ChessPiece otherCurPiece = otherBoard.getPiece(curPosition);
                // if both positions are empty, then there is no difference
                if (thisCurPiece == null && otherCurPiece == null) {
                    continue;
                // if only one position is empty, then they are NOT equal
                }
                if (thisCurPiece == null || otherCurPiece == null) {
                    return false;
                }
                // if both positions are occupied, they must be compared
                if (! this.getPiece(curPosition).equals(otherBoard.getPiece(curPosition))) {
                    return false;
                }
            }
        }
        // all pieces in all positions are the same
        return true;
    }

    @Override
    public int hashCode() {
        int result = 42;
        for (int curRow = 1; curRow < 9; curRow++) {
            for (int curCol = 1; curCol < 9; curCol++) {
                ChessPosition curPosition = new ChessPosition(curRow, curCol);
                ChessPiece curPiece = getPiece(curPosition);
                if (curPiece != null) {
                    result *= curPiece.hashCode();
                }
            }
        }

        return result;
    }




}
