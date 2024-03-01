package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the length of the secret code
        int codeLength;
        while (true) {
            System.out.println("Input the length of the secret code:");
            String input = scanner.nextLine();
            try {
                codeLength = Integer.parseInt(input);
                if (codeLength <= 0) {
                    System.out.println("Error: Length must be a positive integer.");
                    System.exit(0);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: \"" + input + "\" isn't a valid number.");
                System.exit(0);
            }
        }

        // Input the number of possible symbols in the code
        int possibleSymbols;
        while (true) {
            System.out.println("Input the number of possible symbols in the code:");
            String input = scanner.nextLine();
            try {
                possibleSymbols = Integer.parseInt(input);
                if (possibleSymbols <= 0) {
                    System.out.println("Error: Number of possible symbols must be a positive integer.");
                } else if (possibleSymbols > 36) {
                    System.out.println("Error: Maximum number of possible symbols in the code is 36 (0-9, a-z).");
                    System.exit(0);
                } else if (codeLength > possibleSymbols) {
                    System.out.println("Error: It's not possible to generate a code with a length of " + codeLength +
                            " with " + possibleSymbols + " unique symbols.");
                    System.exit(0);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: \"" + input + "\" isn't a valid number.");
            }
        }

        // Prepare secret code
        StringBuilder code = new StringBuilder();
        int maxDigit = Math.min(possibleSymbols, 9);
        char maxChar = (char) ('a' + possibleSymbols - 11);
        int digitCount = Math.min(codeLength, 10);

        // Generate secret code
        Random random = new Random();
        while (code.length() < digitCount) {
            int digit = random.nextInt(10);
            if (code.indexOf(String.valueOf(digit)) == -1) {
                code.append(digit);
            }
        }
        while (code.length() < codeLength) {
            char letter = (char) ('a' + random.nextInt(Math.min(26, possibleSymbols - 10)));
            if (code.indexOf(String.valueOf(letter)) == -1) {
                code.append(letter);
            }
        }

        // Output secret code preparation message
        System.out.printf("The secret is prepared: %s (0-%d, a-%s).\n", "*".repeat(codeLength), maxDigit, maxChar);
        System.out.println("Okay, let's start a game!");

        // Start game
        int turn = 1;
        while (true) {
            System.out.printf("Turn %d:", turn);

            // Input guess
            String guess = scanner.next();

            // Check if the guess is of correct length
            if (guess.length() != codeLength) {
                System.out.println("Error: Length of the guess should be " + codeLength + ".");
                return;
            }

            // Validate if the guess contains valid symbols
            if (!isValidGuess(guess, maxChar)) {
                System.out.println("Error: Guess contains invalid symbols.");
                return;
            }

            // Calculate bulls and cows
            int bulls = 0;
            int cows = 0;
            for (int i = 0; i < codeLength; i++) {
                if (guess.charAt(i) == code.charAt(i)) {
                    bulls++;
                } else if (code.indexOf(String.valueOf(guess.charAt(i))) != -1) {
                    cows++;
                }
            }

            // Output grade
            if (bulls > 0 && cows > 0) {
                System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bulls, cows);
            } else if (bulls > 0) {
                System.out.printf("Grade: %d bull(s).\n", bulls);
            } else if (cows > 0) {
                System.out.printf("Grade: %d cow(s).\n", cows);
            } else {
                System.out.println("Grade: None.");
            }

            // Check if guess is correct
            if (bulls == codeLength) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }

            // Increment turn
            turn++;
        }
    }

    // Method to check if the guess contains valid symbols
    private static boolean isValidGuess(String guess, char maxChar) {
        for (char ch : guess.toCharArray()) {
            if (!Character.isDigit(ch) && (ch < 'a' || ch > maxChar)) {
                return false;
            }
        }
        return true;
    }
}