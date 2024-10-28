package vendingmachine.cc13.group1.database.product;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ItemReport {

    public static void write(String filename, Collection<Product> products) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Product.getHeaders()).build();
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            products.forEach(p -> {
                try {
                    if (p.getAmount() > 0) {
                        printer.printRecord(Arrays.asList(p.asCSVRecord()));
                    }
                } catch (IOException e) {
                }
            });
        }
        writer.close();
    }
}
