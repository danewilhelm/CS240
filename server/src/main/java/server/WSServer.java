package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WSServer {

    private Session session;
    private static final Map<Session, Integer> sessionsMap = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        this.session = session;


        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);

        Gson serializer = new Gson();
        UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);

        UserGameCommand.CommandType commandType = command.getCommandType();


        switch (commandType) {
            case CONNECT:
                ConnectPlayerCommand connectPlayerCommand = serializer.fromJson(message, ConnectPlayerCommand.class);
                connectToGame(command, connectPlayerCommand);
                break;
            case MAKE_MOVE:
                MakeMoveCommand makeMoveCommand = serializer.fromJson(message, MakeMoveCommand.class);
                makeMove(makeMoveCommand);
                break;
            case LEAVE:
                LeaveCommand leaveCommand = serializer.fromJson(message, LeaveCommand.class);
                leaveGame(leaveCommand);
                break;
            case RESIGN:
                ResignCommand resignCommand = serializer.fromJson(message, ResignCommand.class);
                resignGame(resignCommand);
                break;
        }
    }

    private void connectToGame(UserGameCommand command, ConnectPlayerCommand connectPlayerCommand) throws DataAccessException {
        // get AuthData from DAO using authtoken
        GameDatabaseDAO gameDAO = new GameDatabaseDAO();
        GameData game = gameDAO.getGame(command.getGameID());

        // get the GameData from DAO using game ID
        AuthDatabaseDAO authDAO = new AuthDatabaseDAO();
        AuthData authData = authDAO.getAuth(command.getAuthToken());

        // compare the usernames to see if this user is a player that joined
        String connectingUsername = authData.username();
        connectSessionAndBroadcast(game, connectPlayerCommand, connectingUsername);
    }


    private void connectSessionAndBroadcast(GameData game, UserGameCommand connectPlayerCommand, String username) {
        String message;
        ChessGame.TeamColor teamColor = game.getTeamColorByUsername(username);
        if (teamColor!= null) {
            message = username + "has connected as " + teamColor;
        } else {
            message = username + "has connected as an observer";
        }
        sessionsMap.put(session, connectPlayerCommand.getGameID());
        broadcast(message, connectPlayerCommand, false);
    }

    private void disconnectSessionAndBroadcast(GameData game, LeaveCommand leaveCommand, String username) {
        String message;
        ChessGame.TeamColor teamColor = game.getTeamColorByUsername(username);
        if (teamColor != null) {
            message = username + "has disconnected as " + teamColor;
        } else {
            message = username + "has connected as an observer";
        }
        sessionsMap.remove(session);
        broadcast(message, leaveCommand, false);
    }

    private void broadcast(String message, UserGameCommand command, boolean isBroadcastToSelf) {
        int thisGameID = command.getGameID();
        for (Session curSession : sessionsMap.keySet()) {
            boolean sameGame = sessionsMap.get(curSession) == thisGameID;
            boolean isSameSession = curSession.equals(this.session);

            if (sameGame) {
                if (isSameSession) {
                    if (!isBroadcastToSelf) {
                        continue;
                    }
                }

                try {
                    Gson serializer = new Gson();
                    NotificationMessage notification = new NotificationMessage(message);
                    curSession.getRemote().sendString(serializer.toJson(notification));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendNotificationToSelf(String message)  {
        try {
            Gson serializer = new Gson();
            NotificationMessage notification = new NotificationMessage(message);
            session.getRemote().sendString(serializer.toJson(notification));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void makeMove(MakeMoveCommand makeMoveCommand) throws DataAccessException, InvalidMoveException {
        // Allow the user to input what move they want to make.
        // The board is updated to reflect the result of the move, and the board automatically updates on all clients involved in the game.
        GameDAO gameDAO = new GameDatabaseDAO();
        AuthDAO authDAO = new AuthDatabaseDAO();
        AuthData authData = authDAO.getAuth(makeMoveCommand.getAuthToken());
        String username = authData.username();
        GameData game = gameDAO.getGame(makeMoveCommand.getGameID());
        ChessGame.TeamColor teamColor = game.getTeamColorByUsername(authData.username());
        ChessGame.TeamColor pieceColor = game.game().getTeamColorOfChessPosition(makeMoveCommand.getMove().getStartPosition());

        ChessGame chessGame = game.game();


        if (gameIsOver(chessGame)) {
            // if a player attempts to make a move after the game is over
            // notify only that player
            sendNotificationToSelf("You cannot move any pieces because the game is over");
            return;
        }


        try {
            if (pieceColor != teamColor) {
                throw new InvalidMoveException("Not your team");
            }
            game.game().makeMove(makeMoveCommand.getMove());

            // when a player makes a move
            // notify everyone about the move
            broadcast(username + " moved " + makeMoveCommand.getMove().toString(), makeMoveCommand, true);

            // if the game is in stalemate
            // Notify everyone that the game is over
            if (game.game().isInStalemate(teamColor)) {
                broadcast("Game has ended due to stalemate", makeMoveCommand, true);
            }
            // if the game is in checkmate
            // Notify everyone that the game is over
            ChessGame.TeamColor oppositeTeamColor = teamColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            if (game.game().isInCheckmate(oppositeTeamColor)) {
                broadcast("Game has ended because " + oppositeTeamColor + " is in checkmate", makeMoveCommand, true);
            }
            // if a player is in check
            // notify everyone
            else if (game.game().isInCheck(oppositeTeamColor)) {
                broadcast(oppositeTeamColor + " is in check", makeMoveCommand, true);
            }
        } catch (InvalidMoveException e) {
            // when an exception is thrown (InvalidMoveException, UnauthorizedException, etc.)
            // notify only that player
            sendNotificationToSelf("Sorry, your move was invalid because " + e.getMessage());
        }
    }

    private boolean gameIsOver(ChessGame chessGame) {
        if (chessGame.getTeamResigned() != null) {
            return true;
        }
        if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE) || chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            return true;
        }
        if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE) || chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
            return true;
        }
        return false;
    }

    private void leaveGame(LeaveCommand leaveCommand) throws DataAccessException {
        // Tells the server you are leaving the game so it will stop sending you notifications.
        // Removes the user from the game (whether they are playing or observing the game).
        // The client transitions back to the Post-Login UI.

        GameDAO gameDAO = new GameDatabaseDAO();
        AuthDAO authDAO = new AuthDatabaseDAO();


        AuthData authData = authDAO.getAuth(leaveCommand.getAuthToken());
        GameData game = gameDAO.getGame(leaveCommand.getGameID());
        if (Objects.equals(game.blackUsername(), authData.username())) {
            game = game.withBlackUsername(null);
        }
        if (Objects.equals(game.whiteUsername(), authData.username())) {
            game = game.withWhiteUsername(null);
        }
        gameDAO.updateGame(game);

        disconnectSessionAndBroadcast(game, leaveCommand, authData.username());
    }

    private void resignGame(ResignCommand resignCommand) throws DataAccessException {
        // Forfeits the match and ends the game (no more moves can be made).
        GameDAO gameDAO = new GameDatabaseDAO();
        AuthDAO authDAO = new AuthDatabaseDAO();

        AuthData authData = authDAO.getAuth(resignCommand.getAuthToken());
        GameData game = gameDAO.getGame(resignCommand.getGameID());
        ChessGame.TeamColor teamColor = game.getTeamColorByUsername(authData.username());
        if (teamColor == null) {
            sendNotificationToSelf("Observers can't resign");
            return;
        }
        if (teamColor == ChessGame.TeamColor.BLACK) {
            game.game().setTeamResigned(ChessGame.TeamColor.BLACK);
        }
        if (Objects.equals(game.whiteUsername(), authData.username())) {
            game.game().setTeamResigned(ChessGame.TeamColor.WHITE);
        }
        broadcast(authData.username() + "has resigned as " + teamColor, resignCommand, false);
        gameDAO.updateGame(game);
    }


    /*

    ------------INCOMPLETE---------------
Player Move Piece: Players can move pieces. Illegal moves rejected. Notification sent (including check or checkmate notification if applicable) and board drawn.
Game completion: No moves after game completion due to resignation, checkmate, or stalemate.


    ------------COMPLETED----------------
Help Text: Useful help text is displayed informing the user what actions they can take.
Observer Connect: Observers can connect to a game. Notification sent and board drawn.
Observer Leave Game: Observers can leave games. Notification sent.
Player Connect: Players can connect to a game as a specified color. Notification sent and board drawn.
Player Leave Game: Players can leave games. Notification sent.
Display Legal Moves: Any player or observer can display the legal moves available to any piece on the board regardless of whose turn it is.
Redraw Board: The board redraws when requested by the user (player or observer).
Player Resign Game: Players can resign from games. Notification sent.

     */

}
