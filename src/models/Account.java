package models;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

public abstract class Account {
    private String accountNumber;
    private Customer customer;
    private double balance;
    private String status;

    private static int accountCounter= 0;

    // Contructor
    public Account( Customer customer, double balance) {
        this.accountNumber = generate() ;
        this.customer = customer;
        this.balance = balance;
        this.status = "Active";
        //this.accountNumber= accountCounter;
    }

    static String generate() {
        return  String.format("ACC%03d", ++accountCounter);
    }

    // Getters for account number
    public String getAccountNumber() {
        return accountNumber;
    }
    // getter for Test.customer
    public Customer getCustomer() {
        return customer;
    }
    // setter for Test.customer
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    //-------getter for balance------
    public double getBalance() {
        return balance;
    }
    //-------getter for Status------
    public String getStatus() {
        return status;
    }

    // setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    // Setters for balance
    public void setBalance(double balance) {
        this.balance = balance;  //  Actually update the field
    }
    public abstract void displayAccountDetails();
    public abstract String getAccountType();

    // -------------Deposit money--------------------
    /**
     * @param amount
     * @throws InvalidAmountException
     */
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount can not be zero");
        } this.balance+=amount;
    }

    /**
     *
     * @param amount
     * @return
     * @throws InsufficientFundsException
     * @throws InvalidAmountException
     */
    // Withdraw Money
//    public  boolean withdraw(double amount) throws InsufficientFundsException ,InvalidAmountException{
//        if (amount > balance){
//            throw new InsufficientFundsException("Insufficient funds! current balance is " + getBalance());
//        } if (amount <= balance){
//            throw new InvalidAmountException("Withdrawal must be greater done Zero is ");
//        }
//            balance-=amount;
//        return true;
//    }
    public abstract boolean withdraw(double amount) throws InsufficientFundsException, InvalidAmountException;

    public abstract boolean processTransaction(double amount, String type) throws InvalidAmountException;

}
