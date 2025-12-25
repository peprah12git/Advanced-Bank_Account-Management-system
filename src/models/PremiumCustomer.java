package models;

public class PremiumCustomer extends Customer {

    private double minimumBalance;

    public PremiumCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
        this.minimumBalance = 1000.0;
    }

    // getter for minimum balance
    public double getMinimumBalance() {
        return minimumBalance;
    }
    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void displayCustomerDetails() {
        // show Test.customer info + premium benefits
    }

    @Override
    public String getCustomerType() {
        return  "Premium";
    }

    public boolean  hasWaivedFees(){
        return true;
    };
}
