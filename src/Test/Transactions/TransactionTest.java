package Test.Transactions;

import org.junit.jupiter.api.Test;
import models.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionTest {

    Transaction  transaction = new Transaction("ACCOO1", "Deposit", 22,300);
       @Test
       void transactionTest(){
        assertEquals("ACCOO1", transaction.getAccountNumber());
        assertEquals("TXN001", transaction.getTransactionId());
        assertEquals("Deposit", transaction.getType());
        assertNotNull(transaction.getTimestamp());
       }

    @Test
    void displayTransactionDetails() {
    }
}