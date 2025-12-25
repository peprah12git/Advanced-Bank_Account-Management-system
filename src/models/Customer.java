package models;

public abstract class   Customer {
    private static int customerCounter = 0;
    private final String customerId;
    private String name;
    private int age;
    private String contact;
    private String address;

    // Setting constructor
    Customer( String name, int age, String contact, String address) {
        this.customerId = generatedCustomerID();
        this.name = name;
        this.age = age;
        this.address = address;
        this.contact = contact;
        customerCounter++;
    }
    private static String generatedCustomerID(){
        return "CUS" + customerCounter ++;
    }

    // getters Customers
    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public int getAge(){
        return age;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    // setter for Test.customer
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Abstract methods
    public abstract void displayCustomerDetails();

    public abstract String getCustomerType();

}
