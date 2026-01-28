package accounts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import customers.FilePersistence.AccountFilePersistenceService;
import models.Account;
import models.CheckingAccount;
import models.Customer;
import models.PremiumCustomer;
import models.RegularCustomer;
import models.SavingsAccount;

/**
 * Test suite for AccountFilePersistenceService.
 * Tests saving and loading accounts from file with various scenarios.
 */
@DisplayName("Account File Persistence Tests")
class AccountFilePersistenceTest {

    private AccountFilePersistenceService persistenceService;
    private List<Account> testAccounts;
    private static final String TEST_ACCOUNTS_FILE = "src/data/accounts.txt";

    @BeforeEach
    void setUp() {
        persistenceService = new AccountFilePersistenceService();
        testAccounts = new ArrayList<>();
        // Reset account counter for consistent testing
        Account.setAccountCounter(0);
    }

    @AfterEach
    void tearDown() {
        // Clean up test files
        try {
            Path filePath = Paths.get(TEST_ACCOUNTS_FILE);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            System.err.println("Error cleaning up test file: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Save single SavingsAccount to file")
    void testSaveSingleSavingsAccount() throws IOException {
        // Arrange
        Customer customer = new RegularCustomer("John Doe", 30, "0501234567", "Accra");
        Account account = new SavingsAccount(customer, 5000.00);
        testAccounts.add(account);

        // Act
        persistenceService.saveAccountsToFile(testAccounts);

        // Assert
        Path filePath = Paths.get(TEST_ACCOUNTS_FILE);
        assertTrue(Files.exists(filePath), "Accounts file should exist");
        assertTrue(Files.size(filePath) > 0, "Accounts file should not be empty");
        
        String content = Files.readString(filePath);
        assertTrue(content.contains("John Doe"), "File should contain customer name");
        assertTrue(content.contains("5000.00"), "File should contain account balance");
    }

    @Test
    @DisplayName("Save single CheckingAccount to file")
    void testSaveSingleCheckingAccount() throws IOException {
        // Arrange
        Customer customer = new PremiumCustomer("Jane Smith", 25, "0502345678", "Kumasi");
        Account account = new CheckingAccount(customer, 10000.00);
        testAccounts.add(account);

        // Act
        persistenceService.saveAccountsToFile(testAccounts);

        // Assert
        Path filePath = Paths.get(TEST_ACCOUNTS_FILE);
        assertTrue(Files.exists(filePath), "Accounts file should exist");
        
        String content = Files.readString(filePath);
        assertTrue(content.contains("Jane Smith"), "File should contain customer name");
        assertTrue(content.contains("10000.00"), "File should contain account balance");
        assertTrue(content.contains("Premium"), "File should contain customer type");
    }

    @Test
    @DisplayName("Save multiple mixed accounts to file")
    void testSaveMultipleAccounts() throws IOException {
        // Arrange
        Customer customer1 = new RegularCustomer("Alice Johnson", 28, "0503456789", "Takoradi");
        Customer customer2 = new PremiumCustomer("Bob Williams", 35, "0504567890", "Tema");
        Customer customer3 = new RegularCustomer("Carol Davis", 22, "0505678901", "Sekondi");
        
        testAccounts.add(new SavingsAccount(customer1, 3000.00));
        testAccounts.add(new CheckingAccount(customer2, 15000.00));
        testAccounts.add(new SavingsAccount(customer3, 2500.00));

        // Act
        persistenceService.saveAccountsToFile(testAccounts);

        // Assert
        Path filePath = Paths.get(TEST_ACCOUNTS_FILE);
        assertTrue(Files.exists(filePath), "Accounts file should exist");
        
        String content = Files.readString(filePath);
        assertEquals(3, content.split("\n").length, "File should contain 3 account lines");
        assertTrue(content.contains("Alice Johnson"), "File should contain first customer");
        assertTrue(content.contains("Bob Williams"), "File should contain second customer");
        assertTrue(content.contains("Carol Davis"), "File should contain third customer");
    }

    @Test
    @DisplayName("Save empty account list to file")
    void testSaveEmptyAccountList() throws IOException {
        // Act
        persistenceService.saveAccountsToFile(new ArrayList<>());

        // Assert
        Path filePath = Paths.get(TEST_ACCOUNTS_FILE);
        assertTrue(Files.exists(filePath), "Accounts file should exist even when empty");
        assertEquals(0, Files.size(filePath), "File should be empty");
    }

    @Test
    @DisplayName("Load accounts from non-existent file returns empty list")
    void testLoadAccountsFromNonExistentFile() throws IOException {
        // Act
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertNotNull(loadedAccounts, "Loaded accounts list should not be null");
        assertTrue(loadedAccounts.isEmpty(), "Loaded accounts should be empty for non-existent file");
    }

    @Test
    @DisplayName("Load single SavingsAccount from file")
    void testLoadSingleSavingsAccount() throws IOException {
        // Arrange
        Customer customer = new RegularCustomer("Michael Brown", 40, "0506789012", "Cape Coast");
        Account account = new SavingsAccount(customer, 7500.50);
        testAccounts.add(account);
        persistenceService.saveAccountsToFile(testAccounts);

        // Act
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertNotNull(loadedAccounts, "Loaded accounts should not be null");
        assertEquals(1, loadedAccounts.size(), "Should load 1 account");
        
        Account loaded = loadedAccounts.get(0);
        assertEquals("Michael Brown", loaded.getCustomer().getName(), "Customer name should match");
        assertEquals(7500.50, loaded.getBalance(), "Balance should match");
        assertEquals("Savings", loaded.getAccountType(), "Account type should be Savings");
    }

    @Test
    @DisplayName("Load single CheckingAccount from file")
    void testLoadSingleCheckingAccount() throws IOException {
        // Arrange
        Customer customer = new PremiumCustomer("Sarah Wilson", 32, "0507890123", "Osu");
        Account account = new CheckingAccount(customer, 20000.00);
        testAccounts.add(account);
        persistenceService.saveAccountsToFile(testAccounts);

        // Act
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertNotNull(loadedAccounts, "Loaded accounts should not be null");
        assertEquals(1, loadedAccounts.size(), "Should load 1 account");
        
        Account loaded = loadedAccounts.get(0);
        assertEquals("Sarah Wilson", loaded.getCustomer().getName(), "Customer name should match");
        assertEquals(20000.00, loaded.getBalance(), "Balance should match");
        assertEquals("Checking", loaded.getAccountType(), "Account type should be Checking");
        assertEquals("Premium", loaded.getCustomer().getCustomerType(), "Customer type should be Premium");
    }

    @Test
    @DisplayName("Load multiple mixed accounts from file")
    void testLoadMultipleAccounts() throws IOException {
        // Arrange
        Customer c1 = new RegularCustomer("Emma Thompson", 26, "0508901234", "Accra");
        Customer c2 = new PremiumCustomer("David Miller", 45, "0509012345", "Kumasi");
        Customer c3 = new RegularCustomer("Sophia Taylor", 31, "0500123456", "Ashanti");
        
        testAccounts.add(new SavingsAccount(c1, 4500.75));
        testAccounts.add(new CheckingAccount(c2, 25000.00));
        testAccounts.add(new SavingsAccount(c3, 3200.50));
        
        persistenceService.saveAccountsToFile(testAccounts);

        // Act
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertNotNull(loadedAccounts, "Loaded accounts should not be null");
        assertEquals(3, loadedAccounts.size(), "Should load 3 accounts");
        
        // Verify first account
        assertEquals("Emma Thompson", loadedAccounts.get(0).getCustomer().getName());
        assertEquals(4500.75, loadedAccounts.get(0).getBalance());
        assertEquals("Savings", loadedAccounts.get(0).getAccountType());
        
        // Verify second account
        assertEquals("David Miller", loadedAccounts.get(1).getCustomer().getName());
        assertEquals(25000.00, loadedAccounts.get(1).getBalance());
        assertEquals("Checking", loadedAccounts.get(1).getAccountType());
        
        // Verify third account
        assertEquals("Sophia Taylor", loadedAccounts.get(2).getCustomer().getName());
        assertEquals(3200.50, loadedAccounts.get(2).getBalance());
        assertEquals("Savings", loadedAccounts.get(2).getAccountType());
    }

    @Test
    @DisplayName("Round-trip: Save and load preserves account data")
    void testRoundTripSaveAndLoad() throws IOException {
        // Arrange
        Customer customer1 = new RegularCustomer("Oliver Brown", 29, "0501234567", "Abuja");
        Customer customer2 = new PremiumCustomer("Isabella Garcia", 38, "0502345678", "Lagos");
        
        Account original1 = new SavingsAccount(customer1, 6500.25);
        Account original2 = new CheckingAccount(customer2, 18500.75);
        
        testAccounts.add(original1);
        testAccounts.add(original2);

        // Act
        persistenceService.saveAccountsToFile(testAccounts);
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertEquals(2, loadedAccounts.size(), "Should preserve all accounts");
        
        for (int i = 0; i < testAccounts.size(); i++) {
            Account original = testAccounts.get(i);
            Account loaded = loadedAccounts.get(i);
            
            assertEquals(original.getAccountNumber(), loaded.getAccountNumber(), "Account numbers should match");
            assertEquals(original.getCustomer().getName(), loaded.getCustomer().getName(), "Customer names should match");
            assertEquals(original.getBalance(), loaded.getBalance(), "Balances should match");
            assertEquals(original.getAccountType(), loaded.getAccountType(), "Account types should match");
            assertEquals(original.getStatus(), loaded.getStatus(), "Statuses should match");
        }
    }

    @Test
    @DisplayName("Load file with empty lines should skip them")
    void testLoadFileWithEmptyLines() throws IOException {
        // Arrange
        Customer customer = new RegularCustomer("Nathan Lee", 27, "0503456789", "Nairobi");
        Account account = new SavingsAccount(customer, 5000.00);
        testAccounts.add(account);
        
        persistenceService.saveAccountsToFile(testAccounts);
        
        // Manually add empty lines to the file
        Path filePath = Paths.get(TEST_ACCOUNTS_FILE);
        String content = Files.readString(filePath);
        Files.writeString(filePath, content + "\n\n");

        // Act
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertEquals(1, loadedAccounts.size(), "Should skip empty lines and load only valid accounts");
        assertEquals("Nathan Lee", loadedAccounts.get(0).getCustomer().getName());
    }

    @Test
    @DisplayName("Account with special characters in customer name")
    void testAccountWithSpecialCharactersInName() throws IOException {
        // Arrange
        Customer customer = new RegularCustomer("O'Brien-Smith", 33, "0504567890", "San Diego");
        Account account = new SavingsAccount(customer, 8500.00);
        testAccounts.add(account);

        // Act
        persistenceService.saveAccountsToFile(testAccounts);
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertEquals(1, loadedAccounts.size(), "Should handle special characters");
        assertEquals("O'Brien-Smith", loadedAccounts.get(0).getCustomer().getName());
    }

    @Test
    @DisplayName("Save inactive accounts to file")
    void testSaveInactiveAccount() throws IOException {
        // Arrange
        Customer customer = new RegularCustomer("Violet Harris", 50, "0505678901", "Boston");
        Account account = new SavingsAccount(customer, 12000.00);
        account.setStatus("Inactive");
        testAccounts.add(account);

        // Act
        persistenceService.saveAccountsToFile(testAccounts);
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertEquals(1, loadedAccounts.size());
        assertEquals("Inactive", loadedAccounts.get(0).getStatus(), "Account status should be preserved");
    }

    @Test
    @DisplayName("Large account list handling")
    void testLargeAccountList() throws IOException {
        // Arrange - create 100 accounts
        for (int i = 1; i <= 100; i++) {
            Customer customer = new RegularCustomer(
                "Customer" + i, 
                20 + (i % 50), 
                "050" + String.format("%07d", i), 
                "City" + (i % 10)
            );
            
            if (i % 2 == 0) {
                testAccounts.add(new SavingsAccount(customer, 5000 + i));
            } else {
                testAccounts.add(new CheckingAccount(customer, 10000 + i));
            }
        }

        // Act
        persistenceService.saveAccountsToFile(testAccounts);
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertEquals(100, loadedAccounts.size(), "Should save and load 100 accounts");
        assertEquals("Customer1", loadedAccounts.get(0).getCustomer().getName());
        assertEquals("Customer100", loadedAccounts.get(99).getCustomer().getName());
    }

    @Test
    @DisplayName("Account balance with decimal precision")
    void testAccountBalancePrecision() throws IOException {
        // Arrange
        Customer customer = new RegularCustomer("Zoe Martinez", 24, "0506789012", "Phoenix");
        Account account = new SavingsAccount(customer, 12345.67);
        testAccounts.add(account);

        // Act
        persistenceService.saveAccountsToFile(testAccounts);
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertEquals(12345.67, loadedAccounts.get(0).getBalance(), 0.01, "Balance precision should be maintained");
    }

    @Test
    @DisplayName("Multiple saves should overwrite previous data")
    void testMultipleSavesOverwriteData() throws IOException {
        // Arrange - First save
        Customer customer1 = new RegularCustomer("First Customer", 25, "0501111111", "City1");
        testAccounts.add(new SavingsAccount(customer1, 1000.00));
        persistenceService.saveAccountsToFile(testAccounts);

        // Clear and save again with different data
        testAccounts.clear();
        Customer customer2 = new RegularCustomer("Second Customer", 30, "0502222222", "City2");
        testAccounts.add(new SavingsAccount(customer2, 2000.00));

        // Act
        persistenceService.saveAccountsToFile(testAccounts);
        List<Account> loadedAccounts = persistenceService.loadAccountsFromFile();

        // Assert
        assertEquals(1, loadedAccounts.size(), "Should contain only the last saved account");
        assertEquals("Second Customer", loadedAccounts.get(0).getCustomer().getName());
        assertEquals(2000.00, loadedAccounts.get(0).getBalance());
    }
}
