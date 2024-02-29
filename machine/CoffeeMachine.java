package machine;

import java.util.Scanner;

public class CoffeeMachine {
    // Initial amounts of water, milk, coffee beans, disposable cups, and money
    static int waterAmount = 400;
    static int milkAmount = 540;
    static int coffeeBeansAmount = 120;
    static int disposableCups = 9;
    static int money = 550;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWrite action (buy, fill, take, remaining, exit):");
            String input = scanner.nextLine();
            handleUserInput(input, scanner);
        }
    }

    static void handleUserInput(String input, Scanner scanner) {
        switch (input) {
            case "buy":
                buyCoffee(scanner);
                break;
            case "fill":
                fillSupplies(scanner);
                break;
            case "take":
                takeMoney();
                break;
            case "remaining":
                displayState();
                break;
            case "exit":
                System.exit(0);
            default:
                System.out.println("Invalid action. Please try again.");
        }
    }

    static void buyCoffee(Scanner scanner) {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                makeCoffee(250, 0, 16, 4);
                break;
            case "2":
                makeCoffee(350, 75, 20, 7);
                break;
            case "3":
                makeCoffee(200, 100, 12, 6);
                break;
            case "back":
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    static void makeCoffee(int waterNeeded, int milkNeeded, int beansNeeded, int cost) {
        if (waterAmount >= waterNeeded && milkAmount >= milkNeeded && coffeeBeansAmount >= beansNeeded && disposableCups >= 1) {
            System.out.println("I have enough resources, making you a coffee!");
            waterAmount -= waterNeeded;
            milkAmount -= milkNeeded;
            coffeeBeansAmount -= beansNeeded;
            disposableCups--;
            money += cost;
        } else {
            System.out.println("Sorry, not enough resources to make coffee.");
        }
    }

    static void fillSupplies(Scanner scanner) {
        System.out.println("Write how many ml of water you want to add:");
        int waterToAdd = scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add:");
        int milkToAdd = scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add:");
        int beansToAdd = scanner.nextInt();
        System.out.println("Write how many disposable cups you want to add:");
        int cupsToAdd = scanner.nextInt();

        waterAmount += waterToAdd;
        milkAmount += milkToAdd;
        coffeeBeansAmount += beansToAdd;
        disposableCups += cupsToAdd;
        System.out.println("Supplies filled.");
    }

    static void takeMoney() {
        System.out.println("I gave you $" + money);
        money = 0;
    }

    static void displayState() {
        System.out.println("\nThe coffee machine has:");
        System.out.println(waterAmount + " ml of water");
        System.out.println(milkAmount + " ml of milk");
        System.out.println(coffeeBeansAmount + " g of coffee beans");
        System.out.println(disposableCups + " disposable cups");
        System.out.println("$" + money + " of money");
    }
}