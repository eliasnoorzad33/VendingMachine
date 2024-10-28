package vendingmachine.cc13.group1.database.product;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vendingmachine.cc13.group1.database.transactions.SellerSummary;
import vendingmachine.cc13.group1.database.transactions.TransactionDatabase;

public class SellerSummaryTest {
    @BeforeEach
    void resetDatabase() {
        TransactionDatabase.getInstance("").reset();
        ProductDatabase.getInstance("").reset();
    }

    @Test
    void testWrite() throws IOException {
        TransactionDatabase tb = TransactionDatabase.getInstance("src/test/resources/test_transactions_in.json");
        ProductDatabase pb = ProductDatabase.getInstance("src/test/resources/test_products.csv");
        SellerSummary.write("src/test/resources/test_seller_summary.csv", tb.transactions(), pb.products().values());
        File expected = new File("src/test/resources/test_seller_summary_expected.csv");
        File actual = new File("src/test/resources/test_seller_summary.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
        boolean success = actual.delete();
        assertTrue(success);
    }
}
