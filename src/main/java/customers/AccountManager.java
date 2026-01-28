package customers;

import exceptions.AccountNotFoundException;
import exceptions.ViewAccountException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.Account;
import customers.FilePersistence.AccountFilePersistenceService;
import utils.InputReader;
import utils.ValidationUtils;

public class AccountManager {
    private List<Account> accounts;
    private AccountFilePersistenceService persistenceService;

    // Constructor
    public AccountManager() {
        this.accounts = new ArrayList<>();
        this.persistenceService = new AccountFilePersistenceService();

        // Load accounts on startup
        loadAccountsOnStartup();
    }

    /**
     * Loads accounts from file on startup.
     */
    private void loadAccountsOnStartup() {
        try {
            List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

            // Add each loaded account to the list
            for (Account account : loadedAccounts) {
                accounts.add(account);
            }

        } catch (IOException e) {
            System.err.println("Error loading accounts from file: " + e.getMessage());
            System.out.println("Starting with empty account list.");
        }
    }

    // add account method
    public void addAccount(Account account) {
        if (account == null) {
            System.out.println("Cannot add null account");
            return;
        }

        // Add to List for ordered storage
        accounts.add(account);
    }

    // finding account method - Uses Stream API with filter
    public Account findAccountByNumber(String accountNumber) throws AccountNotFoundException {
        if (!ValidationUtils.isValidAccountNumber(accountNumber)) {
            throw new AccountNotFoundException(
                    "Invalid account number format. Expected: ACC### (e.g., ACC001)"
            );
        }

        Account account = accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber)) // Filter by account number
                .findFirst() // Get first match
                .orElse(null); // Return null if not found

        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    // Legacy static method for backward compatibility
    public static Account findAccount(String accountNumber) throws AccountNotFoundException {
        throw new AccountNotFoundException("Use findAccountByNumber() instead - instance method required.");
    }

    // view all accounts
    public void viewAllAccounts(InputReader inputReader) throws ViewAccountException {
        if (accounts.isEmpty()) {
            throw new ViewAccountException();
        }

        // Display table header
        if (!accounts.isEmpty()) {
            Account firstAccount = accounts.get(0);
            if (firstAccount instanceof models.SavingsAccount) {
                models.SavingsAccount.displayTableHeader();
            } else if (firstAccount instanceof models.CheckingAccount) {
                models.CheckingAccount.displayTableHeader();
            }
        }

        for (Account account : accounts) {
            account.displayAccountDetails();
        }
        inputReader.waitForEnter();
    }

    // sum of all balances
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(Account::getBalance) // Map each account to its balance
                .sum();
    }

    // number of accounts
    public int getAccountCount() {
        return accounts.size();
    }

    // Check if account exists - Uses Stream API with anyMatch
    public boolean accountExists(String accountNumber) {
        return accounts.stream()
                .anyMatch(a -> a.getAccountNumber().equals(accountNumber));
    }

    // Remove account (if needed in future)
    public boolean removeAccount(String accountNumber) {
        return accounts.removeIf(account -> account.getAccountNumber().equals(accountNumber));
    }

    // Get all accounts as list
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    /**
     * Saves all accounts to file.
     * Call this method on application exit.
     */
    public void saveAccountsToFile() {
        try {
            persistenceService.saveAccountsToFile(accounts);
        } catch (IOException e) {
            System.err.println("Error saving accounts to file: " + e.getMessage());
        }
    }

    /**
     * Reloads accounts from file.
     * Useful for refreshing data.
     */
    public void reloadAccountsFromFile() {
        accounts.clear();
        loadAccountsOnStartup();
    }
}