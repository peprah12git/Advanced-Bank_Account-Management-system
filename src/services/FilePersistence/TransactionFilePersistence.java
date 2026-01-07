package services.FilePersistence;

import models.Transaction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for transaction file persistence operations.
 * Handles saving and loading transaction data using Files.lines().
 *
 * CSV Format:
 * transactionId,type,amount,balance
 */
public class TransactionFilePersistence {

    private static final String TRANSACTIONS_FILE = "src/data/transactions.txt";
    private static final String DELIMITER = ",";
    private static final int EXPECTED_FIELD_COUNT = 4;

    /**
     * Saves all transactions to file.
     */
    public void saveTransactionsToFile(List<Transaction> transactions) throws IOException {
        Path filePath = Paths.get(TRANSACTIONS_FILE);

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            for (Transaction transaction : transactions) {
                writer.write(transactionToCSV(transaction));
                writer.newLine();
            }
        }
    }

    /**
     * Loads transactions from file.
     */
    public List<Transaction> loadTransactionsFromFile() throws IOException {
        Path filePath = Paths.get(TRANSACTIONS_FILE);

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        return Files.lines(filePath)
                .filter(line -> !line.trim().isEmpty())
                .map(this::csvToTransaction)
                .filter(t -> t != null)
                .toList();
    }

    /**
     * Converts a Transaction to CSV.
     */
    private String transactionToCSV(Transaction transaction) {
        return String.join(DELIMITER,
                transaction.getTransactionId(),
                transaction.getAccountNumber(),
                transaction.getType(),
                String.valueOf(transaction.getAmount()),
                String.valueOf(transaction.getBalanceAfter())
        );
    }

    /**
     * Converts a CSV line to a Transaction object.
     */
    private Transaction csvToTransaction(String line) {
        try {
            String[] parts = line.split(DELIMITER);

            if (parts.length < EXPECTED_FIELD_COUNT) {
                System.err.println("Invalid transaction format: " + line);
                return null;
            }

            return new Transaction(
                    parts[1].trim(),                       // transactionId
                    parts[2].trim(),                       // type
                    Double.parseDouble(parts[3].trim()),   // amount
                    Double.parseDouble(parts[4].trim())    // balance
            );

        } catch (NumberFormatException e) {
            System.err.println("Invalid number in transaction: " + line);
            return null;
        }
    }

    /**
     * Checks if transactions file exists.
     */
    public boolean transactionsFileExists() {
        return Files.exists(Paths.get(TRANSACTIONS_FILE));
    }

    /**
     * Deletes the transactions file.
     */
    public void deleteTransactionsFile() throws IOException {
        Path filePath = Paths.get(TRANSACTIONS_FILE);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    /**
     * Gets the transactions file path.
     */
    public String getTransactionsFilePath() {
        return TRANSACTIONS_FILE;
    }
}
