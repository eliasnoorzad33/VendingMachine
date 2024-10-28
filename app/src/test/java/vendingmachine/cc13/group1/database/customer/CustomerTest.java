package vendingmachine.cc13.group1.database.customer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test 
    void testGetUsername() {
        Customer c = new Customer("steven", "12345");
        String ex = "steven";
        assertEquals(ex, c.getUsername());
    }

    @Test 
    void testGetPassword() {
        Customer c = new Customer("steven", "12345");
        String ex = "12345";
        assertEquals(ex, c.getPassword());
    }

    @Test 
    void testCardDetails() {
        Customer c = new Customer("steven", "12345");
        c.setCardholderName("Steven");
        assertEquals(c.getCardholderName(), "Steven");
        c.setCardNumber(45543);
        assertEquals(c.getCardNumber(), 45543);
        
    }

    @Test 
    void testRoles() {
        Customer c = new Customer("steven", "12345");
        assertEquals(c.getRole(), UserRole.CUSTOMER);
        c.setRole(UserRole.OWNER);
        assertEquals(c.getRole(), UserRole.OWNER);
        c.setRole(UserRole.CUSTOMER);
        assertEquals(c.getRole(), UserRole.CUSTOMER); 
    }

    @Test
    void testAsCSVRecord() {
        Customer c = new Customer("steven", "12345");
        String[] ex = new String[] {"steven","12345", "CUSTOMER"};
        assertArrayEquals(ex, c.asCSVRecord());

        c.setCardholderName("Steven");
        c.setCardNumber(99999);
        String[] ex2 = new String[] {"steven","Steven","99999"};
        assertArrayEquals(ex2, c.asCardDetailsCSVRecord());

        String[] ex3 = new String[] {"steven", "CUSTOMER"};
        assertArrayEquals(ex3, c.asCSVRecordForRoleReport());
    }

    @Test
    void testGetHeaders() {
        String[] ex = new String[] {"username", "password", "role"};
        assertArrayEquals(ex, Customer.getHeaders());

        String[] ex2 = new String[] { "username", "cardholderName", "cardNumber" };
        assertArrayEquals(ex2, Customer.getCardDetailsHeaders());

        String[] ex3 = new String[] {"username", "role"};
        assertArrayEquals(ex3, Customer.getHeadersForRoleReport());
    }

    @Test
    void testEquals() {
        Customer c = new Customer("user", "password");
        Customer c2 = new Customer("user", "password");
        assertTrue(c.equals(c));
        assertTrue(c.equals(c2));
        assertFalse(c.equals("hello"));
        assertFalse(c.equals(null));
        Customer c3 = new Customer(null, null);
        Customer c4 = new Customer("user", null);
        Customer c5 = new Customer(null, "password");
        assertFalse(c3.equals(c4));
        assertFalse(c3.equals(c5));
        assertFalse(c4.equals(c3));
        assertFalse(c4.equals(c5));
        assertFalse(c5.equals(c3));
        assertFalse(c5.equals(c4));
    }
}