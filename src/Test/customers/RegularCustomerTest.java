package Test.customers;

import models.RegularCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RegularCustomerTest {
    public RegularCustomer regularCustomer;
    @BeforeEach
    void setUp() {
        regularCustomer = new RegularCustomer("Emmanuel", 34, "058 44 5758","Kumasi");
    }
    @Test
    void testConstructor() {
        assertEquals("Alice", regularCustomer.getName());
        assertEquals(25, regularCustomer.getAge());
        assertEquals("555-1234", regularCustomer.getContact());
        assertEquals("789 Oak Ave", regularCustomer.getAddress());
    }
        @Test
        void testGetCustomerType() {
            assertEquals("Regular", regularCustomer.getCustomerType());
        }

        @Test
        void testDisplayCustomerDetails() {
            // Just ensuring it doesn't throw an exception
            assertDoesNotThrow(() -> regularCustomer.displayCustomerDetails());
        }
}