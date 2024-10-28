package vendingmachine.cc13.group1.database.transactions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import vendingmachine.cc13.group1.database.product.Product;

public class SellerSummary {
    public static void write(String filename, List<Transaction> transactions, Collection<Product> products) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader("code", "name", "qty-sold").build();
        Multimap<Integer, Product> sold = ArrayListMultimap.create();
        transactions.forEach(t -> {
            if (t.isCancelled()) {
                return;
            }
            t.getProducts().forEach(p -> {
                sold.put(p.getCode(), p);
            });
        });
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            sold.asMap().forEach((code, ps) -> {
                int amount = 0;
                String name = "";
                for (Product p : ps) {
                    amount += p.getAmount();
                    name = p.getName();
                }
                try {
                    printer.printRecord(code, name, amount);
                } catch (IOException e) {
                }
            });
            products.forEach(p -> {
                if (!sold.containsKey(p.getCode())) {
                    try {
                        printer.printRecord(p.getCode(), p.getName(), 0);
                    } catch (IOException e) {
                    }
                }
            });
        }
        writer.close();
    }
}
