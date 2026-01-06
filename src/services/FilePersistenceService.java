package services;

import models.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for file persistence operations.
 * Handles saving and loading account data from files.
 */
public class FilePersistenceService {

    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String DELIMITER = ",";

    /**
     * Saves all accounts to accounts.txt file.
     * Format: accountNumber,customerName,customerId,customerAge,customerContact,customerAddress,customerType,balance,status,accountType
     *
     * @param accounts list of accounts to save
     * @throws IOException if file writing fails
     */
    public void saveAccountsToFile(List<Account> accounts) throws IOException {
        Path filePath = Paths.get(ACCOUNTS_FILE);

        // Convert accounts to CSV lines using Streams and method references
        List<String> lines = accounts.stream()
                .map(this::accountToCSV)
                .collect(Collectors.toList());

        // Write all lines to file (overwrites existing file)
        Files.write(filePath, lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Successfully saved " + accounts.size() + " accounts to " + ACCOUNTS_FILE);
    }

    /**
     * Loads accounts from accounts.txt file on startup.
     *
     * @return list of loaded accounts
     * @throws IOException if file reading fails
     */
    public List<Account> loadAccountsFromFile() throws IOException {
        Path filePath = Paths.get(ACCOUNTS_FILE);

        // Check if file exists
        if (!Files.exists(filePath)) {
            System.out.println("No existing accounts file found. Starting fresh.");
            return new ArrayList<>();
        }

        // Read all lines and map each to an Account object using method reference
        List<Account> accounts = Files.lines(filePath)
                .filter(line -> !line.trim().isEmpty())
                .map(this::csvToAccount)
                .filter(account -> account != null)  // Filter out any parsing errors
                .collect(Collectors.toList());

        System.out.println("Successfully loaded " + accounts.size() + " accounts from " + ACCOUNTS_FILE);
        return accounts;
    }

    /**
     * Converts an Account object to CSV string format.
     * Format: accountNumber,customerName,customerId,customerAge,customerContact,customerAddress,customerType,balance,status,accountType
     *
     * @param account the account to convert
     * @return CSV formatted string
     */
    private String accountToCSV(Account account) {
        Customer customer = account.getCustomer();
        return String.join(DELIMITER,
                account.getAccountNumber(),
                customer.getName(),
                customer.getCustomerId(),
                String.valueOf(customer.getAge()),
                customer.getContact(),
                customer.getAddress(),
                customer.getCustomerType(),
                String.valueOf(account.getBalance()),
                account.getStatus(),
                account.getAccountType());
    }

    /**
     * Converts a CSV line to an Account object.
     *
     * @param line CSV formatted string
     * @return Account object or null if parsing fails
     */
    private Account csvToAccount(String line) {
        try {
            String[] parts = line.split(DELIMITER);

            if (parts.length < 10) {
                System.err.println("Invalid line format: " + line);
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

            // Create Customer object based on customer type
            Customer customer = createCustomerByType(customerName, customerAge,
                    customerContact, customerAddress, customerType);

            if (customer == null) {
                System.err.println("Failed to create customer for line: " + line);
                return null;
            }

            // Create appropriate account type based on the saved type
            return createAccountByType(customer, balance, status, accountType);

        } catch (Exception e) {
            System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Factory method to create the appropriate Customer subclass.
     *
     * @param name customer name
     * @param age customer age
     * @param contact customer contact
     * @param address customer address
     * @param customerType type of customer (Premium, Regular, etc.)
     * @return appropriate Customer subclass instance
     */
    private Customer createCustomerByType(String name, int age, String contact,
                                          String address, String customerType) {
        Customer customer = null;

        switch (customerType) {
            case "Premium":
            case "PremiumCustomer":
                customer = new PremiumCustomer(name, age, contact, address);
                break;
            case "Regular":
            case "RegularCustomer":
                customer = new RegularCustomer(name, age, contact, address);
                break;
            default:
                System.err.println("Unknown customer type: " + customerType + ". Defaulting to PremiumCustomer.");
                customer = new PremiumCustomer(name, age, contact, address);
                break;
        }

        return customer;
    }

    /**
     * Factory method to create the appropriate Account subclass.
     *
     * @param customer the customer object
     * @param balance account balance
     * @param status account status
     * @param accountType type of account (Savings, Checking, etc.)
     * @return appropriate Account subclass instance
     */
    private Account createAccountByType(Customer customer, double balance,
                                        String status, String accountType) {
        Account account = null;

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
                System.err.println("Unknown account type: " + accountType + ". Defaulting to SavingsAccount.");
                account = new SavingsAccount(customer, balance);
                break;
        }

        // Set status if account was created
        if (account != null) {
            account.setStatus(status);
        }

        return account;
    }

    /**
     * Checks if accounts file exists.
     *
     * @return true if file exists, false otherwise
     */
    public boolean accountsFileExists() {
        return Files.exists(Paths.get(ACCOUNTS_FILE));
    }

    /**
     * Deletes the accounts file.
     * Useful for testing or reset functionality.
     *
     * @throws IOException if deletion fails
     */
    public void deleteAccountsFile() throws IOException {
        Path filePath = Paths.get(ACCOUNTS_FILE);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            System.out.println("Accounts file deleted.");
        }
    }

    /**
     * Gets the accounts file path.
     *
     * @return the file path as a string
     */
    public String getAccountsFilePath() {
        return ACCOUNTS_FILE;
    }
}