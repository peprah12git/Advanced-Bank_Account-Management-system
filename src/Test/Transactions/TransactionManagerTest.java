package Test.Transactions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.Transaction;
import services.TransactionManager;

/** Unit tests for TransactionManager including add, totals, and capacity limit operations. */
class TransactionManagerTest {

    private TransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        transactionManager = new TransactionManager();
    }

    @Test
    void testAddTransaction() {
        Transaction transaction = new Transaction("ACC001", "DEPOSIT", 100.0, 100.0);
        transactionManager.addTransaction(transaction);
        assertEquals(1, transactionManager.getTransactionCount());
    }

    @Test
    void testAddNullTransaction() {
        transactionManager.addTransaction(null);
        assertEquals(0, transactionManager.getTransactionCount());
    }

    @Test
    void testCalculateTotalDeposits() {
        transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 100.0, 100.0));
        transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 50.0, 150.0));
        transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 30.0, 120.0));

        assertEquals(150.0, transactionManager.calculateTotalDepositsForAccount("ACC001"));
    }


    @Test
    void testCalculateTotalWithdrawals() {
        transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 100.0, 100.0));
        transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 20.0, 80.0));
        transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 30.0, 50.0));

        assertEquals(50.0, transactionManager.calculateTotalWithdrawals());
    }

    @Test
    void testTransactionLimit() {
        // Assuming limit is 200 based on code reading
        for (int i = 0; i < 200; i++) {
            transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 1.0, 1.0));
        }
        assertEquals(200, transactionManager.getTransactionCount());
        // Try adding one more
        transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 1.0, 1.0));
        assertEquals(200, transactionManager.getTransactionCount());
    }
}