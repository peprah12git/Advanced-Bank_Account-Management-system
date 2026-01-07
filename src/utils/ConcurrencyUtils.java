package utils;
import exceptions.InvalidAmountException;
import models.Account;
import exceptions.AccountNotFoundException;
import services.AccountManager;
import java.util.Random;


public class ConcurrencyUtils implements Runnable{
    private Account account;
    private String transactionType;

    public ConcurrencyUtils(Account account,String transactionType){
        this.account = account;
        this.transactionType = transactionType;
    }
    public void run(){
        double amount = new Random().nextDouble(1000);
        String message = transactionType.equalsIgnoreCase("Deposit") ?
                String.format("%s %sing %.2f to %s\n",Thread.currentThread().getName(),transactionType,amount,account.getAccountNumber()):
                String.format("%s %sing %.2f from %s\n",Thread.currentThread().getName(),transactionType,amount,account.getAccountNumber());
        System.out.printf(message);
        try {
            Thread.sleep(450);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            account.processTransaction(amount,transactionType);
        } catch (InvalidAmountException e) {
            throw new RuntimeException(e);
        }
    }

    public static void simulateConcurrentTransactions(InputReader inputReader) {
        try {
            String accountNumber = inputReader.readAccountNumber("Enter account number to simulate transactions: ");
            Account account = AccountManager.findAccount(accountNumber);

            System.out.println("\nSimulating 5 concurrent transactions on account: " + accountNumber);
            System.out.printf("Initial Balance: $%.2f\n\n", account.getBalance());

            Thread t1 = new Thread(new ConcurrencyUtils(account, "Deposit"));
            Thread t2 = new Thread(new ConcurrencyUtils(account, "Withdraw"));
            Thread t3 = new Thread(new ConcurrencyUtils(account, "Withdraw"));
            Thread t4 = new Thread(new ConcurrencyUtils(account, "Deposit"));
            Thread t5 = new Thread(new ConcurrencyUtils(account, "Deposit"));

            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();

            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();

            System.out.println("\nThread-safe operations completed successfully.");
            System.out.printf("Final Balance for %s: $%.2f\n", accountNumber, account.getBalance());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}