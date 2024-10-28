package vendingmachine.cc13.group1.database.transactions;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import vendingmachine.cc13.group1.database.product.Category;
import vendingmachine.cc13.group1.database.product.Product;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionDBWriterTest {

    @BeforeEach
    void resetDatabase() {
        TransactionDatabase.getInstance("").reset();
    }

    @Test
    public void testWriteToJSON() throws IOException {
        Transaction t1 = new Transaction("username", LocalDateTime.of(2022, 11, 25, 12, 30, 0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 3.5, "Pringles", Category.CHIPS))));
        t1.setPayment(Payment.CARD);
        Transaction t2 = new Transaction("anotherusername", LocalDateTime.of(2022, 11, 25, 12, 30, 0), new ArrayList<Product>(Arrays.asList(
                new Product(1005, 12, 7, "Coke", Category.DRINKS))));
        TransactionDBWriter writer = new TransactionDBWriter();
        t2.setPayment(Payment.CASH);

        ArrayList<Transaction> transactions = new ArrayList<>(Arrays.asList(t1, t2));

        File output = new File("src/test/resources/test_db_writer.json");
        OutputStream out = new FileOutputStream(output);

        writer.writeJsonStream(out, transactions);

        File expected = new File("src/test/resources/test_transactions.json");
        File actual = new File("src/test/resources/test_db_writer.json");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }
}
