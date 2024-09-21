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
         System.out.println(upRightMoves);
         System.out.println(downRightMoves);
         System.out.println(downLeftMoves);
         System.out.println(upLeftMoves);


        return allPossibleMoves;
    }



}
