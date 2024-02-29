package cinema;
import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the number of rows
        System.out.print("Enter the number of rows:\n> ");
        int numRows = scanner.nextInt();

        // Prompt the user to enter the number of seats in each row
        System.out.print("Enter the number of seats in each row:\n> ");
        int numSeatsPerRow = scanner.nextInt();

        // Calculate total number of seats
        int totalSeats = numRows * numSeatsPerRow;

        // Initialize seating arrangement
        char[][] seats = new char[numRows][numSeatsPerRow];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numSeatsPerRow; j++) {
                seats[i][j] = 'S';
            }
        }

        int choice;
        do {
            // Print menu
            System.out.println("\n1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            System.out.print("> ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Show the seats
                    printSeats(seats);
                    break;
                case 2:
                    // Buy a ticket
                    buyTicket(scanner, seats, numRows, numSeatsPerRow);
                    break;
                case 3:
                    // Show statistics
                    showStatistics(seats, numRows, numSeatsPerRow);
                    break;
                case 0:
                    // Exit
                    break;
                default:
                    System.out.println("Invalid choice! Please enter again.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }

    // Method to print the seating arrangement
    public static void printSeats(char[][] seats) {
        System.out.println("\nCinema:");
        System.out.print("  ");
        for (int i = 1; i <= seats[0].length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < seats.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < seats[i].length; j++) {
                System.out.print(seats[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to buy a ticket
    public static void buyTicket(Scanner scanner, char[][] seats, int numRows, int numSeatsPerRow) {
        // Prompt the user to enter a row number
        System.out.print("\nEnter a row number:\n> ");
        int chosenRow = scanner.nextInt();

        // Prompt the user to enter a seat number in that row
        System.out.print("Enter a seat number in that row:\n> ");
        int chosenSeat = scanner.nextInt();

        // Check if seat is available
        if (chosenRow < 1 || chosenRow > numRows || chosenSeat < 1 || chosenSeat > numSeatsPerRow) {
            System.out.println("Wrong input!");
            return;
        }

        if (seats[chosenRow - 1][chosenSeat - 1] == 'B') {
            System.out.println("That ticket has already been purchased!");
            buyTicket(scanner, seats, numRows, numSeatsPerRow);
        }

        // Calculate ticket price
        int ticketPrice;
        int totalSeats = numRows * numSeatsPerRow;
        if (totalSeats <= 60 || chosenRow <= numRows / 2) {
            ticketPrice = 10;
        } else {
            ticketPrice = 8;
        }

        // Update seating arrangement with chosen seat
        seats[chosenRow - 1][chosenSeat - 1] = 'B';

        // Print ticket price
        System.out.println("\nTicket price: $" + ticketPrice);
    }

    // Method to show statistics
    public static void showStatistics(char[][] seats, int numRows, int numSeatsPerRow) {
        int purchasedTickets = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numSeatsPerRow; j++) {
                if (seats[i][j] == 'B') {
                    purchasedTickets++;
                }
            }
        }

        double percentageOccupancy = ((double) purchasedTickets / (numRows * numSeatsPerRow)) * 100;

        int currentIncome = calculateCurrentIncome(seats, numRows, numSeatsPerRow);
        int totalIncome = calculateTotalIncome(numRows, numSeatsPerRow);

        System.out.println("\nNumber of purchased tickets: " + purchasedTickets);
        System.out.printf("Percentage: %.2f%%\n", percentageOccupancy);
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + totalIncome);
    }

    // Method to calculate current income
    public static int calculateCurrentIncome(char[][] seats, int numRows, int numSeatsPerRow) {
        int currentIncome = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numSeatsPerRow; j++) {
                if (seats[i][j] == 'B') {
                    if (numRows * numSeatsPerRow <= 60 || i < numRows / 2) {
                        currentIncome += 10;
                    } else {
                        currentIncome += 8;
                    }
                }
            }
        }
        return currentIncome;
    }

    // Method to calculate total income
    public static int calculateTotalIncome(int numRows, int numSeatsPerRow) {
        int totalIncome;
        if (numRows * numSeatsPerRow <= 60) {
            totalIncome = numRows * numSeatsPerRow * 10;
        } else {
            int frontHalfIncome = (numRows / 2) * numSeatsPerRow * 10;
            int backHalfIncome = (numRows - numRows / 2) * numSeatsPerRow * 8;
            totalIncome = frontHalfIncome + backHalfIncome;
        }
        return totalIncome;
    }
}
