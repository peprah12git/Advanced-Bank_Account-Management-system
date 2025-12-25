package services;

import exceptions.AccountNotFoundException;
import exceptions.ViewAccountException;
import models.Account;
import utils.InputReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AccountManager {
    private final Map<String, Account> accountMap = new HashMap<>();

    // Constructor
    public AccountManager() {
        // HashMap handles capacity automatically
    }

    // Add account method - now uses HashMap for O(1) lookups
    public void addAccount(Account account) {
        accountMap.put(account.getAccountNumber(), account);
    }

    // Finding account method - using HashMap for instant lookup
    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        return Optional.ofNullable(accountMap.get(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    // View all accounts - using Stream for cleaner iteration
    public void viewAllAccounts(InputReader inputReader) throws ViewAccountException {
        if (accountMap.isEmpty()) {
            throw new ViewAccountException();
        }

        accountMap.values().forEach(account -> {
            account.displayAccountDetails();
            System.out.println("--------------------------------");
        });

        inputReader.waitForEnter();
    }

    // Sum of all balances - using Stream reduction
    public double getTotalBalance() {
        return accountMap.values().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    // Number of accounts
    public int getAccountCount() {
        return accountMap.size();
    }

    // Bonus: Get all accounts as a list (if needed elsewhere)
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accountMap.values());
    }

    // Bonus: Check if account exists
    public boolean accountExists(String accountNumber) {
        return accountMap.containsKey(accountNumber);
    }

    // Bonus: Remove account
    public boolean removeAccount(String accountNumber) {
        return accountMap.remove(accountNumber) != null;
    }

    // Bonus: Get accounts filtered by minimum balance
    public List<Account> getAccountsAboveBalance(double minBalance) {
        return accountMap.values().stream()
                .filter(account -> account.getBalance() >= minBalance)
                .toList();
    }
}