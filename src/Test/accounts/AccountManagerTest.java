package Test.accounts;

import models.*;
import models.Customer;
import models.RegularCustomer;
import exceptions.ViewAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exceptions.AccountNotFoundException;
import services.AccountManager;
import utils.ConsoleInputReader;


import static org.junit.jupiter.api.Assertions.*;

class AccountManagerTest {
    private AccountManager accountManager;
    private Customer customer;

    @BeforeEach
    void setUp() {
        accountManager = new AccountManager();
        customer = new RegularCustomer("Alice", 28, "555-0101", "321 Pine St");
    }


    @Test
    void testAddAccount() {
        Account account = new CheckingAccount(customer, 500.0);
        accountManager.addAccount(account);
        assertEquals(1, accountManager.getAccountCount());
    }
    @Test
    void testFindAccountThrowsExceptionWhenNotExist() {
        AccountManager manager = new AccountManager(); // empty manager
        assertThrows(AccountNotFoundException.class, () -> {
            manager.findAccountByNumber("ACC001");  // test triggers exception
        });
    }

    @Test
    void testViewAllAccountsThrowsExceptionWhenEmpty()  throws ViewAccountException{
        AccountManager manager = new AccountManager(); // no Test.accounts added
        // Check that the exception is thrown
        ConsoleInputReader inputReader = new ConsoleInputReader();
        assertThrows(ViewAccountException.class, () -> {
            manager.viewAllAccounts(inputReader);
        });
    }

    @Test
    void getTotalBalance() {
    }

    @Test
    void getAccountCount() {
    }
}