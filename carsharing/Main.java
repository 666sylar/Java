package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String databaseFileName = getDatabaseFileName(args);
        String databaseURL = "jdbc:h2:./src/carsharing/db/" + databaseFileName;

        try (Connection connection = DriverManager.getConnection(databaseURL)) {
            connection.setAutoCommit(true);

            createCompanyTable(connection);
            createCarTable(connection);
            createCustomerTable(connection);

            // Main menu
            while (true) {
                System.out.println("1. Log in as a manager");
                System.out.println("2. Log in as a customer");
                System.out.println("3. Create a customer");
                System.out.println("0. Exit");

                int choice = readInt();
                if (choice == 0) {
                    break;
                } else if (choice == 1) {
                    managerMenu(connection);
                } else if (choice == 2) {
                    customerMenu(connection);
                } else if (choice == 3) {
                    createCustomer(connection);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getDatabaseFileName(String[] args) {
        String databaseFileName = "carsharing"; // default value

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName")) {
                databaseFileName = args[i + 1];
                break;
            }
        }

        return databaseFileName;
    }

    private static void createCompanyTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE)";
            statement.executeUpdate(sql);
        }
    }

    private static void createCarTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
            statement.executeUpdate(sql);
        }
        // Add this line to call the method to add the AVAILABLE column
        alterCarTableAddAvailableColumn(connection);
    }

    private static void createCustomerTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR(255) NOT NULL UNIQUE, " +
                    "RENTED_CAR_ID INT, " +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
            statement.executeUpdate(sql);
        }
    }

    private static void managerMenu(Connection connection) {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            int choice = readInt();
            if (choice == 0) {
                break;
            } else if (choice == 1) {
                showCompanyList(connection);
            } else if (choice == 2) {
                createCompany(connection);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showCompanyList(Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM COMPANY ORDER BY ID")) {

            if (!resultSet.isBeforeFirst()) {
                System.out.println("The company list is empty!");
            } else {
                System.out.println("Choose a company:");
                int index = 1;
                while (resultSet.next()) {
                    System.out.println(index + ". " + resultSet.getString("NAME"));
                    index++;
                }
                System.out.println("0. Back");

                int choice = readInt();
                if (choice == 0) {
                } else if (choice >= 1 && choice < index) {
                    int companyId = getCompanyId(connection, choice);
                    if (companyId != -1) {
                        companyMenu(connection, companyId);
                    }
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int getCompanyId(Connection connection, int choice) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID FROM COMPANY ORDER BY ID")) {
            int index = 1;
            while (resultSet.next()) {
                if (index == choice) {
                    return resultSet.getInt("ID");
                }
                index++;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return -1;
    }

    private static void companyMenu(Connection connection, int companyId) {
        while (true) {
            String companyName = getCompanyName(connection, companyId);
            System.out.println("'" + companyName + "' company:");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            int choice = readInt();
            if (choice == 0) {
                return;
            } else if (choice == 1) {
                showCarList(connection, companyId);
            } else if (choice == 2) {
                createCar(connection, companyId);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showCarList(Connection connection, int companyId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ? ORDER BY ID")) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.isBeforeFirst()) {
                    System.out.println("The car list is empty!");
                } else {
                    System.out.println("'" + getCompanyName(connection, companyId) + "' cars:");
                    int index = 1;
                    while (resultSet.next()) {
                        System.out.println(index + ". " + resultSet.getString("NAME"));
                        index++;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createCompany(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO COMPANY (NAME) VALUES (?)")) {
            System.out.println("Enter the company name:");
            String companyName = readString();
            statement.setString(1, companyName);
            statement.executeUpdate();
            System.out.println("The company was created!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createCar(Connection connection, int companyId) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)")) {
            System.out.println("Enter the car name:");
            String carName = readString();
            statement.setString(1, carName);
            statement.setInt(2, companyId);
            statement.executeUpdate();
            System.out.println("The car was added!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getCompanyName(Connection connection, int companyId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT NAME FROM COMPANY WHERE ID = ?")) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("NAME");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "";
    }

    private static void customerMenu(Connection connection) {
        while (true) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER ORDER BY ID")) {

                if (!resultSet.isBeforeFirst()) {
                    System.out.println("The customer list is empty!");
                    return;
                } else {
                    System.out.println("Customer list:");
                    int index = 1;
                    while (resultSet.next()) {
                        System.out.println(index + ". " + resultSet.getString("NAME"));
                        index++;
                    }
                    System.out.println("0. Back");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }


            int choice = readInt();
            if (choice == 0) {
                return;
            } else if (choice >= 1 && choice <= getCustomerCount(connection)) {
                customerSubMenu(connection, choice);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showCustomerList(Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM CUSTOMER ORDER BY ID")) {

            if (!resultSet.isBeforeFirst()) {
                System.out.println("The customer list is empty!");
            } else {
                int index = 1;
                while (resultSet.next()) {
                    System.out.println(index + ". " + resultSet.getString("NAME"));
                    index++;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int getCustomerCount(Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM CUSTOMER")) {
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    private static void customerSubMenu(Connection connection, int choice) {
        while (true) {
            try {
                int customerId = getCustomerIdByIndex(connection, choice);
                String customerName = getCustomerName(connection, customerId);
                System.out.println("'" + customerName + "' menu:");
                System.out.println("1. Rent a car");
                System.out.println("2. Return a rented car");
                System.out.println("3. My rented car");
                System.out.println("0. Back");

                int option = readInt();
                switch (option) {
                    case 0:
                        break;
                    case 1:
                        rentCar(connection, customerId);
                        break;
                    case 2:
                        returnRentedCar(connection, customerId);
                        break;
                    case 3:
                        showRentedCar(connection, customerId);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void showRentedCar(Connection connection, int customerId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT CAR.NAME AS CAR_NAME, COMPANY.NAME AS COMPANY_NAME " +
                        "FROM CUSTOMER " +
                        "INNER JOIN CAR ON CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                        "INNER JOIN COMPANY ON CAR.COMPANY_ID = COMPANY.ID " +
                        "WHERE CUSTOMER.ID = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String carName = resultSet.getString("CAR_NAME");
                    String companyName = resultSet.getString("COMPANY_NAME");
                    System.out.println("Rented car: " + carName + " (Company: " + companyName + ")");
                } else {
                    System.out.println("You didn't rent a car!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnRentedCar(Connection connection, int customerId) {
        int rentedCarId = -1; // Initialize with a default value

        try (PreparedStatement statement = connection.prepareStatement("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    rentedCarId = resultSet.getInt("RENTED_CAR_ID");
                    if (rentedCarId == 0) {
                        System.out.println("You didn't rent a car!");
                        return;
                    }
                } else {
                    System.out.println("Customer not found!");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // If the customer has rented a car, proceed with returning it
        try (PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?")) {
            statement.setInt(1, customerId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("You've returned a rented car!");
            } else {
                System.out.println("Failed to return the rented car.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Mark the returned car as unavailable
        try {
            markCarUnavailable(connection, rentedCarId);
        } catch (SQLException e) {
            System.out.println("Error marking car as unavailable: " + e.getMessage());
        }
    }


    private static void markCarUnavailable(Connection connection, int carId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE CAR SET AVAILABLE = FALSE WHERE ID = ?")) {
            statement.setInt(1, carId);
            statement.executeUpdate();
        }
    }

    private static void markCarAvailable(Connection connection, int carId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE CAR SET AVAILABLE = TRUE WHERE ID = ?")) {
            statement.setInt(1, carId);
            statement.executeUpdate();
        }
    }

    private static int getCustomerIdByIndex(Connection connection, int index) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID FROM CUSTOMER ORDER BY ID")) {
            int currentIndex = 1;
            while (resultSet.next()) {
                if (currentIndex == index) {
                    return resultSet.getInt("ID");
                }
                currentIndex++;
            }
        }
        return -1;
    }

    private static String getCustomerName(Connection connection, int customerId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT NAME FROM CUSTOMER WHERE ID = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("NAME");
                }
            }
        }
        return "";
    }

    private static void createCustomer(Connection connection) {
        try {
            System.out.println("Enter the customer name:");
            String customerName = readString();

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO CUSTOMER (NAME) VALUES (?)")) {
                statement.setString(1, customerName);
                statement.executeUpdate();
                System.out.println("The customer was added!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void rentCar(Connection connection, int customerId) {
        try {
            // Check if the customer has already rented a car
            if (hasRentedCar(connection, customerId)) {
                System.out.println("You've already rented a car!");
                return;
            }

            // Get the list of available companies
            ArrayList<Integer> availableCompanies = getAvailableCompanies(connection);

            // Check if there are any available companies
            if (availableCompanies.isEmpty()) {
                System.out.println("No available companies to rent a car from.");
                return;
            }

            // Print the list of available companies
            System.out.println("Choose a company:");
            for (int i = 0; i < availableCompanies.size(); i++) {
                int companyId = availableCompanies.get(i);
                String companyName = getCompanyName(connection, companyId);
                System.out.println((i + 1) + ". " + companyName);
            }
            System.out.println("0. Back");

            int companyChoice = readInt();
            if (companyChoice == 0) {
            } else if (companyChoice >= 1 && companyChoice <= availableCompanies.size()) {
                int companyId = availableCompanies.get(companyChoice - 1);

                // Get the list of available cars for the chosen company
                ArrayList<Integer> availableCars = getAvailableCars(connection, companyId);

                // Check if there are any available cars for the chosen company
                if (availableCars.isEmpty()) {
                    System.out.println("No available cars in the chosen company.");
                    return;
                }

                // Print the list of available cars for the chosen company
                System.out.println("Choose a car:");
                for (int i = 0; i < availableCars.size(); i++) {
                    int carId = availableCars.get(i);
                    String carName = getCarName(connection, carId);
                    System.out.println((i + 1) + ". " + carName);
                }
                System.out.println("0. Back");

                int carChoice = readInt();
                if (carChoice == 0) {
                } else if (carChoice >= 1 && carChoice <= availableCars.size()) {
                    int carId = availableCars.get(carChoice - 1);

                    // Update the rented_car_id of the customer to the chosen car
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?")) {
                        statement.setInt(1, carId);
                        statement.setInt(2, customerId);
                        statement.executeUpdate();
                        System.out.println("You rented '" + getCarName(connection, carId) + "'.");
                    }

                    // Mark the car as unavailable
                    markCarUnavailable(connection, carId);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static boolean isCarRented(Connection connection, int carId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE RENTED_CAR_ID = ?")) {
            statement.setInt(1, carId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If any row is returned, it means the car is rented
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return true; // Assuming an error implies the car is rented to prevent unintentional rental
        }
    }

    private static boolean hasRentedCar(Connection connection, int customerId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RENTED_CAR_ID") != 0;
                }
            }
        }
        return false;
    }

    private static ArrayList<Integer> getAvailableCompanies(Connection connection) throws SQLException {
        ArrayList<Integer> availableCompanies = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID FROM COMPANY")) {
            while (resultSet.next()) {
                int companyId = resultSet.getInt("ID");
                if (!hasRentedCarFromCompany(connection, companyId)) {
                    availableCompanies.add(companyId);
                }
            }
        }
        return availableCompanies;
    }

    private static boolean hasRentedCarFromCompany(Connection connection, int companyId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE RENTED_CAR_ID IN (SELECT ID FROM CAR WHERE COMPANY_ID = ?)")) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private static ArrayList<Integer> getAvailableCars(Connection connection, int companyId) throws SQLException {
        ArrayList<Integer> availableCars = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT ID FROM CAR WHERE COMPANY_ID = ? AND AVAILABLE = TRUE"
        )) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    availableCars.add(resultSet.getInt("ID"));
                }
            }
        }
        return availableCars;
    }

    private static String getCarName(Connection connection, int carId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT NAME FROM CAR WHERE ID = ?")) {
            statement.setInt(1, carId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("NAME");
                }
            }
        }
        return "";
    }

    private static int readInt() {
        try {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private static String readString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void alterCarTableAddAvailableColumn(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "ALTER TABLE CAR ADD COLUMN IF NOT EXISTS AVAILABLE BOOLEAN DEFAULT TRUE";
            statement.executeUpdate(sql);
        }
    }
}
