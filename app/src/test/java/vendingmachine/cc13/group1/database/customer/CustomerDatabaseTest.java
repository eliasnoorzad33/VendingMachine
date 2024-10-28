package vendingmachine.cc13.group1.database.customer;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import vendingmachine.cc13.group1.database.product.UnintialisedException;

public class CustomerDatabaseTest {

    @BeforeEach
    void restDatabase() {
        CustomerDatabase.getInstance("", "").reset();
    }

    @Test
    void testGetInstance() {
        assertThrows(UnintialisedException.class, () -> CustomerDatabase.getInstance());
        assertNotNull(CustomerDatabase.getInstance("", ""));
    }

    @Test
    void testGets() {
        // get customers list and usernames list
        CustomerDatabase.getInstance("src/test/resources/test_customers_gets.csv", "");
        assertNotNull(CustomerDatabase.getInstance().customers());
        assertNotNull(CustomerDatabase.getInstance().usernames());
        assertEquals(3, CustomerDatabase.getInstance().customers().size());
        assertEquals(3, CustomerDatabase.getInstance().usernames().size());


        Customer exC1 = new Customer("adam", "dog");
        Customer exC2 = new Customer("brian", "echidna");
        Customer exC3 = new Customer("charlie", "frog");
        assertEquals(exC1, CustomerDatabase.getInstance().customers().get(0));
        assertEquals(exC2, CustomerDatabase.getInstance().customers().get(1));
        assertEquals(exC3, CustomerDatabase.getInstance().customers().get(2));

        String exN1 = "adam";
        String exN2 = "brian";
        String exN3 = "charlie";
        assertEquals(exN1, CustomerDatabase.getInstance().usernames().get(0));
        assertEquals(exN2, CustomerDatabase.getInstance().usernames().get(1));
        assertEquals(exN3, CustomerDatabase.getInstance().usernames().get(2));
    }



    @Test
    void testAddAndRemoveRolesAndSave() throws IOException {
        // Add a customer and compare to expected output

        Files.copy(new File("src/test/resources/test_customers_save_in.csv").toPath(), new File("src/test/resources/test_customers_save.csv").toPath(), 
            StandardCopyOption.REPLACE_EXISTING);

        CustomerDatabase.getInstance("src/test/resources/test_customers_save.csv", "");
        CustomerDatabase.getInstance().addCustomer("john", "ASafePassword");
        CustomerDatabase.getInstance().addSeller("jeff", "sellerpw");
        CustomerDatabase.getInstance().addCashier("tom", "cashierpw");
        CustomerDatabase.getInstance().addOwner("dave", "ownerpw");
        CustomerDatabase.getInstance().removeUser("jeff");

        File expected = new File("src/test/resources/test_customers_save_expected.csv");
        File actual = new File("src/test/resources/test_customers_save.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));          
    }

    @Test
    void testLogin() {
        CustomerDatabase.getInstance("src/test/resources/test_customers_gets.csv", "");
        assertNull(CustomerDatabase.getInstance().getLoggedIn());
        CustomerDatabase.getInstance().setLoggedIn("adam");
        assertNotNull(CustomerDatabase.getInstance().getLoggedInAsObject());
        assertEquals("adam", CustomerDatabase.getInstance().getLoggedIn());
        assertEquals(UserRole.SELLER, CustomerDatabase.getInstance().getLoggedInRole());
        CustomerDatabase.getInstance().logout();
        assertNull(CustomerDatabase.getInstance().getLoggedIn());
    }

    @Test
    void testLoginNotExists() {
        CustomerDatabase.getInstance("src/test/resources/test_customers_gets.csv", "");
        assertNull(CustomerDatabase.getInstance().getLoggedIn());
        assertNull(CustomerDatabase.getInstance().getLoggedInAsObject());
        CustomerDatabase.getInstance().setLoggedIn("anthony_norman_albanese");
        assertNull(CustomerDatabase.getInstance().getLoggedIn());
        assertNull(CustomerDatabase.getInstance().getLoggedInAsObject());
    }

    @Test
    void testAddCardAndSave() throws IOException {
        Files.copy(new File("src/test/resources/test_card_save_in.csv").toPath(), new File("src/test/resources/test_card_save.csv").toPath(), 
            StandardCopyOption.REPLACE_EXISTING);

        CustomerDatabase.getInstance("src/test/resources/test_customers_gets.csv", "src/test/resources/test_card_save.csv");
        CustomerDatabase.getInstance().setLoggedIn("brian");
        CustomerDatabase.getInstance().saveCardToLoggedInUser("Sergio",42689);

        File expected = new File("src/test/resources/test_card_save_expected.csv");
        File actual = new File("src/test/resources/test_card_save.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }

    @Test 
    void testUserRolesReport() throws IOException {
        CustomerDatabase.getInstance("src/test/resources/test_customers_gets.csv", "");
        CustomerDatabase.getInstance().generateReport("src/test/resources/test_users_report.csv");

        File expected = new File("src/test/resources/test_users_report_expected.csv");
        File actual = new File("src/test/resources/test_users_report.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }

}