package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] grid = createEmptyGrid();

        printGrid(grid);

        char currentPlayer = 'X';

        // Game loop
        while (true) {
            System.out.print("Enter the coordinates: ");
            String[] coordinates = scanner.nextLine().split(" ");

            // Validate user input
            if (coordinates.length != 2 || !coordinates[0].matches("\\d") || !coordinates[1].matches("\\d")) {
                System.out.println("You should enter numbers!");
                continue;
            }

            int row = Integer.parseInt(coordinates[0]) - 1;
            int col = Integer.parseInt(coordinates[1]) - 1;

            // Check if coordinates are within the grid
            if (row < 0 || row > 2 || col < 0 || col > 2) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            // Check if the cell is occupied
            if (grid[row][col] != '_') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            // Update the grid with the user's move
            grid[row][col] = currentPlayer;

            printGrid(grid);

            // Check if the game is over
            if (isGameOver(grid, currentPlayer)) {
                System.out.println(currentPlayer + " wins");
                break;
            } else if (isGridFull(grid)) {
                System.out.println("Draw");
                break;
            }

            // Switch player
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        scanner.close();
    }

    // Method to create an empty tic-tac-toe grid
    public static char[][] createEmptyGrid() {
        return new char[][]{{'_', '_', '_'}, {'_', '_', '_'}, {'_', '_', '_'}};
    }

    // Method to print the tic-tac-toe grid
    public static void printGrid(char[][] grid) {
        System.out.println("---------");
        for (char[] row : grid) {
            System.out.print("| ");
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    // Method to check if the game is over
    public static boolean isGameOver(char[][] grid, char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] == player && grid[i][1] == player && grid[i][2] == player) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (grid[0][i] == player && grid[1][i] == player && grid[2][i] == player) {
                return true;
            }
        }

        // Check diagonals
        return (grid[0][0] == player && grid[1][1] == player && grid[2][2] == player) ||
                (grid[0][2] == player && grid[1][1] == player && grid[2][0] == player);
    }

    // Method to check if the grid is full
    public static boolean isGridFull(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == '_') {
                    return false;
                }
            }
        }
        return true;
    }
}