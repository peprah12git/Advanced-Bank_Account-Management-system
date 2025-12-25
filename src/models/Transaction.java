package models;

import java.time.*;

public class Transaction {
    static int  transactionCounter;
    private  String transactionId;
    private  String accountNumber;
    private String type;
    private double amount;
    private  double balanceAfter;
    private String timestamp;

    public Transaction( String accountNumber, String type, double amount, double balanceAfter){
        this.accountNumber= accountNumber;
        this.type= type;
        this.amount = amount;
        this.balanceAfter= balanceAfter;
        this.timestamp = LocalDateTime.now().toString();
        this.transactionId = generate();

    }
    static String generate() {
        return  String.format("TXN%03d", ++transactionCounter);
    }

    public static int getTransactionCounter() {
        return transactionCounter;
    }

    public static void setTransactionCounter(int transactionCounter) {
        Transaction.transactionCounter = transactionCounter;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getTimestamp() {
        return  timestamp;
    }

    public  void  displayTransactionDetails(){
        System.out.printf(
                " %s | %s | %s | $%.2fd | $%.2f %n", transactionId, timestamp,type, amount, balanceAfter
        );;
    }
}
