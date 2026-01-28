package models;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import utils.ValidationUtils;

public abstract class Account {
    private String accountNumber;
    private Customer customer;
    private double balance;
    private String status;

    private static int accountCounter = 0;

    /**
     * Constructor
     */
    public Account(Customer customer, double balance) {
        this.accountNumber = generateAccountNumber();
        this.customer = customer;
        this.balance = balance;
        this.status = "Active";
    }

    /**
     * Generates unique account number in format ACC001, ACC002, etc.
     */
    private static String generateAccountNumber() {
        return String.format("ACC%03d", ++accountCounter);
    }

    /**
     * Gets the current account counter value
     */
    public static int getAccountCounter() {
        return accountCounter;
    }

    /**
     * Sets the account counter value (useful for testing)
     */
    public static void setAccountCounter(int counter) {
        accountCounter = counter;
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Deposits money into the account
    // * @param amount Amount to deposit
     * @throws InvalidAmountException if amount is invalid
     */
    public void deposit(double amount) throws InvalidAmountException {
        try {
            ValidationUtils.validateAmount(amount, "Deposit");
        } catch (IllegalArgumentException e) {
            throw new InvalidAmountException(e.getMessage());
        }
        this.balance += amount;
    }

    // Abstract methods to be implemented by subclasses
    public abstract void displayAccountDetails();

    public abstract String getAccountType();

    public abstract boolean withdraw(double amount) throws InsufficientFundsException, InvalidAmountException;

    public abstract boolean processTransaction(double amount, String type) throws InvalidAmountException;
}