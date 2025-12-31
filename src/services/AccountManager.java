package services;

import exceptions.AccountNotFoundException;
import exceptions.ViewAccountException;
import models.Account;
import services.FilePersistence.AccountFilePersistenceService;
import utils.InputReader;
import utils.ValidationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountManager {
    private ArrayList<Account> accounts;
    private Map<String, Account> accountMap;
    private AccountFilePersistenceService persistenceService;

    // Constructor
    public AccountManager() {
        this.accounts = new ArrayList<>();
        this.accountMap = new HashMap<>();
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

            // Add each loaded account to both data structures
            for (Account account : loadedAccounts) {
                accounts.add(account);
                accountMap.put(account.getAccountNumber(), account);
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

        // Add to ArrayList for ordered storage
        accounts.add(account);

        // Add to HashMap for fast lookup by account number
        accountMap.put(account.getAccountNumber(), account);
    }

    // finding account method - O(1) lookup using HashMap
    // In AccountManager.java
    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        if (!ValidationUtils.isValidAccountNumber(accountNumber)) {
            throw new AccountNotFoundException(
                    "Invalid account number format. Expected: ACC### (e.g., ACC001)"
            );
        }

        Account account = accountMap.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return account;
    }

    // view all accounts
    public void viewAllAccounts(InputReader inputReader) throws ViewAccountException {
        if (accounts.isEmpty()) {
            throw new ViewAccountException();
        }

        for (Account account : accounts) {
            account.displayAccountDetails();
            System.out.println("--------------------------------");
        }
        inputReader.waitForEnter();
    }

    // sum of all balances
    public double getTotalBalance() {
        double total = 0;

        for (Account account : accounts) {
            total += account.getBalance();
        }

        return total;
    }

    // number of accounts
    public int getAccountCount() {
        return accounts.size();
    }

    // Check if account exists - O(1)
    public boolean accountExists(String accountNumber) {
        return accountMap.containsKey(accountNumber);
    }

    // Remove account (if needed in future)
    public boolean removeAccount(String accountNumber) {
        Account account = accountMap.remove(accountNumber);

        if (account != null) {
            accounts.remove(account);
            return true;
        }

        return false;
    }

    // Get all accounts as list
    public ArrayList<Account> getAllAccounts() {
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
        accountMap.clear();
        loadAccountsOnStartup();
    }
}