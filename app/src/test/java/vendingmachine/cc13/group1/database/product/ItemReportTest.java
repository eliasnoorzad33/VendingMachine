package vendingmachine.cc13.group1.database.product;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class ItemReportTest {
    @Test
    void testWrite() throws IOException {
        ItemReport.write("src/test/resources/test_av_items.csv", ProductDatabase.getInstance("src/test/resources/test_products.csv").products().values());
        File expected = new File("src/test/resources/test_av_items_save.csv");
        File actual = new File("src/test/resources/test_av_items.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
        boolean success = actual.delete();
        assertTrue(success);
    }
}
