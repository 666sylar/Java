package lastpencil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int pencils = getInitialPencils(scanner);
        String firstPlayer = getFirstPlayer(scanner);

        while (pencils > 0) {
            if (firstPlayer.equalsIgnoreCase("John")) {
                pencils = johnsTurn(pencils, scanner);
                firstPlayer = "Jack";
            } else if (firstPlayer.equalsIgnoreCase("Jack")) {
                pencils = jacksTurn(pencils);
                firstPlayer = "John";
            }
        }
    }

    private static int getInitialPencils(Scanner scanner) {
        int pencils = 0;
        while (pencils <= 0) {
            System.out.println("How many pencils would you like to use:");
            String userInput = scanner.nextLine();
            if (isNumeric(userInput)) {
                pencils = Integer.parseInt(userInput);
                if (pencils <= 0) {
                    System.out.println("The number of pencils should be positive.");
                }
            } else {
                System.out.println("The number of pencils should be numeric.");
            }
        }
        return pencils;
    }

    private static String getFirstPlayer(Scanner scanner) {
        String firstPlayer = "";
        while (!firstPlayer.equalsIgnoreCase("John") && !firstPlayer.equalsIgnoreCase("Jack")) {
            System.out.println("Who will be the first (John, Jack):");
            firstPlayer = scanner.nextLine();
            if (!firstPlayer.equalsIgnoreCase("John") && !firstPlayer.equalsIgnoreCase("Jack")) {
                System.out.println("Choose between 'John' and 'Jack'.");
            }
        }
        return firstPlayer;
    }

    private static int johnsTurn(int pencils, Scanner scanner) {
        System.out.println(drawPencils(pencils));
        System.out.println("John's turn!");
        int johnsMove = getUserInput(pencils, scanner);
        pencils -= johnsMove;
        if (pencils <= 0) {
            System.out.println("Jack won!");
        }
        return pencils;
    }

    private static int jacksTurn(int pencils) {
        System.out.println(drawPencils(pencils));
        System.out.println("Jack's turn:");
        int jacksMove = (pencils % 4 == 1) ? 1 : (pencils - 1) % 4;
        if (jacksMove == 0) {
            jacksMove = 3;
        }
        System.out.println(jacksMove);
        pencils -= jacksMove;
        if (pencils <= 0) {
            System.out.println("John won!");
        }
        return pencils;
    }

    private static int getUserInput(int pencils, Scanner scanner) {
        int move = Integer.parseInt(scanner.nextLine());
        while (move < 1 || move > 3 || move > pencils) {
            try {
                System.out.println("Possible values: '1', '2', '3'");
                move = Integer.parseInt(scanner.nextLine());
                if (move > pencils) {
                    System.out.println("Too many pencils were taken");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return move;
    }

    private static String drawPencils(int count) {
        return "|".repeat(Math.max(0, count));
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9]+");
    }
}