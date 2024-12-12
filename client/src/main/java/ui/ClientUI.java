package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import model.GameData;
import service.BadRequestException;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ClientUI {

    private boolean isRunning;
    private boolean isLoggedIn;
    private boolean isObserver;

    private String teamPerspective;
    private Integer joinedGameID;

    private ServerFacade serverFacade = new ServerFacade("http://localhost:8080");



    public static void main(String[] args) throws Exception {
        new ClientUI().run();
    }

    private String[] getInput(String userStatus) {
        System.out.print("[" + userStatus + "] >>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        return line.split(" ");
    }

    public void run() {
        System.out.println("Welcome to CS 240 chess. Type help to get started (^v^)");

        isRunning = true;
        isLoggedIn = false;

        while (isRunning) {
            String[] input = getInput("LOGGED_OUT");
            String command = input[0].toLowerCase();
            switch (command) {
                case "help":
                    printLoggedOutHelp();
                    break;
                case "quit":
                    System.out.println("Goodbye :(");
                    isRunning = false;
                    break;
                case "register":
                    attemptRegister(input);
                    break;
                case "login":
                    attemptLogin(input);
                    break;
                default:
                    System.out.println("oi bruv, I don't understand what you want");
            }

        }
    }


    private void postLoginLoop() {

        while (isRunning && isLoggedIn) {
            isObserver = false;
            joinedGameID = null;
            String[] input = getInput("LOGGED_IN");
            String command = input[0].toLowerCase();
            switch (command) {
                case "create":
                    attemptCreateGame(input);
                    break;
                case "help":
                    printLoggedInHelp();
                    break;
                case "list":
                    attemptListGames();
                    break;
                case "join":
                    attemptJoinGame(input);
                    break;
                case "observe":
                    attemptObserveGame(input);
                    break;
                case "logout":
                    attemptLogout();
                    break;
                case "quit":
                    System.out.println("Goodbye :(");
                    isRunning = false;
                    return;
                default:
                    System.out.println("oi bruv, I don't understand what you want");
            }
        }
    }

    private void gameLoop() {
        while (true) {
            String[] input = getInput("IN_GAME");
            String command = input[0].toLowerCase();
            switch (command) {
                case "help":
                    printInGameHelp();
                    break;
                case "redraw":
                    drawBoard(null);
                    break;
                case "leave":
                    leaveGame();
                    return;
                case "move":
                    attemptMove(input);
                    break;
                case "resign":
                    resignGame();
                    break;
                case "highlight":
                    highlightPosition(input);
                    break;
                default:
                    System.out.println("oi bruv, I don't understand what you want");
            }
        }
    }




    // --------------------------- Game Loop Helper Methods ---------------------------------------------------------
    private void printInGameHelp() {
        System.out.println("help - oi bruv, you lookin' at it");
        System.out.println("redraw - artist has to redraw the chess board, again...");
        System.out.println("leave - leaving the game so soon?");
        System.out.println("move <starting position of moving piece> <ending position of moving piece> - " +
                "\n     Ex: \"move c4 c5\" when moving a pawn" +
                "\n     Ex: \"move C7 C8 Q\" to move and promote a pawn to a queen");
        System.out.println("resign - gg I guess");
        System.out.println("highlight <location on board> - highlights possible moves for a given location on the board. Ex: \"highlight b6\"");
    }

    private void drawBoard(ChessPosition highlightedPosition) {
        GameData joinedGame = getJoinedGame();
        if (joinedGame == null) {
            System.out.println("ERROR: server did not find your game, and cannot draw it");
            return;
        }

        ChessBoardUI boardUI = new ChessBoardUI(joinedGame.game(), teamPerspective, highlightedPosition);
        boardUI.drawChessBoardUI();
    }

    private void highlightPosition(String[] input) {
        if (input.length < 2) {
            System.out.println("Missing input: highlight <location on board> - highlights possible moves for a given location on the board. Ex: \"highlight b6\"");
            return;
        }

        String selectedCoordinates = input[1];
        ChessPosition highlightedPosition;
        try {
             highlightedPosition = parseCoordinates(selectedCoordinates);
        } catch (BadRequestException e) {
            return;
        }

        if (highlightedPosition == null) {
            System.out.println("Highlighting failed. Please try again");
            return;
        }

        drawBoard(highlightedPosition);
    }

    private ChessPosition parseCoordinates(String selectedCoordinates) throws BadRequestException {
        String givenColString = selectedCoordinates.charAt(0) + "";
        givenColString = givenColString.toLowerCase();
        if (! givenColString.matches("^[a-h]$")) {
            System.out.println("the coordinates you gave are not valid. Try the following format: B6, or c2");
            throw new BadRequestException("");
        }

        int givenRow = selectedCoordinates.charAt(1) - '0';

        int givenCol;
        switch (givenColString) {
            case "a":
                givenCol = 1;
                break;
            case "b":
                givenCol = 2;
                break;
            case "c":
                givenCol = 3;
                break;
            case "d":
                givenCol = 4;
                break;
            case "e":
                givenCol = 5;
                break;
            case "f":
                givenCol = 6;
                break;
            case "g":
                givenCol = 7;
                break;
            case "h":
                givenCol = 8;
                break;
            default:
                System.out.println("ERROR: did not parse coordinates correctly");
                return null;
        }

        return new ChessPosition(givenRow, givenCol);
    }

    private void leaveGame() {
        isObserver = false;
        if (! serverFacade.leaveGame(joinedGameID)) {
            System.out.println("failed to connect observer");
        }
    }

    private void attemptMove(String[] input) {
        if (input.length < 3) {
            System.out.println("missing input to make a move");
            return;
        }

        ChessPosition startPosition;
        ChessPosition endPosition;
        ChessPiece.PieceType promotionPiece = null;

        try {
            startPosition = parseCoordinates(input[1]);
            endPosition = parseCoordinates(input[2]);
        } catch (BadRequestException e) {
            return;
        }

        if (input.length == 4) {
            if (input[3].length() > 1) {
                System.out.println("The format for promotion pieces should be a single character as seen on the board");
                return;
            }

            promotionPiece = parsePromotionPiece(input[3]);
        }

        ChessMove attemptedMove = new ChessMove(startPosition, endPosition, promotionPiece);
        if (! serverFacade.makeMove(joinedGameID, attemptedMove))  {
            System.out.println("failed to connect observer");
        }
    }

    private ChessPiece.PieceType parsePromotionPiece(String str) {
        String string = str.toUpperCase();
        ChessPiece.PieceType promotionPiece;
        switch (string) {
            case "Q":
                promotionPiece = ChessPiece.PieceType.QUEEN;
                break;
            case "K":
                promotionPiece = ChessPiece.PieceType.KING;
                break;
            case "R":
                promotionPiece = ChessPiece.PieceType.ROOK;
                break;
            case "N":
                promotionPiece = ChessPiece.PieceType.KNIGHT;
                break;
            case "B":
                promotionPiece = ChessPiece.PieceType.BISHOP;
                break;
            case "P":
                promotionPiece = ChessPiece.PieceType.PAWN;
                break;
            default:
                System.out.println("ERROR: did not parse promotion piece correctly");
                return null;
        }
        return promotionPiece;
    }



    private void resignGame() {
        if (! serverFacade.resignGame(joinedGameID)) {
            System.out.println("failed to connect observer");
        }
    }

    private GameData getJoinedGame() {
        return getGameFromID(joinedGameID);
    }

    private GameData getGameFromID(int givenGameID) {
        Collection<GameData> listedGames = serverFacade.listGames();

        for (GameData curGame : listedGames) {
            if (curGame.gameID() == givenGameID) {
                return curGame;
            }
        }
        return null;
    }


    // ---------------------------- Post-Login Helper Methods --------------------------------------------------------
    private void attemptObserveGame(String[] input) {
        if (input.length != 2) {
            System.out.println("Missing input: observe <ID>");
            System.out.println("Find the correct ID by searching the games list");
            return;
        }


        if (! input[1].matches("\\d+")) {
            System.out.println("Incorrect input: ID must be a number");
            System.out.println("Find the correct ID by searching the games list");
            return;
        }

        int givenGameID = Integer.parseInt(input[1]);

        GameData observedGame = getGameFromID(givenGameID);
        if (observedGame == null) {
            System.out.println("Incorrect input: There is no game associated with this ID");
            return;
        }

        if (! serverFacade.connectPlayer(null, givenGameID)) {
            System.out.println("failed to connect observer");
            return;
        }

        // TODO: verify this is done correctly with the observer having NO team

        joinedGameID = givenGameID;
        teamPerspective = "WHITE";
        drawBoard(null);
        isObserver = true;
        System.out.println("Successfully observing game");
        gameLoop();
    }



    private void attemptJoinGame(String[] input) {
        if (input.length != 3) {
            System.out.println("missing input: join <ID> [WHITE|BLACK] - join a game");
            return;
        }

        String attemptedGameID = input[1];
        if (! attemptedGameID.matches("\\d+")) {
            System.out.println("Incorrect input: ID must be a number");
            System.out.println("Find the correct ID by searching the games list");
            return;
        }

        String joinColor = input[2].toUpperCase();
        if (! (joinColor.equals("WHITE") || joinColor.equals("BLACK"))) {
            System.out.println("incorrect input: the team color must be WHITE or BLACK");
            return;
        }


        if (! serverFacade.joinGame(joinColor, Integer.parseInt(attemptedGameID))) {
            System.out.println("Failed to join game. Try again");
            return;
        }

        if (! serverFacade.connectPlayer(joinColor, Integer.parseInt(attemptedGameID))) {
            System.out.println("Failed to connect to websocket");
            return;
        }

        System.out.println("Successfully joined game as player");
        joinedGameID = Integer.parseInt(attemptedGameID);
        teamPerspective = joinColor;
        drawBoard(null);
        isObserver = false;
        gameLoop();
    }

    private void attemptListGames() {
        Collection<GameData> gamesList = serverFacade.listGames();
        if (gamesList == null) {
            System.out.println("Failed to list games. Try again");
            return;
        }

        if (gamesList.isEmpty()) {
            System.out.println("There are no games created");
        } else {
            int indexGameID = 1;
            for (GameData curGame: gamesList) {
                System.out.println(indexGameID + ". " + curGame.gameName());
                System.out.println("    whitePlayer: " + curGame.whiteUsername());
                System.out.println("    blackPlayer: " + curGame.blackUsername());
                indexGameID++;
            }
        }
    }


    private void attemptLogout() {
        if (serverFacade.logout()) {
            System.out.println("Successfully logged out");
            isLoggedIn = false;
        } else {
            System.out.println("Failed to log out. Try again");
        }
    }

    private void printLoggedInHelp() {
        System.out.println("create <NAME> - create a game");
        System.out.println("list - see all games and their gameID's");
        System.out.println("join <ID> [WHITE|BLACK] - join a game");
        System.out.println("observe <ID> - watch your friend lose");
        System.out.println("logout - when you are done");
        System.out.println("quit - pls don't");
        System.out.println("help - oi bruv, you lookin' at it");
    }

    private void attemptCreateGame(String[] input) {
        if (input.length == 1) {
            System.out.println("Missing input: create <NAME> - create a game");
            return;
        }

        String gameName = input[1];
        for (int i = 2; i < input.length; i++) {
            gameName = gameName + " " + input[i];
        }

        if (serverFacade.createGame(gameName)) {
            System.out.println("Successfully created game");
        } else {
            System.out.println("Failed to register. Try again");
        }
    }

    // ------------------------------------- Pre-Login Helper Methods--------------------------------------------------
    private void attemptRegister(String[] input) {
        if (input.length != 4) {
            System.out.println("oi bruv, you need to give a username, password, and email. Nothin' more, nothin' less");
            return;
        }

        if (serverFacade.register(input[1], input[2], input[3])) {
            System.out.println("Successfully registered");
            isLoggedIn = true;
            postLoginLoop();
        } else {
            System.out.println("Failed to register. Try again");
        }
    }

    private void attemptLogin(String[] input) {
        if (input.length != 3) {
            System.out.println("oi bruv, you need to give a username and password. Nothin' else");
            return;
        }

        if (serverFacade.login(input[1], input[2])) {
            System.out.println("Successfully logged in");
            isLoggedIn = true;
            postLoginLoop();
        } else {
            System.out.println("Failed to log in. Try again");
        }

    }

    private void printLoggedOutHelp() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to login into your account");
        System.out.println("quit - exit the program");
        System.out.println("help - oi bruv, you lookin' at it");
    }



}
