package models;


public class RegularCustomer extends Customer {
    // contructor acepting name, age , contact and address
    public RegularCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
    }

    @Override
    public void displayCustomerDetails() {
        return;
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }
}
