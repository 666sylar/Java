package calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Print the Earned amount header
        System.out.println("Earned amount:");

        // Print the item names and earned amounts
        printItemEarnings("Bubblegum", 202);
        printItemEarnings("Toffee", 118);
        printItemEarnings("Ice cream", 2250);
        printItemEarnings("Milk chocolate", 1680);
        printItemEarnings("Doughnut", 1075);
        printItemEarnings("Pancake", 80);

        // Calculate and print the total earnings
        double totalEarnings = 202 + 118 + 2250 + 1680 + 1075 + 80;
        System.out.println("\nIncome: $" + totalEarnings);

        // Get staff expenses from the user
        System.out.println("Staff expenses:");
        double staffExpenses = getInput();

        // Get other expenses from the user
        System.out.println("Other expenses:");
        double otherExpenses = getInput();

        // Calculate and print the net income
        double netIncome = totalEarnings - staffExpenses - otherExpenses;
        System.out.println("Net income: $" + netIncome);
    }

    // Method to print item name and earned amount
    private static void printItemEarnings(String itemName, double earnings) {
        System.out.println(itemName + ": $" + earnings);
    }

    // Method to get input from the user
    private static double getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }
}