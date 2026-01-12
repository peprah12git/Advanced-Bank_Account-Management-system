package utils;

import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import models.Account;
import models.Transaction;
import services.AccountManager;
import services.TransactionManager;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyUtils2 {

    private static final Random random = new Random();

    public static void simulateConcurrentTransactions(InputReader inputReader, AccountManager accountManager, TransactionManager transactionManger) {
        if (accountManager.getAllAccounts() == null || accountManager.getAllAccounts().isEmpty()) {
            System.out.println("No Accounts");
            return;
        }

        int numberOfThreads = inputReader.readInt("Enter  a number between 2 t0 10 to start concurrency simulation", 2, 10);
        ExecutorService executer = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i + 1;
            executer.submit(() -> {
                randomTransaction(transactionManger, accountManager);

            });
        }
        executer.shutdown();
        inputReader.waitForEnter();
    }

    public static void randomTransaction(TransactionManager transactionManager, AccountManager accountManager) {
        Account account = accountManager.getAllAccounts().getFirst();
        double amount = (random.nextDouble() * 50) + (100);
        amount = Math.round(amount * 100.0) / 100.0;

        boolean isDeposit = random.nextBoolean();
        System.out.println(" this is my deposit" + isDeposit);
        try {
            if (isDeposit) {
                synchronized (account) {
                    Transaction transaction = new Transaction(account.getAccountNumber(), account.getAccountType(), amount, account.getBalance());
                    transactionManager.addTransaction(transaction);

                }

            } else {
                synchronized (account) {
                    try {
                        account.withdraw(amount);
                        Transaction transaction = new Transaction(account.getAccountNumber(), account.getAccountType(), amount, account.getBalance());
                        transactionManager.addTransaction(transaction);
                    } catch (InsufficientFundsException | InvalidAmountException e) {
                        System.out.println("Insufficient amount");
                    }

                }
            }


        } catch (Exception e) {
            System.out.println("Invalid Amount");
        }



    }
}
