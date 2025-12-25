package services;
import exceptions.AccountNotFoundException;
import exceptions.ViewAccountException;
import models.Account;
import utils.InputReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {
   private ArrayList<Account>accounts = new ArrayList<>();
    private Map<String, Account> accountMap = new HashMap<>();
    private int accountCount;

    // Constructor
    public AccountManager() {
        this.accounts = new Account[50];  // capacity
        this.accountCount = 0;
    }

    // add account method
    public void addAccount(Account account) {
        if (accountCount < accounts.length) {
            accounts[accountCount] = account;
            accountCount++;
        } else {
            System.out.println("Account list is full.");
        }
    }

    //------------------ finding account method----------------
    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        // Loop through all Test.accounts in the manager
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equals(accountNumber)) {
                return accounts[i];
            }
        }
        // If no account matches, throw exception
        throw new AccountNotFoundException("Account not found: " + accountNumber);
    }

    // view all Test.accounts
    public void viewAllAccounts(InputReader inputReader) throws ViewAccountException {
        if (accountCount == 0) {
            throw new ViewAccountException(); // Throw exception instead of printing
        }

        for (int i = 0; i < accountCount; i++) {
            accounts[i].displayAccountDetails();
            System.out.println("--------------------------------");
        }
        inputReader.waitForEnter();
    }

    // sum of all balances
    public double getTotalBalance() {
        double total = 0;

        for (int i = 0; i < accountCount; i++) {
            total += accounts[i].getBalance();
        }

        return total;
    }

    // number of Test.accounts
    public int getAccountCount() {
        return accountCount;
    }
}