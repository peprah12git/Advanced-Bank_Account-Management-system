package services;

import models.Transaction;
import utils.ConsoleTablePrinter;
import utils.InputReader;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/** Manages a collection of transactions using ArrayList and Streams. */
public class TransactionManager {

    private static final String DEPOSIT_TYPE = "DEPOSIT";
    private static final String WITHDRAWAL_TYPE = "WITHDRAWAL";

    private final List<Transaction> transactions;
    private final TablePrinter printer;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
        this.printer = new ConsoleTablePrinter();
    }

    /** Adds a transaction to the history. */
    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            System.out.println("Attempted to add null transaction");
            return;
        }
        transactions.add(transaction);
    }

    /** Calculates total deposits for a specific account. */
    public double calculateTotalDepositsForAccount(String accountNumber) {
        return calculateTotalByTypeForAccount(accountNumber, DEPOSIT_TYPE);
    }

    /** Calculates total withdrawals for a specific account. */
    public double calculateTotalWithdrawalsForAccount(String accountNumber) {
        return calculateTotalByTypeForAccount(accountNumber, WITHDRAWAL_TYPE);
    }

    /** Calculates total deposits across all accounts. */
    public double calculateTotalDeposits() {
        return calculateTotalByType(DEPOSIT_TYPE);
    }

    /** Calculates total withdrawals across all accounts. */
    public double calculateTotalWithdrawals() {
        return calculateTotalByType(WITHDRAWAL_TYPE);
    }

    public int getTransactionCount() {
        return transactions.size();
    }

    /** Displays all transactions. */
    public void viewAllTransactions(InputReader inputReader) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            inputReader.waitForEnter();
            return;
        }

        displayTransactionTable(transactions);
        displayTransactionSummary(
                transactions.size(),
                calculateTotalDeposits(),
                calculateTotalWithdrawals()
        );

        inputReader.waitForEnter();
    }

    /** Displays transactions for a specific account. */
    public void viewTransactionsByAccount(String accountNumber, InputReader inputReader) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Invalid account number provided");
            inputReader.waitForEnter();
            return;
        }

        List<Transaction> accountTransactions = getTransactionsForAccount(accountNumber);

        if (accountTransactions.isEmpty()) {
            System.out.println("No transactions recorded for account: " + accountNumber);
            inputReader.waitForEnter();
            return;
        }

        displayTransactionTable(accountTransactions);

        double totalDeposits = calculateTotalDepositsForAccount(accountNumber);
        double totalWithdrawals = calculateTotalWithdrawalsForAccount(accountNumber);
        displayTransactionSummary(
                accountTransactions.size(),
                totalDeposits,
                totalWithdrawals
        );

        inputReader.waitForEnter();
    }

    /** Returns transactions for an account. */
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .toList();
    }

    // ==================== HELPER METHODS ====================

    private double calculateTotalByType(String type) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private double calculateTotalByTypeForAccount(String accountNumber, String type) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber) &&
                        t.getType().equalsIgnoreCase(type))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private void displayTransactionTable(List<Transaction> transactionList) {
        String[] headers = createTransactionHeaders();
        String[][] data = buildTransactionData(transactionList);
        printer.printTable(headers, data);
    }

    private String[] createTransactionHeaders() {
        return new String[]{"TRANSACTION ID", "ACCOUNT NUMBER", "TYPE", "AMOUNT", "DATE"};
    }

    private String[][] buildTransactionData(List<Transaction> transactionList) {
        return IntStream.range(0, transactionList.size())
                .mapToObj(i -> buildTransactionRow(transactionList.get(i)))
                .toArray(String[][]::new);
    }

    private String[] buildTransactionRow(Transaction tx) {
        return new String[]{
                tx.getTransactionId(),
                tx.getAccountNumber(),
                tx.getType().toUpperCase(),
                formatAmount(tx.getType(), tx.getAmount()),
                tx.getTimestamp()
        };
    }

    private String formatAmount(String type, double amount) {
        String prefix = type.equalsIgnoreCase(DEPOSIT_TYPE) ? "+$" : "-$";
        return String.format("%s%.2f", prefix, amount);
    }

    private void displayTransactionSummary(int count, double totalDeposits, double totalWithdrawals) {
        System.out.println("Number of transactions: " + count);
        System.out.printf("Total Deposits: $%.2f%n", totalDeposits);
        System.out.printf("Total Withdrawals: $%.2f%n", totalWithdrawals);
    }
}