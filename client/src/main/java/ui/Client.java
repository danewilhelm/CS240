package ui;

import java.util.Scanner;

public class Client {

    private boolean isRunning = true;


    public static void main(String[] args) throws Exception {
        new Client().run();
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
        while (isRunning) {
            String[] input = getInput("LOGGED_IN");
            String command = input[0].toLowerCase();
            switch (command) {
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
                    ChessBoardUI.observe();
                    break;
                case "logout":
                    attemptLogout(input);
                    break;
                case "quit":
                    System.out.println("Goodbye :(");
                    isRunning = false;
                    return;
            }
        }
    }

    // ---------------------------- Post-Login Helper Methods --------------------------------------------------------
    private void attemptListGames() {
    }

    private void attemptJoinGame(String[] input) {
        if (input.length != 3) {
            System.out.println("missing input: join <ID> [WHITE|BLACK] - join a game");
            return;
        }

        String joinColor = input[2].toUpperCase();
        if (! (joinColor.equals("WHITE") || joinColor.equals("BLACK"))) {
            System.out.println("incorrect input: the team color must be WHITE or BLACK");

        }
    }

    private void attemptLogout(String[] input) {

    }

    private void printLoggedInHelp() {
        System.out.println("create <NAME> - create a game");
        System.out.println("list - see unlimited games, but no bacon");
        System.out.println("join <ID> [WHITE|BLACK] - join a game");
        System.out.println("observe <ID> - watch your friend lose");
        System.out.println("logout - when you are done");
        System.out.println("quit - pls don't");
        System.out.println("help - oi bruv, you lookin' at it");
    }




    // ------------------------------------- Pre-Login Helper Methods--------------------------------------------------
    private void attemptRegister(String[] input) {
        if (input.length != 4) {
            System.out.println("oi bruv, you need to give a username, password, and email. Nothin' more, nothin' less");
            return;
        }

        // attempt to register
        postLoginLoop();

    }

    private void attemptLogin(String[] input) {
        if (input.length != 3) {
            System.out.println("oi bruv, you need to give a username and password. Nothin' else");
            return;
        }

        // attempt to login
        postLoginLoop();

    }

    private void printLoggedOutHelp() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to login into your account");
        System.out.println("quit - exit the program");
        System.out.println("help - oi bruv, you lookin' at it");
    }






}
