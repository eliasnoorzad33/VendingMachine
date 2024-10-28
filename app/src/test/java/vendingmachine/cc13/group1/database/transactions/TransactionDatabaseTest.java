package vendingmachine.cc13.group1.database.transactions;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import vendingmachine.cc13.group1.database.product.Category;
import vendingmachine.cc13.group1.database.product.Product;
import vendingmachine.cc13.group1.database.product.UnintialisedException;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class TransactionDatabaseTest {
    @BeforeEach
    void resetDatabase() {
        TransactionDatabase.getInstance("").reset();
    }
    @Test
    @Order(1)
    void testGetInstance() {
        assertThrows(UnintialisedException.class, () -> TransactionDatabase.getInstance());
        assertNotNull(TransactionDatabase.getInstance(""));
    }
//    @Test
//    @Order(2)
//    void generateTestTransactions(){
//        TransactionDatabase.getInstance("src/test/resources/test_cancelled_transactions.json");
//
//        Transaction t1 = new Transaction("username", LocalDateTime.of(2022, 11, 25, 12, 30, 0), new ArrayList<Product>(Arrays.asList(
//                new Product(1002, 2, 3.5,"Pringles", Category.CHIPS))));
//        t1.setPayment(Payment.CARD);
//        t1.cancel();
//        t1.setReason("user cancelled");
//        Transaction t2 = new Transaction("anotherusername", LocalDateTime.of(2022, 11, 25, 12, 30, 0), new ArrayList<Product>(Arrays.asList(
//                new Product(1005, 12, 7,"Coke", Category.DRINKS))));
//        t2.setPayment(Payment.CASH);
//        t2.cancel();
//        t2.setReason("no change");
//        TransactionDatabase.getInstance().addTransaction(t1);
//        TransactionDatabase.getInstance().addTransaction(t2);
//
//    }

    @Test
    @Order(3)
    void testTransactions() {
        assertNotNull(TransactionDatabase.getInstance("").transactions());
        assertEquals(0, TransactionDatabase.getInstance().transactions().size());

        TransactionDatabase.getInstance().reset();
        assertEquals(2, TransactionDatabase.getInstance("src/test/resources/test_transactions.json").transactions().size());
        Transaction ex = new Transaction("username", LocalDateTime.of(2022, 11, 25, 12, 30, 0, 0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 3.5,"Pringles", Category.CHIPS)
        )));
        ex.setPayment(Payment.CARD);
        assertEquals(ex, TransactionDatabase.getInstance("src/test/resources/test_transactions.json").transactions().get(0));

        ex = new Transaction("anotherusername", LocalDateTime.of(2022, 11, 25, 12, 30, 0), new ArrayList<Product>(Arrays.asList(
                new Product(1005, 12, 7,"Coke", Category.DRINKS))));
        ex.setPayment(Payment.CASH);
        assertEquals(ex, TransactionDatabase.getInstance("src/test/resources/test_transactions.json").transactions().get(1));

    }

    @Test
    void testSave() throws IOException {
        TransactionDatabase.getInstance("src/test/resources/test_transactions_save.json");
        TransactionDatabase.getInstance().transactions().clear();
        Transaction t1 = new Transaction("username", LocalDateTime.of(2022, 11, 25, 12, 30, 0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 3.5,"Pringles", Category.CHIPS))));

        Transaction t2 = new Transaction("anotherusername", LocalDateTime.of(2022, 11, 25, 12, 30, 0), new ArrayList<Product>(Arrays.asList(
                new Product(1005, 12, 7,"Coke", Category.DRINKS))));
        TransactionDatabase.getInstance().addTransaction(t1);
        TransactionDatabase.getInstance().addTransaction(t2);
        TransactionDatabase.getInstance().save();
        File expected = new File("src/test/resources/test_transactions.csv");
        File actual = new File("src/test/resources/test_transactions_save.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }

    @Test
    void generateReportTest() throws IOException{
        TransactionDatabase.getInstance("src/test/resources/test_transactions.json");
        TransactionDatabase.getInstance().save();
        TransactionDatabase.getInstance().generateReport("src/test/resources/test_transactions_report.csv");

        File expected = new File("src/test/resources/test_transactions_report_expected.csv");
        File actual = new File("src/test/resources/test_transactions_report.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }

    @Test
    void generateCancelledReportTest() throws IOException{
        TransactionDatabase.getInstance("src/test/resources/test_cancelled_transactions.json");
        TransactionDatabase.getInstance().save();
        TransactionDatabase.getInstance().generateCancelledReport("src/test/resources/test_cancelled_transactions_report.csv");

        File expected = new File("src/test/resources/test_cancelled_transactions_report_expected.csv");
        File actual = new File("src/test/resources/test_cancelled_transactions_report.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }

    @Test
    void testGetLastFiveProducts() {
        TransactionDatabase.getInstance("src/test/resources/test_last_five_products.json");
        ArrayList<Product> actual = TransactionDatabase.getInstance().getLastFiveProducts(null);

        ArrayList<Product> expected = new ArrayList<Product>();
        expected.add(new Product(2, 0, 1.2, "Pepsi", Category.DRINKS));
        expected.add(new Product(8, 10, 100.0, "Smiths", Category.CHIPS));
        expected.add(new Product(0, 9, 13.3, "Mountain Dew", Category.DRINKS));
        
        for (int i = 0; i < 3; i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }
}
