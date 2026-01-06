package services;

import models.Transaction;
import utils.ConsoleTablePrinter;
import utils.InputReader;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** Manages a collection of transactions with fixed capacity. */
public class TransactionManager {

    private static final int MAX_TRANSACTIONS = 200;
    private static final String DEPOSIT_TYPE = "DEPOSIT";
    private static final String WITHDRAWAL_TYPE = "WITHDRAWAL";

    private ArrayList<Transaction> transactions;
    private final TablePrinter printer;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
        this.printer = new ConsoleTablePrinter();
    }

    /** Adds a transaction to the history if capacity allows. */
    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            System.out.println("Attempted to add null transaction");
            return;
        }

        if (transactions.size() >= MAX_TRANSACTIONS) {
            System.out.println("Transaction limit reached. Cannot add more transactions.");
            return;
        }

        this.transactions.add(transaction);
    }

    /** Calculates the total amount of all deposits for a specific account. */
    public double calculateTotalDepositsForAccount(String accountNumber) {
        double total = 0;
        for (Transaction t : transactions) {
            if (t != null &&
                    t.getAccountNumber().equals(accountNumber) &&
                    t.getType().equalsIgnoreCase(DEPOSIT_TYPE)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    /** Calculates the total amount of withdrawals for a specific account. */
    public double calculateTotalWithdrawalsForAccount(String accountNumber) {
        return calculateTotalByTypeForAccount(accountNumber, WITHDRAWAL_TYPE);
    }

    /** Calculates the total amount of all deposits. */
    public double calculateTotalDeposits() {
        double total = 0;
        for (Transaction t : transactions) {
            if (t != null && t.getType().equalsIgnoreCase(DEPOSIT_TYPE)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    /** Calculates the total amount of all withdrawals. */
    public double calculateTotalWithdrawals() {
        return calculateTotalByType(WITHDRAWAL_TYPE);
    }

    public int getTransactionCount() {
        return this.transactions.size();
    }

    /**
     * Displays a tabular view of all transactions.
     *
     * @param inputReader used to pause execution after display
     */
    public void viewAllTransactions(InputReader inputReader) {
        if (isTransactionListEmpty(inputReader)) {
            return;
        }

        String[] headers = createTransactionHeaders();
        String[][] data = buildTransactionData(transactions);

        printer.printTable(headers, data);
        displayTransactionSummary(
                transactions.size(), calculateTotalDeposits(), calculateTotalWithdrawals());

        waitForUserInput(inputReader);
    }

    /**
     * Displays transactions for a specific account.
     *
     * @param accountNumber the account to filter by
     * @param inputReader used to pause execution after display
     */
    public void viewTransactionsByAccount(String accountNumber, InputReader inputReader) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Invalid account number provided");
            inputReader.waitForEnter();
            return;
        }

        ArrayList<Transaction> accountTransactions = filterTransactionsByAccount(accountNumber);

        if (accountTransactions.isEmpty()) {
            System.out.println("No transactions recorded for account: " + accountNumber);
            inputReader.waitForEnter();
            return;
        }

        String[] headers = createTransactionHeaders();
        String[][] data = buildTransactionData(accountTransactions);

        printer.printTable(headers, data);

        double totalDeposits = calculateTotalByTypeForAccount(accountNumber, DEPOSIT_TYPE);
        double totalWithdrawals = calculateTotalByTypeForAccount(accountNumber, WITHDRAWAL_TYPE);
        displayTransactionSummary(accountTransactions.size(), totalDeposits, totalWithdrawals);

        waitForUserInput(inputReader);
    }

    /** Returns all transactions for the specified account. */
    public Transaction[] getTransactionsForAccount(String accountNumber) {
        ArrayList<Transaction> filtered = filterTransactionsByAccount(accountNumber);
        return filtered.toArray(new Transaction[0]);
    }

    /** Returns total deposits for the specified account. */
    public double getTotalDeposits(String accountNumber) {
        return calculateTotalByTypeForAccount(accountNumber, DEPOSIT_TYPE);
    }

    /** Returns total withdrawals for the specified account. */
    public double getTotalWithdrawals(String accountNumber) {
        return calculateTotalByTypeForAccount(accountNumber, WITHDRAWAL_TYPE);
    }

    // ==================== HELPER METHODS ====================

    private double calculateTotalByType(String type) {
        double total = 0;
        for (Transaction t : transactions) {
            if (t != null && t.getType().equalsIgnoreCase(type)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    private ArrayList<Transaction> filterTransactionsByAccount(String accountNumber) {
        ArrayList<Transaction> filtered = new ArrayList<>();

        // Add in reverse order (most recent first)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t != null && t.getAccountNumber().equals(accountNumber)) {
                filtered.add(t);
            }
        }

        return filtered;
    }

    private String[] createTransactionHeaders() {
        return new String[] {"TRANSACTION ID", "ACCOUNT NUMBER", "TYPE", "AMOUNT", "DATE"};
    }

    private String[][] buildTransactionData(ArrayList<Transaction> transactionList) {
        String[][] data = new String[transactionList.size()][5];

        for (int i = 0; i < transactionList.size(); i++) {
            Transaction tx = transactionList.get(i);
            if (tx != null) {
                data[i][0] = tx.getTransactionId();
                data[i][1] = tx.getAccountNumber();
                data[i][2] = tx.getType().toUpperCase();
                data[i][3] = formatAmount(tx.getType(), tx.getAmount());
                data[i][4] = tx.getTimestamp();
            }
        }

        return data;
    }

    private String formatAmount(String type, double amount) {
        String prefix = type.equalsIgnoreCase(DEPOSIT_TYPE) ? "+$" : "-$";
        return String.format("%s%.2f", prefix, amount);
    }

    private void displayTransactionSummary(int count, double totalDeposits, double totalWithdrawals) {
        System.out.println("Number of transactions: " + count);
        System.out.println(String.format("Total Deposits: $%.2f", totalDeposits));
        System.out.println(String.format("Total Withdrawals: $%.2f", totalWithdrawals));
    }

    private double calculateTotalByTypeForAccount(String accountNumber, String type) {
        double total = 0;
        for (Transaction t : transactions) {
            if (t != null
                    && t.getAccountNumber().equals(accountNumber)
                    && t.getType().equalsIgnoreCase(type)) {
                total += t.getAmount();
            }
        }
        return total;
    }

    private boolean isTransactionListEmpty(InputReader inputReader) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            inputReader.waitForEnter();
            return true;
        }
        return false;
    }

    private void waitForUserInput(InputReader inputReader) {
        inputReader.waitForEnter();
    }

    // ==================== SORTING AND SEARCH METHODS ====================

    /**
     * Sorts transactions by date (timestamp) in ascending order.
     * @return sorted list of transactions
     */
    public List<Transaction> sortTransactionsByDate() {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }

    /**
     * Sorts transactions by date in descending order (most recent first).
     * @return sorted list of transactions
     */
    public List<Transaction> sortTransactionsByDateDescending() {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sorts transactions by amount in ascending order.
     * @return sorted list of transactions
     */
    public List<Transaction> sortTransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .collect(Collectors.toList());
    }

    /**
     * Sorts transactions by amount in descending order.
     * @return sorted list of transactions
     */
    public List<Transaction> sortTransactionsByAmountDescending() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Filters transactions by type (DEPOSIT or WITHDRAWAL).
     * @param type the transaction type to filter by
     * @return filtered list of transactions
     */
    public List<Transaction> filterTransactionsByType(String type) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Filters transactions by amount range.
     * @param minAmount minimum amount (inclusive)
     * @param maxAmount maximum amount (inclusive)
     * @return filtered list of transactions
     */
    public List<Transaction> filterTransactionsByAmountRange(double minAmount, double maxAmount) {
        return transactions.stream()
                .filter(t -> t.getAmount() >= minAmount && t.getAmount() <= maxAmount)
                .collect(Collectors.toList());
    }

    /**
     * Searches for transactions matching a specific account number and sorts by date.
     * @param accountNumber the account number to search for
     * @return sorted list of transactions for the account
     */
    public List<Transaction> searchAndSortByAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets transactions above a certain amount.
     * @param threshold the minimum amount
     * @return filtered list of transactions
     */
    public List<Transaction> getTransactionsAboveAmount(double threshold) {
        return transactions.stream()
                .filter(t -> t.getAmount() > threshold)
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets all deposits sorted by amount (descending).
     * @return sorted list of deposit transactions
     */
    public List<Transaction> getDepositsSortedByAmount() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(DEPOSIT_TYPE))
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets all withdrawals sorted by amount (descending).
     * @return sorted list of withdrawal transactions
     */
    public List<Transaction> getWithdrawalsSortedByAmount() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(WITHDRAWAL_TYPE))
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Displays transactions sorted by date.
     * @param inputReader used to pause execution after display
     */
    public void viewTransactionsSortedByDate(InputReader inputReader) {
        if (isTransactionListEmpty(inputReader)) {
            return;
        }

        List<Transaction> sorted = sortTransactionsByDateDescending();
        displayTransactionList(sorted, "Transactions Sorted by Date (Most Recent First)", inputReader);
    }

    /**
     * Displays transactions sorted by amount.
     * @param inputReader used to pause execution after display
     */
    public void viewTransactionsSortedByAmount(InputReader inputReader) {
        if (isTransactionListEmpty(inputReader)) {
            return;
        }

        List<Transaction> sorted = sortTransactionsByAmountDescending();
        displayTransactionList(sorted, "Transactions Sorted by Amount (Highest First)", inputReader);
    }

    /**
     * Helper method to display a list of transactions with a title.
     */
    private void displayTransactionList(List<Transaction> transactionList, String title, InputReader inputReader) {
        System.out.println("\n" + title);
        System.out.println("=".repeat(title.length()));

        String[] headers = createTransactionHeaders();
        String[][] data = buildTransactionData(new ArrayList<>(transactionList));

        printer.printTable(headers, data);

        double totalDeposits = transactionList.stream()
                .filter(t -> t.getType().equalsIgnoreCase(DEPOSIT_TYPE))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalWithdrawals = transactionList.stream()
                .filter(t -> t.getType().equalsIgnoreCase(WITHDRAWAL_TYPE))
                .mapToDouble(Transaction::getAmount)
                .sum();

        displayTransactionSummary(transactionList.size(), totalDeposits, totalWithdrawals);
        waitForUserInput(inputReader);
    }
}