package utils;
import exceptions.InvalidAmountException;
import models.Account;
import exceptions.AccountNotFoundException;
import services.AccountManager;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

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

    public static void simulateConcurrentTransactions()  {
        try{
            Account account = AccountManager.findAccount("acc001");
            Thread t1 = new Thread(new ConcurrencyUtils(account,"Deposit"));
            Thread t2 = new Thread(new ConcurrencyUtils(account,"Withdraw"));
            Thread t3 = new Thread(new ConcurrencyUtils(account,"Withdraw"));
            Thread t4 = new Thread(new ConcurrencyUtils(account,"Deposit"));
            Thread t5 = new Thread(new ConcurrencyUtils(account,"Deposit"));
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
            System.out.println("Thread-safe opeartions completed successfully.");
            System.out.printf("Final Balance for ACC001: $%.2f",account.getBalance());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch ( AccountNotFoundException iae){
            System.out.println(iae.getMessage());
        }

    }
}