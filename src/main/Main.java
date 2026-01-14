package main;

import models.*;
import services.*;
import exceptions.*;
import utils.*;

import static utils.ConsoleInputReader.simulateConcurrency2;
import static utils.ConsoleInputReader.simulateConcurrency2;

public class Main {

    public static void main(String[] args) {
        System.out.println("+-------------------------\n| BANK ACCOUNT MANAGEMENT SYSTEM |\n+-------------------------+");

        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager();
        CustomerManager customerManager = new CustomerManager();

        try (ConsoleInputReader inputReader = new ConsoleInputReader()) {
            int choice;

            do {
                displayMainMenu();
                choice = inputReader.readInt("Enter your choice: ", 1, 9);
                handleMenuChoice(choice, accountManager, transactionManager, customerManager, inputReader);
            } while (choice != 9);
        }

        // Final save before exit
        System.out.println("\nFinalizing data persistence...");
        accountManager.saveAccountsToFile();
        transactionManager.saveTransactionsToFile();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Thank you for using Bank Account Management System!");
        System.out.println("All data has been saved successfully.");
        System.out.println("Goodbye!");
        System.out.println("=".repeat(50) + "\n");
    }


    // ========================================
    // MENU DISPLAY METHODS
    // ========================================

    private static void displayMainMenu() {
        System.out.println("\n+-----------+\n| MAIN MENU |\n+-----------+");
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. View Customers");
        System.out.println("4. Process Transaction");
        System.out.println("5. View All Transactions ");
        System.out.println("6. Generate Account Statement");
        System.out.println("7. Run Tests");
        System.out.println("8. Simulate Concurrent Transactions");
        System.out.println("9. Exit\n");
    }


    // ========================================
    // MENU ROUTING
    // ========================================

    private static void handleMenuChoice(
            int choice,
            AccountManager accountManager,
            TransactionManager transactionManager,
            CustomerManager customerManager,
            InputReader inputReader)  {

        switch (choice) {
            case 1 -> createAccount(accountManager, customerManager, inputReader);
            case 2 -> {
                try {
                    accountManager.viewAllAccounts(inputReader);
                }catch (ViewAccountException e){
                    System.out.println(e.getMessage());
                }

            }
            case 3 -> customerManager.viewAllCustomers(inputReader);
            case 4 -> processTransaction(accountManager, transactionManager, inputReader);
            case 5 -> transactionManager.viewAllTransactions(inputReader);
            case 6 -> generateBankStatement(accountManager, transactionManager, inputReader);
            case 7 ->  runTests(inputReader);
           // case 8 -> simulateConcurrency(inputReader, accountManager);
            case 8 -> simulateConcurrency2(inputReader,accountManager,transactionManager);
            case 9 ->{}
            default -> System.out.println("Invalid Input. Try Again!");
        }
    }


    // ========================================
    // ACCOUNT CREATION
    // ========================================

    private static void createAccount(
            AccountManager accountManager,
            CustomerManager customerManager,
            InputReader inputReader) {

        System.out.println("\n+------------------+\n| ACCOUNT CREATION |\n+------------------+");

        // Create customer
        Customer customer = createCustomer(inputReader);
        customerManager.addCustomer(customer);

        // Create account for customer
        Account account = createAccountForCustomer(inputReader, customer);

        // Display confirmation
        System.out.println("\n+--------------+\n| Confirmation |\n+--------------+");
        System.out.printf("Customer Name: %s\n", customer.getName());

        String customerType;
        if (customer instanceof RegularCustomer) {
            customerType = "Regular";
        } else {
            customerType = "Premium (Enhanced benefits, min balance $10,000)";
        }
        System.out.printf("Customer Type: %s\n", customerType);

        System.out.printf("Account Type: %s\n", account.getAccountType());
        System.out.printf("Initial Deposit: $%.2f\n", account.getBalance());

        // Confirm and save
        if (inputReader.readString("\nConfirm account creation? (y/n): ").toLowerCase().startsWith("y")) {
            accountManager.addAccount(account);
            System.out.println("Account Created Successfully!");
            accountManager.saveAccountsToFile();
            account.displayAccountDetails();
            customer.displayCustomerDetails();
        } else {
            System.out.println("Account creation cancelled.");
        }

        inputReader.waitForEnter();
    }

    private static Customer createCustomer(InputReader inputReader) {

        String name = inputReader.readString("Enter customer name: ");
        int age = inputReader.readInt("Enter customer age: ", 0, 150);
        String contact = inputReader.readContact("Enter customer contact: ");
        String address = inputReader.readString("Enter customer address: ");

        System.out.println("\nCustomer type:");
        System.out.println("1. Regular Customer (Standard banking services)");
        System.out.println("2. Premium Customer (Enhanced Benefits)");

        int customerType = inputReader.readInt("\nSelect type (1-2): ", 1, 2);

        if (customerType == 1) {
            return new RegularCustomer(name, age, contact, address);
        } else {
            return new PremiumCustomer(name, age, contact, address);
        }
    }


    private static Account createAccountForCustomer(InputReader inputReader, Customer customer) {
        System.out.println("\nAccount type:");
        System.out.println("1. Savings Account (Interest: 3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");

        int accountType = inputReader.readInt("\nSelect type (1-2): ", 1, 2);

        double minDeposit;
        if (customer instanceof PremiumCustomer) {
            minDeposit = 1000.0;
        } else {
            if (accountType == 1) {
                minDeposit = 500.0;
            } else {
                minDeposit = 0.0;
            }
        }

        double initialDeposit = inputReader.readDouble("\nEnter initial deposit amount: ", minDeposit);

        if (accountType == 1) {
            return new SavingsAccount(customer, initialDeposit);
        } else {
            return new CheckingAccount(customer, initialDeposit);
        }
    }


    // ========================================
    // TRANSACTION PROCESSING
    // ========================================

    private static void processTransaction(
            AccountManager accountManager,
            TransactionManager transactionManager,
            InputReader inputReader) {

        System.out.println("\n+---------------------+\n| PROCESS TRANSACTION |\n+---------------------+");
        String accountNumber = inputReader.readAccountNumber("\nEnter Account number: ");

        try {
            Account account = accountManager.findAccountByNumber(accountNumber);
           // account.displayAccountDetails();
            handleTransaction(account, transactionManager, accountManager, inputReader);
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
            inputReader.waitForEnter();
        }
    }

    private static void handleTransaction(
            Account account,
            TransactionManager transactionManager,
            AccountManager accountManager,
            InputReader inputReader) {

        int transactionType = promptTransactionType(inputReader);
        double amount = inputReader.readDouble("Enter amount: ", 0);

        Transaction transaction = buildTransaction(account, transactionType, amount);

        printTransactionConfirmation(transaction, account);

        if (confirm(inputReader)) {
            executeTransaction(account, transactionManager, accountManager, transaction);
        } else {
            System.out.println("Transaction cancelled.");
        }

        inputReader.waitForEnter();
    }

    private static int promptTransactionType(InputReader inputReader) {
        System.out.println("\nSelect Transaction Type:");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        return inputReader.readInt("Enter choice (1-2): ", 1, 2);
    }

    private static Transaction
    buildTransaction(Account account, int transactionType, double amount) {
        String type;
        if (transactionType == 1) {
            type = "DEPOSIT";
        } else {
            type = "WITHDRAWAL";
        }

        double newBalance;
        if (transactionType == 1) {
            newBalance = account.getBalance() + amount;
        } else {
            newBalance = account.getBalance() - amount;
        }

        return new Transaction(account.getAccountNumber(), type, amount, newBalance);
    }

    private static void printTransactionConfirmation(Transaction transaction, Account account) {
        System.out.println("\n+--------------------------+\n| Transaction Confirmation |\n+--------------------------+");
        System.out.printf("Transaction ID: %s\n", transaction.getTransactionId());
        System.out.printf("Account: %s\n", account.getAccountNumber());
        System.out.printf("Type: %s\n", transaction.getType());
        System.out.printf("Amount: $%.2f\n", transaction.getAmount());
        System.out.printf("Previous Balance: $%.2f\n", account.getBalance());
        System.out.printf("New Balance: $%.2f\n", transaction.getBalanceAfter());
        System.out.printf("Date/Time: %s\n", transaction.getTimestamp());
    }

    private static boolean confirm(InputReader inputReader) {
        return inputReader.readString("\nConfirm transaction? (y/n): ").toLowerCase().startsWith("y");
    }

    private static void executeTransaction(
            Account account,
            TransactionManager transactionManager,
            AccountManager accountManager,
            Transaction transaction) {

        try {
            double previousBalance = account.getBalance();

            account.processTransaction(transaction.getAmount(), transaction.getType());

            // Update transaction with actual new balance
            Transaction actualTransaction = new Transaction(
                    account.getAccountNumber(),
                    transaction.getType(),
                    transaction.getAmount(),
                    account.getBalance()  // Use the ACTUAL balance after processing
            );

            transactionManager.addTransaction(actualTransaction);

            transactionManager.saveTransactionsToFile();

            accountManager.saveAccountsToFile();  // IMPORTANT: Save updated account balance to file
            System.out.printf("%s Successful! New Balance: $%.2f\n",
                    transaction.getType(), account.getBalance());
        } catch (InvalidAmountException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================================
    // BANK STATEMENT
    // ========================================

    private static void generateBankStatement(
            AccountManager accountManager,
            TransactionManager transactionManager,
            InputReader inputReader) {

        System.out.println("\n+----------------+\n| ACCOUNT STATEMENT |\n+----------------+");
        String accountNumber = inputReader.readString("\nEnter Account number: ");

        try {
            Account account = accountManager.findAccountByNumber(accountNumber);
            account.displayAccountDetails();
            //displayTransactions(transactionManager.getTransactionsForAccount(accountNumber));
            displaySummary(transactionManager, accountNumber, account.getBalance());
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }

        inputReader.waitForEnter();
    }


    private static void displaySummary(
            TransactionManager transactionManager,
            String accountNumber,
            double balance) {

        Transaction[] transactions = transactionManager.getTransactionsForAccount(accountNumber);
        double totalDeposits = transactionManager.calculateTotalDepositsForAccount(accountNumber);
        double totalWithdrawals = transactionManager.calculateTotalWithdrawalsForAccount(accountNumber);
        double netChange = totalDeposits - totalWithdrawals;

        System.out.println("\n--- Transaction Summary ---");

        if (transactions.length == 0) {
            System.out.println("No transactions found.");
        } else {
            // Calculate starting balance
            double startingBalance = balance - netChange;
            double runningBalance = startingBalance;

            // Prepare table data with running balance
            String[][] tableData = new String[transactions.length][];
            for (int i = 0; i < transactions.length; i++) {
                Transaction t = transactions[i];
                double amount = t.getAmount();

                if (t.getType().equalsIgnoreCase("DEPOSIT")) {
                    runningBalance += amount;
                } else {
                    runningBalance -= amount;
                    amount = -amount; // Show withdrawals as negative
                }

                tableData[i] = new String[] {
                        t.getTransactionId(),
                        t.getTimestamp(),
                        t.getType(),
                        String.format("%s$%.2f", amount >= 0 ? "+" : "", amount),
                        String.format("$%,.2f", runningBalance)
                };
            }

            new ConsoleTablePrinter().printTable(
                    new String[] {"ID","DATE/TIME", "TYPE", "AMOUNT", "BALANCE"},
                    tableData
            );
        }

        System.out.println();
        System.out.printf("Net Change: %s$%.2f\n", netChange >= 0 ? "+" : "", netChange);
        System.out.println();
        System.out.println("✓ Statement generated successfully.");
    }

    /*
    TEST RUNNER
     */

    private static void runTests(InputReader inputReader) {
        System.out.println("Running tests with JUnit...");

        try {
            new CustomTestRunner().runTests();
        } catch (Exception e) {
            System.out.println("Failed to run tests: " + e.getMessage());
        }

        inputReader.waitForEnter();
    }
}