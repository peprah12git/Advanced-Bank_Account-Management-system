package customers;

import models.Customer;
import utils.ConsoleTablePrinter;
import utils.InputReader;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;

/** Manages a collection of bank customers using ArrayList for dynamic capacity. */
public class CustomerManager {

    private final List<Customer> customers;
    private final TablePrinter printer;

    public CustomerManager() {
        this.customers = new ArrayList<>();
        this.printer = new ConsoleTablePrinter();
    }

    /** Adds a customer to the system. */
    public void addCustomer(Customer customer) {
        if (customer != null) {
            this.customers.add(customer);
        } else {
            System.out.println("Cannot add null customer.");
        }
    }

    /**
     * Finds a customer by their unique ID.
     *
     * @param customerId the ID of the customer to find
     * @return the customer if found, or null if not found
     */
    public Customer findCustomer(String customerId) {
        return customers.stream()
                .filter(customer -> customer.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Displays a tabular view of all registered customers.
     *
     * @param inputReader used to pause execution after display
     */
    public void viewAllCustomers(InputReader inputReader) {
        String[] headers = {"CUSTOMER ID", "NAME", "TYPE", "AGE", "CONTACT", "ADDRESS"};

        if (customers.isEmpty()) {
            System.out.println("No customers available.");
            inputReader.waitForEnter();
            return;
        }

        String[][] data = buildTableData();

        printer.printTable(headers, data);

        System.out.println("Total Customers: " + customers.size());
        inputReader.waitForEnter();
    }

    /**
     * Constructs a 2D array of formatted customer data for tabular display.
     */
    private String[][] buildTableData() {
        return customers.stream()
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
        return customers.size();
    }
}