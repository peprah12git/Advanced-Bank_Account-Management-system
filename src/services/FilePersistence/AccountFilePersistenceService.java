package services.FilePersistence;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import models.*;

/**
 * Service for file persistence operations.
 *  Save Accounts to File - Write account data to accounts.txt on exit.
 *  Load Accounts on Startup - Read data using Files.lines() and map each line to an object using method references.
 */
public class AccountFilePersistenceService {

    // Use a more reliable path that works regardless of execution context
    private static final String ACCOUNTS_FILE = "src/data/accounts.txt";
    private static final String DELIMITER = ",";
    private static final int EXPECTED_FIELD_COUNT = 10;

    /**
     * Gets the accounts file path, ensuring the directory exists.
     * Creates parent directories if they don't exist.
     */
    private static Path getAccountsFilePath() throws IOException {
        Path filePath = Paths.get(ACCOUNTS_FILE);
        // Ensure parent directories exist
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        return filePath;
    }

    /**
     * Saves all accounts to accounts.txt file on exit.
     * Uses BufferedWriter for efficient writing.
     * Format: accountNumber,customerName,customerId,customerAge,customerContact,
     *         customerAddress,customerType,balance,status,accountType
     *
     * @param accounts list of accounts to save
     * @throws IOException if file writing fails
     */
    public void saveAccountsToFile(List<Account> accounts) throws IOException {
        Path filePath = getAccountsFilePath();

        try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Account account : accounts) {
                String csvLine = accountToCSV(account);
                writer.write(csvLine);
                writer.newLine();
            }

            System.out.println("\n" + "=".repeat(50));
            System.out.println("SUCCESS: Saved " + accounts.size() + " accounts to file");
            System.out.println("File location: " + filePath.toAbsolutePath());
            System.out.println("=".repeat(50));
        }
    }

    /**
     * US-2.2: Loads accounts from accounts.txt file on startup.
     * Uses Files.lines() and maps each line to an object using method references.
     *
     * @return list of loaded accounts
     * @throws IOException if file reading fails
     */
    public List<Account> loadAccountsFromFile() throws IOException {
        Path filePath = getAccountsFilePath();

        if (!Files.exists(filePath)) {
            System.out.println("No existing accounts file found at: " + filePath.toAbsolutePath());
            System.out.println("Starting fresh.");
            return new ArrayList<>();
        }

        // US-2.2: Using Files.lines() and method reference to map lines to objects
        List<Account> accounts = Files.lines(filePath) // read lines as Stream<String>
                .filter(line -> !line.trim().isEmpty()) // Filter out/ ignore empty lines
                .map(this::csvToAccount)  // Method reference for mapping
                .filter(account -> account != null)
                .toList();  // or .collect(Collectors.toList()) for older Java

        System.out.println("Successfully loaded " + accounts.size() + " accounts from " + filePath.toAbsolutePath());
        return accounts;
    }

    /**
     * Converts an Account object to CSV string format.
     *
     * @param account the account to convert
     * @return CSV formatted string
     */
    private String accountToCSV(Account account) {
        Customer customer = account.getCustomer();
        return String.join(DELIMITER,
                account.getAccountNumber(),
                escapeCsvField(customer.getName()), // Escape if name contains comma
                customer.getCustomerId(), //
                String.valueOf(customer.getAge()),//
                escapeCsvField(customer.getContact()),//
                escapeCsvField(customer.getAddress()),
                customer.getCustomerType(),
                String.format("%.2f", account.getBalance()),
                account.getStatus(),
                account.getAccountType());
    }

    /**
     * Converts a CSV line to an Account object (method for mapping).
     *
     * @param line CSV formatted string
     * @return Account object or null if parsing fails
     */
    private Account csvToAccount(String line) {
        try {
            String[] parts = line.split(DELIMITER); // every comma creates a new array item

            if (parts.length < EXPECTED_FIELD_COUNT) {
                System.err.println("Invalid line format (expected " + EXPECTED_FIELD_COUNT + " fields): " + line);
                return null;
            }

            String accountNumber = parts[0].trim();
            String customerName = parts[1].trim();
            String customerId = parts[2].trim();
            int customerAge = Integer.parseInt(parts[3].trim());
            String customerContact = parts[4].trim();
            String customerAddress = parts[5].trim();
            String customerType = parts[6].trim();
            double balance = Double.parseDouble(parts[7].trim());
            String status = parts[8].trim();
            String accountType = parts[9].trim();

            // Create Customer object
            Customer customer = createCustomerByType(
                    customerName, customerAge, customerContact, customerAddress, customerType);

            if (customer == null) {
                System.err.println("Failed to create customer from line: " + line);
                return null;
            }

            // Create Account object
            return createAccountByType(customer, balance, status, accountType);

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in line: " + line);
            return null;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Factory method to create the appropriate Customer subclass.
     */
    private Customer createCustomerByType(String name, int age, String contact,
                                          String address, String customerType) {
        switch (customerType) {
            case "Premium":
            case "PremiumCustomer":
                return new PremiumCustomer(name, age, contact, address);

            case "Regular":
            case "RegularCustomer":
                return new RegularCustomer(name, age, contact, address);

            default:
                System.err.println("Unknown customer type: " + customerType + ". Using PremiumCustomer.");
                return new PremiumCustomer(name, age, contact, address);
        }
    }

    /**
     * Factory method to create the appropriate Account subclass.
     */
    private Account createAccountByType(Customer customer, double balance,
                                        String status, String accountType) {
        Account account;

        switch (accountType) {
            case "Savings":
            case "SavingsAccount":
                account = new SavingsAccount(customer, balance);
                break;

            case "Checking":
            case "CheckingAccount":
                account = new CheckingAccount(customer, balance);
                break;

            default:
                System.err.println("Unknown account type: " + accountType + ". Using SavingsAccount.");
                account = new SavingsAccount(customer, balance);
                break;
        }

        account.setStatus(status);
        return account;
    }

    /**
     * Escapes CSV field if it contains comma or special characters.
     */
    private String escapeCsvField(String field) {
        if (field.contains(DELIMITER)) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * Checks if accounts file exists.
     */
    public boolean accountsFileExists() {
        return Files.exists(Paths.get(ACCOUNTS_FILE));
    }

    /**
     * Deletes the accounts file.
     */
    public void deleteAccountsFile() throws IOException {
        Path filePath = Paths.get(ACCOUNTS_FILE);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            System.out.println("Accounts file deleted successfully.");
        }
    }
}