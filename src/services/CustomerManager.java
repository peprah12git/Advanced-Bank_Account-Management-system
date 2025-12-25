package services;

import models.Customer;
import utils.ConsoleTablePrinter;
import utils.InputReader;
import utils.TablePrinter;

/** Manages a collection of bank customers with fixed capacity. */
public class CustomerManager {

    private final Customer[] customers;
    private int customerCount;
    private final TablePrinter printer;
    private static final int MAX_CUSTOMERS = 100;

    public CustomerManager() {
        this.customers = new Customer[MAX_CUSTOMERS];
        this.customerCount = 0;
        this.printer = new ConsoleTablePrinter();
    }

    /** Adds a customer to the system if capacity allows. */
    public void addCustomer(Customer customer) {
        if (customerCount < MAX_CUSTOMERS) this.customers[this.customerCount++] = customer;
        else System.out.println("Customer limit reached.");
    }

    /**
     * Finds a customer by their unique ID.
     *
     * @param customerId the ID of the customer to find
     * @return the customer if found, or null if not found
     */
    public Customer findCustomer(String customerId) {
        for (int i = 0; i < this.customerCount; i++) {
            if (this.customers[i].getCustomerId().equals(customerId)) {
                return this.customers[i];
            }
        }
        return null;
    }

    /**
     * Displays a tabular view of all registered customers.
     *
     * @param inputReader used to pause execution after display
     */
    public void viewAllCustomers(InputReader inputReader) {
        String[] headers = {"CUSTOMER ID", "NAME", "TYPE", "AGE", "CONTACT", "ADDRESS"};

        if (customerCount == 0) {
            System.out.println("No customers available.");
            inputReader.waitForEnter();
            return;
        }

        String[][] data = buildTableData();

        printer.printTable(headers, data);

        System.out.println("Total Customers: " + customerCount);
        inputReader.waitForEnter();
    }

    // { "CUS001", "kofi", "Premium", "23", "23", "alsjdf" }
    //
    /** Constructs a 2D array of formatted customer data for tabular display. */
    private String[][] buildTableData() {
        return java.util.stream.IntStream.range(0, customerCount)
                .mapToObj(i -> customers[i])
                .map(
                        customer ->
                                new String[] {
                                        customer.getCustomerId(),
                                        customer.getName(),
                                        customer.getCustomerType(),
                                        String.valueOf(customer.getAge()),
                                        customer.getContact(),
                                        customer.getAddress()
                                })
                .toArray(String[][]::new);
    }

    public int getCustomerCount() {
        return customerCount;
    }
}