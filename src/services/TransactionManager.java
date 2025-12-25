package services;

import models.Transaction;
import utils.ConsoleTablePrinter;
import utils.InputReader;
import utils.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getType().equalsIgnoreCase(DEPOSIT_TYPE))
                .mapToDouble(Transaction::getAmount)
                .sum();
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

        String[] headers = createTransactionHeaders();
        String[][] data = buildTransactionData(transactions);

        printer.printTable(headers, data);
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

        List<Transaction> accountTransactions = transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());

        if (accountTransactions.isEmpty()) {
            System.out.println("No transactions recorded for account: " + accountNumber);
            inputReader.waitForEnter();
            return;
        }

        String[] headers = createTransactionHeaders();
        String[][] data = buildTransactionData(accountTransactions);

        printer.printTable(headers, data);

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
                .collect(Collectors.toList());
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
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private String[] createTransactionHeaders() {
        return new String[]{"TRANSACTION ID", "ACCOUNT NUMBER", "TYPE", "AMOUNT", "DATE"};
    }

    private String[][] buildTransactionData(List<Transaction> transactionList) {
        String[][] data = new String[transactionList.size()][5];

        for (int i = 0; i < transactionList.size(); i++) {
            Transaction tx = transactionList.get(i);
            data[i][0] = tx.getTransactionId();
            data[i][1] = tx.getAccountNumber();
            data[i][2] = tx.getType().toUpperCase();
            data[i][3] = formatAmount(tx.getType(), tx.getAmount());
            data[i][4] = tx.getTimestamp();
        }
        return data;
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
