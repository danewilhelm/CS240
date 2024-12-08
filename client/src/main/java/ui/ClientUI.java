package ui;

import client.ServerFacade;
import model.GameData;

import java.util.Collection;
import java.util.Scanner;

public class ClientUI {

    private boolean isRunning = true;
    private boolean isLoggedIn = false;
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
        while (isRunning) {
            String[] input = getInput("LOGGED_OUT");
            String command = input[0].toLowerCase();
            switch (command) {
                case "help":
                    printLoggedOutHelp();
                    break;
                case "quit":
                    System.out.println("Goodbye :(");
                    isRunning=false;
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


        Collection<GameData> gamesList = serverFacade.listGames();
        GameData observedGame = null;
        for (GameData curGame: gamesList) {
            if (curGame.gameID() == givenGameID) {
                observedGame = curGame;
            }
        }

        if (observedGame == null) {
            System.out.println("Incorrect input: There is no game associated with this ID");
        } else {
            ChessBoardUI.observePhase5();
        }

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

    private void attemptJoinGame(String[] input) {
        if (input.length != 3) {
            System.out.println("missing input: join <ID> [WHITE|BLACK] - join a game");
            return;
        }

        if (! input[1].matches("\\d+")) {
            System.out.println("Incorrect input: ID must be a number");
            System.out.println("Find the correct ID by searching the games list");
            return;
        }

        String joinColor = input[2].toUpperCase();
        if (! (joinColor.equals("WHITE") || joinColor.equals("BLACK"))) {
            System.out.println("incorrect input: the team color must be WHITE or BLACK");
        }

        if (serverFacade.joinGame(input[2], Integer.parseInt(input[1]))) {
            System.out.println("Successfully joined game");
            postLoginLoop();
        } else {
            System.out.println("Failed to join game. Try again");
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
