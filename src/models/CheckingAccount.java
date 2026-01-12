package models;

import exceptions.InvalidAmountException;
import exceptions.OverdraftLimitExceededException;

public class CheckingAccount extends Account {

    private double overdraftLimit;
    private double monthlyFee;

    // Constructor
    public CheckingAccount( Customer customer, double balance) {
        super(customer, balance);
        this.overdraftLimit = 1000;
        this.monthlyFee = 10.0;
    }

    public static void displayTableHeader() {
        System.out.println("ACCOUNT LISTING");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-7s | %-20s | %-10s | %12s | %s\n",
            "ACC NO",
            "CUSTOMER NAME",
            "TYPE",
            "BALANCE",
            "STATUS");
        System.out.println("---------------------------------------------------------------");
    }

    @Override
    public void displayAccountDetails() {
        System.out.printf("%-7s | %-22s | %-12s | %12s | %s\n",
            getAccountNumber(),
            getCustomer().getName(),
            "Checking",
            String.format("$%,.2f", getBalance()),
            getStatus());
        System.out.printf("%-7s | %s\n",
            "",
            String.format("Overdraft Limit: $%,.2f | Monthly Fee: $%.2f", overdraftLimit, monthlyFee));
        System.out.println("---------------------------------------------------------------------");
    }

    @Override
    public synchronized boolean withdraw(double amount) throws InvalidAmountException, OverdraftLimitExceededException {
        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero");
        }

        double allowedLimit = -overdraftLimit; // balance can go down to -1000

        // Check if withdrawal exceeds overdraft limit
        if (getBalance() - amount < allowedLimit) {
            throw new OverdraftLimitExceededException(
                    "Withdrawal denied! Exceeds overdraft limit of $" + overdraftLimit +
                            ". Current balance: $" + getBalance()
            );
        }

        //  Actually perform the withdrawal
        setBalance(getBalance() - amount);
        System.out.println("Withdrawal successful! New balance: $" + getBalance());
        return true;
    }

    @Override
    public boolean processTransaction(double amount, String type) throws InvalidAmountException {
        if (type.equalsIgnoreCase("Deposit")) {
            if (amount <= 0) {
                return false;
            }
            this.deposit(amount);
            return true;
        } else if (type.equalsIgnoreCase("Withdrawal")) {
            if (amount <= 0) {
                return false;
            }
            double oldBalance = this.getBalance();
            try {
                this.withdraw(amount);
            }catch (InvalidAmountException | OverdraftLimitExceededException e){
                return false;
            }
            return this.getBalance() != oldBalance;
        }
        return false;
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    public void applyMonthlyFee() {
        System.out.println("Monthly fee of $" + monthlyFee + " applied.");
        setBalance(getBalance() - monthlyFee);
    }
}