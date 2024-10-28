package vendingmachine.cc13.group1.database.purchases;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

public class CashRegisterTest {

    @Test
    void testGenerateReport() throws IOException {
        CashRegister cr = new CashRegister();
        String fname = "src/test/resources/test_change_report.csv";
        // format is largest ($50) to smallest (5c)
        int[] quantities = {10, 2, 3, 4, 10, 5, 6, 7, 10, 22};
        cr.generateReport(fname, quantities);

        File expected = new File("src/test/resources/test_change_report_expected.csv");
        File actual = new File("src/test/resources/test_change_report.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }

    @Test
    void testGetCashInRegister(){
        CashRegister cr = new CashRegister();
        cr.getCashInRegister();
    }

    @Test
    void testChangeCashInRegister(){
        CashRegister cr = new CashRegister();
        int[] quantities = {1,4,3,5,8,6,4,2,5,7};
        cr.changeCashInRegister(quantities);
    }

}