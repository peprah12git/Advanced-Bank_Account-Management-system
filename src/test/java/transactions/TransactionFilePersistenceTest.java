package transactions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import customers.FilePersistence.TransactionFilePersistence;
import models.Transaction;

/**
 * Test suite for TransactionFilePersistence.
 * Tests saving and loading transactions from file with various scenarios.
 */
@DisplayName("Transaction File Persistence Tests")
class TransactionFilePersistenceTest {

    private TransactionFilePersistence persistenceService;
    private List<Transaction> testTransactions;
    private static final String TEST_TRANSACTIONS_FILE = "src/data/transactions.txt";

    @BeforeEach
    void setUp() {
        persistenceService = new TransactionFilePersistence();
        testTransactions = new ArrayList<>();
        // Reset transaction counter for consistent testing
        Transaction.setTransactionCounter(0);
    }

    @AfterEach
    void tearDown() {
        // Clean up test files
        try {
            if (persistenceService.transactionsFileExists()) {
                persistenceService.deleteTransactionsFile();
            }
        } catch (IOException e) {
            System.err.println("Error cleaning up test file: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Save single transaction to file")
    void testSaveSingleTransaction() throws IOException {
        // Arrange - Create transaction using constructor (accountNumber, type, amount, balanceAfter)
        Transaction transaction = new Transaction("ACC001", "DEPOSIT", 5000.00, 5000.00);
        testTransactions.add(transaction);

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);

        // Assert
        Path filePath = Paths.get(TEST_TRANSACTIONS_FILE);
        assertTrue(Files.exists(filePath), "Transactions file should exist");
        assertTrue(Files.size(filePath) > 0, "Transactions file should not be empty");
        
        String content = Files.readString(filePath);
        assertTrue(content.contains("DEPOSIT"), "File should contain transaction type");
        assertTrue(content.contains("5000.00"), "File should contain transaction amount");
        assertTrue(content.contains("ACC001"), "File should contain account number");
    }

    @Test
    @DisplayName("Save multiple transactions to file")
    void testSaveMultipleTransactions() throws IOException {
        // Arrange
        testTransactions.add(new Transaction("ACC001", "DEPOSIT", 1000.00, 1000.00));
        testTransactions.add(new Transaction("ACC002", "WITHDRAWAL", 500.00, 500.00));
        testTransactions.add(new Transaction("ACC001", "TRANSFER", 250.00, 250.00));

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);

        // Assert
        Path filePath = Paths.get(TEST_TRANSACTIONS_FILE);
        assertTrue(Files.exists(filePath), "Transactions file should exist");
        
        String content = Files.readString(filePath);
        String[] lines = content.split("\n");
        assertEquals(3, lines.length, "File should contain 3 transaction lines");
        assertTrue(content.contains("DEPOSIT"), "File should contain deposit");
        assertTrue(content.contains("WITHDRAWAL"), "File should contain withdrawal");
        assertTrue(content.contains("TRANSFER"), "File should contain transfer");
    }

    @Test
    @DisplayName("Save empty transaction list to file")
    void testSaveEmptyTransactionList() throws IOException {
        // Act
        persistenceService.saveTransactionsToFile(new ArrayList<>());

        // Assert
        Path filePath = Paths.get(TEST_TRANSACTIONS_FILE);
        assertTrue(Files.exists(filePath), "Transactions file should exist even when empty");
        assertEquals(0, Files.size(filePath), "File should be empty");
    }

    @Test
    @DisplayName("Load transactions from non-existent file returns empty list")
    void testLoadTransactionsFromNonExistentFile() throws IOException {
        // Act
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertNotNull(loadedTransactions, "Loaded transactions list should not be null");
        assertTrue(loadedTransactions.isEmpty(), "Loaded transactions should be empty for non-existent file");
    }

    @Test
    @DisplayName("Load single transaction from file")
    void testLoadSingleTransaction() throws IOException {
        // Arrange - Save a transaction and then load it
        Transaction transaction = new Transaction("ACC005", "DEPOSIT", 7500.00, 7500.00);
        testTransactions.add(transaction);
        persistenceService.saveTransactionsToFile(testTransactions);

        // Act
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertNotNull(loadedTransactions, "Loaded transactions should not be null");
        assertEquals(1, loadedTransactions.size(), "Should load 1 transaction");
        
        Transaction loaded = loadedTransactions.get(0);
        assertEquals("ACC005", loaded.getAccountNumber(), "Account number should match");
        assertEquals("DEPOSIT", loaded.getType(), "Transaction type should match");
        assertEquals(7500.00, loaded.getAmount(), "Amount should match");
        assertNotNull(loaded.getTimestamp(), "Timestamp should not be null");
    }

    @Test
    @DisplayName("Load multiple transactions from file")
    void testLoadMultipleTransactions() throws IOException {
        // Arrange
        testTransactions.add(new Transaction("ACC001", "DEPOSIT", 2000.00, 2000.00));
        testTransactions.add(new Transaction("ACC002", "WITHDRAWAL", 1000.00, 1000.00));
        testTransactions.add(new Transaction("ACC003", "TRANSFER", 500.00, 500.00));
        
        persistenceService.saveTransactionsToFile(testTransactions);

        // Act
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertNotNull(loadedTransactions, "Loaded transactions should not be null");
        assertEquals(3, loadedTransactions.size(), "Should load 3 transactions");
        
        // Verify first transaction
        assertEquals("ACC001", loadedTransactions.get(0).getAccountNumber());
        assertEquals("DEPOSIT", loadedTransactions.get(0).getType());
        assertEquals(2000.00, loadedTransactions.get(0).getAmount());
        
        // Verify second transaction
        assertEquals("ACC002", loadedTransactions.get(1).getAccountNumber());
        assertEquals("WITHDRAWAL", loadedTransactions.get(1).getType());
        
        // Verify third transaction
        assertEquals("ACC003", loadedTransactions.get(2).getAccountNumber());
        assertEquals("TRANSFER", loadedTransactions.get(2).getType());
    }

    @Test
    @DisplayName("Round-trip: Save and load preserves transaction data")
    void testRoundTripSaveAndLoad() throws IOException {
        // Arrange
        Transaction t1 = new Transaction("ACC010", "DEPOSIT", 5000.50, 5000.50);
        Transaction t2 = new Transaction("ACC011", "WITHDRAWAL", 2000.25, 2000.25);
        
        testTransactions.add(t1);
        testTransactions.add(t2);

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(2, loadedTransactions.size(), "Should preserve all transactions");
        
        for (int i = 0; i < testTransactions.size(); i++) {
            Transaction original = testTransactions.get(i);
            Transaction loaded = loadedTransactions.get(i);
            
            assertEquals(original.getAccountNumber(), loaded.getAccountNumber(), "Account numbers should match");
            assertEquals(original.getType(), loaded.getType(), "Transaction types should match");
            assertEquals(original.getAmount(), loaded.getAmount(), "Amounts should match");
        }
    }

    @Test
    @DisplayName("Load file with empty lines should skip them")
    void testLoadFileWithEmptyLines() throws IOException {
        // Arrange
        Transaction transaction = new Transaction("ACC001", "DEPOSIT", 3000.00, 3000.00);
        testTransactions.add(transaction);
        
        persistenceService.saveTransactionsToFile(testTransactions);
        
        // Manually add empty lines to the file
        Path filePath = Paths.get(TEST_TRANSACTIONS_FILE);
        String content = Files.readString(filePath);
        Files.writeString(filePath, content + "\n\n");

        // Act
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(1, loadedTransactions.size(), "Should skip empty lines and load only valid transactions");
    }

    @Test
    @DisplayName("Transaction with various type formats")
    void testTransactionWithVariousTypes() throws IOException {
        // Arrange
        testTransactions.add(new Transaction("ACC001", "DEPOSIT", 1000.00, 1000.00));
        testTransactions.add(new Transaction("ACC002", "WITHDRAWAL", 500.00, 500.00));
        testTransactions.add(new Transaction("ACC003", "TRANSFER", 250.00, 250.00));
        testTransactions.add(new Transaction("ACC004", "INTEREST_PAYMENT", 50.00, 50.00));

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(4, loadedTransactions.size(), "Should save and load all transaction types");
        assertEquals("DEPOSIT", loadedTransactions.get(0).getType());
        assertEquals("WITHDRAWAL", loadedTransactions.get(1).getType());
        assertEquals("TRANSFER", loadedTransactions.get(2).getType());
        assertEquals("INTEREST_PAYMENT", loadedTransactions.get(3).getType());
    }

    @Test
    @DisplayName("Transaction amount with decimal precision")
    void testTransactionAmountPrecision() throws IOException {
        // Arrange
        Transaction transaction = new Transaction("ACC001", "DEPOSIT", 1234.56, 1234.56);
        testTransactions.add(transaction);

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(1234.56, loadedTransactions.get(0).getAmount(), 0.01, "Amount precision should be maintained");
    }


    @Test
    @DisplayName("Large number of transactions")
    void testLargeTransactionList() throws IOException {
        // Arrange - create 100 transactions
        for (int i = 1; i <= 100; i++) {
            String type = (i % 3 == 0) ? "DEPOSIT" : (i % 3 == 1) ? "WITHDRAWAL" : "TRANSFER";
            testTransactions.add(new Transaction(
                "ACC" + String.format("%03d", (i % 100) + 1),
                type,
                1000.00 + i,
                1000.00 + i
            ));
        }

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(100, loadedTransactions.size(), "Should save and load 100 transactions");
    }

    @Test
    @DisplayName("Multiple saves should overwrite previous data")
    void testMultipleSavesOverwriteData() throws IOException {
        // Arrange - First save
        testTransactions.add(new Transaction("ACC001", "DEPOSIT", 1000.00, 1000.00));
        persistenceService.saveTransactionsToFile(testTransactions);

        // Clear and save again with different data
        testTransactions.clear();
        testTransactions.add(new Transaction("ACC002", "WITHDRAWAL", 500.00, 500.00));

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(1, loadedTransactions.size(), "Should contain only the last saved transaction");
        assertEquals("ACC002", loadedTransactions.get(0).getAccountNumber());
        assertEquals(500.00, loadedTransactions.get(0).getAmount());
    }

    @Test
    @DisplayName("File exists check method")
    void testTransactionsFileExistsMethod() throws IOException {
        // Arrange
        assertFalse(persistenceService.transactionsFileExists(), "File should not exist initially");

        // Act
        testTransactions.add(new Transaction("ACC001", "DEPOSIT", 1000.00, 1000.00));
        persistenceService.saveTransactionsToFile(testTransactions);

        // Assert
        assertTrue(persistenceService.transactionsFileExists(), "File should exist after saving");
    }

    @Test
    @DisplayName("Delete transactions file method")
    void testDeleteTransactionsFile() throws IOException {
        // Arrange
        testTransactions.add(new Transaction("ACC001", "DEPOSIT", 1000.00, 1000.00));
        persistenceService.saveTransactionsToFile(testTransactions);
        assertTrue(persistenceService.transactionsFileExists(), "File should exist after saving");

        // Act
        persistenceService.deleteTransactionsFile();

        // Assert
        assertFalse(persistenceService.transactionsFileExists(), "File should not exist after deletion");
    }

    @Test
    @DisplayName("Get transactions file path method")
    void testGetTransactionsFilePath() {
        // Act
        String filePath = persistenceService.getTransactionsFilePath();

        // Assert
        assertNotNull(filePath, "File path should not be null");
        assertEquals("src/data/transactions.txt", filePath, "File path should match expected value");
    }

    @Test
    @DisplayName("Sequential transactions with incrementing amounts")
    void testSequentialTransactionsWithIncrementingAmounts() throws IOException {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            testTransactions.add(new Transaction(
                "ACC001",
                "DEPOSIT",
                1000.00 * i,
                1000.00 * i
            ));
        }

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(10, loadedTransactions.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(1000.00 * (i + 1), loadedTransactions.get(i).getAmount(), 
                "Amount should match index: " + (i + 1));
        }
    }

    @Test
    @DisplayName("Transactions from multiple accounts")
    void testTransactionsFromMultipleAccounts() throws IOException {
        // Arrange
        for (int i = 1; i <= 10; i++) {
            testTransactions.add(new Transaction(
                "ACC" + String.format("%03d", i),
                "DEPOSIT",
                1000.00 * i,
                1000.00 * i
            ));
        }

        // Act
        persistenceService.saveTransactionsToFile(testTransactions);
        List<Transaction> loadedTransactions = persistenceService.loadTransactionsFromFile();

        // Assert
        assertEquals(10, loadedTransactions.size());
        for (int i = 0; i < 10; i++) {
            assertEquals("ACC" + String.format("%03d", i + 1), loadedTransactions.get(i).getAccountNumber(),
                "Account number should match index: " + (i + 1));
        }
    }
}

