package models;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

public class SavingsAccount extends Account{

    // private fields
    private double interestRate;
    private double minimumBalance;

    // Setting constructor
    public SavingsAccount( Customer customer, double balance) {
        super( customer, balance);
        this.interestRate = 3.5;
        this.minimumBalance = 500;
    }

    public static void displayTableHeader() {
        System.out.println("ACCOUNT LISTING");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-7s | %-20s | %-10s | %12s | %s\n",
            "ACC NO",
            "CUSTOMER NAME",
            "TYPE",
            "BALANCE",
            "STATUS");
        System.out.println("-----------------------------------------------------------------");
    }

    @Override
    public void displayAccountDetails() {
        System.out.printf("%-7s | %-20s | %-10s | %12s | %s\n",
            getAccountNumber(),
            getCustomer().getName(),
            "Savings",
            String.format("$%,.2f", getBalance()),
            getStatus());
        System.out.printf("%-7s | %s\n",
            "",
            String.format("Interest Rate: %.1f%% | Min Balance: $%.2f", interestRate, minimumBalance));
        System.out.println("-----------------------------------------------------------------");
    }


    /**
     * Validates that a withdrawal amount does not breach the minimum balance requirement.
     *
     * @param amount
     * @return
     * @throws InsufficientFundsException when the withdrawal would result in a balance below the minimum
     * @throws InvalidAmountException
     */

    @Override
    public synchronized boolean withdraw(double amount)  throws InsufficientFundsException {
        if (getBalance() - amount < minimumBalance) {
            throw new InsufficientFundsException("Withdrawal denied! Balance cannot go below minimum: $ " + + minimumBalance);
//            System.out.println(" Withdrawal denied! Balance cannot go below minimum: $" + minimumBalance);
//            return false;
        }
        this.setBalance(getBalance() - amount);
        return true;  //  Return true on success
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
            } catch (InsufficientFundsException e) {
                System.err.println("Withdrawal failed: " + e.getMessage());
                return false;
            }
            return this.getBalance() != oldBalance;
        }
        return false;
    }


    @Override
    public String getAccountType() {
        return "Savings";
    }

    // Calculating interest method
    public double calculateInterest() {
        return (getBalance() + interestRate) / 100;
    }
// process t

}
