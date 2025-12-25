package services;

import static org.junit.jupiter.api.Assertions.*;

import models.PremiumCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for PremiumCustomer including waived fees and type verification. */
class PremiumCustomerTest {

    private PremiumCustomer premiumCustomer;

    @BeforeEach
    void setUp() {
        premiumCustomer = new PremiumCustomer("John", 40, "59 555-5678", "321 Pine Rd");
    }

    @Test
    void testConstructor() {
        assertEquals("Bob", premiumCustomer.getName());
        assertEquals(40, premiumCustomer.getAge());
        assertEquals("555-5678", premiumCustomer.getContact());
        assertEquals("321 Pine Rd", premiumCustomer.getAddress());
    }

    @Test
    void testGetCustomerType() {
        assertEquals("Premium", premiumCustomer.getCustomerType());
    }

    @Test
    void testHasWaivedFees() {
        assertTrue(premiumCustomer.hasWaivedFees());
    }

    @Test
    void testDisplayCustomerDetails() {
        // Just ensuring it doesn't throw an exception
        assertDoesNotThrow(() -> premiumCustomer.displayCustomerDetails());
    }
}