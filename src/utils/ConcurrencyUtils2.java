package utils;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import models.Account;
import models.Transaction;
import services.AccountManager;
import services.TransactionManager;

public class ConcurrencyUtils2 {

    private static final Random random = new Random();

    public static void simulateConcurrentTransactions(InputReader inputReader, AccountManager accountManager, TransactionManager transactionManger) {
        // Validate: No accounts to process
        if (accountManager.getAllAccounts() == null || accountManager.getAllAccounts().isEmpty()) {
            System.out.println("No Accounts");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("     CONCURRENT TRANSACTION SIMULATION STARTED");
        System.out.println("=".repeat(60));
        
        // Get number of threads from user
        int numberOfThreads = inputReader.readInt("Enter  a number between 2 t0 10 to start concurrency simulation: ", 2, 10);
        System.out.println("\nCreating thread pool with " + numberOfThreads + " threads...");
        ExecutorService executer = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i + 1;
            executer.submit(() -> {
                randomTransaction(transactionManger, accountManager);
            });
        }
        System.out.println("\nAll threads submitted. Processing transactions...\n");
        executer.shutdown();
        
        // Wait for all threads to complete before showing completion message
        try {
            if (!executer.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Some threads did not complete within timeout");
                executer.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted while waiting: " + e.getMessage());
            executer.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     SIMULATION COMPLETE");
        System.out.println("=".repeat(60) + "\n");
        inputReader.waitForEnter();
    }

    public static void randomTransaction(TransactionManager transactionManager, AccountManager accountManager) {
        // Pick a random account
        Account account = accountManager.getAllAccounts().getFirst();
        // Generate random amount between 100 and 150
        double amount = (random.nextDouble() * 50) + (100);
        amount = Math.round(amount * 100.0) / 100.0;
// Randomly decide deposit or withdrawal
        boolean isDeposit = random.nextBoolean();
        String transactionType = isDeposit ? "DEPOSIT" : "WITHDRAWAL";
        System.out.println("[" + Thread.currentThread().getName() + "] Processing " + transactionType + " - Amount: $" + String.format("%.2f", amount));
        try {
            if (isDeposit) {
                synchronized (account) {
                    // Capture balance BEFORE deposit
                    double balanceBeforeDeposit = account.getBalance();
                    // Perform the actual deposit operation
                    account.deposit(amount);
                    // Create transaction with the new balance
                    Transaction transaction = new Transaction(account.getAccountNumber(), account.getAccountType(), amount, account.getBalance()); // new balance after deposit
                    transactionManager.addTransaction(transaction);
                    System.out.println("[" + Thread.currentThread().getName() + "] ✓ Deposit successful! New balance: $" + String.format("%.2f", account.getBalance()));
                }

            } else {
                synchronized (account) {
                    try {
                        // Capture balance BEFORE withdrawal
                        double balanceBeforeWithdrawal = account.getBalance();
                        // Perform the actual withdrawal operation
                        account.withdraw(amount);
                        // Create transaction with the new balance after withdrawal
                        Transaction transaction = new Transaction(account.getAccountNumber(), account.getAccountType(), amount, account.getBalance());
                        transactionManager.addTransaction(transaction);
                        System.out.println("[" + Thread.currentThread().getName() + "] ✓ Withdrawal successful! New balance: $" + String.format("%.2f", account.getBalance()));
                    } catch (InsufficientFundsException | InvalidAmountException e) {
                        System.out.println("[" + Thread.currentThread().getName() + "] ✗ Withdrawal failed: Insufficient funds");
                    }
                }
            }


        } catch (Exception e) {
            System.out.println("Invalid Amount");
        }
    }
}
