package vendingmachine.cc13.group1.database.product;

import org.apache.commons.csv.CSVRecord;

/**
 * Factory method to create products
 * 
 * @author Darin Huang (dhua3771)
 */
public class ProductFactory {
    public Product create(CSVRecord record) {
        int code = Integer.parseInt(record.get("code"));
        int amount = Integer.parseInt(record.get("amount"));
        double price = Double.parseDouble(record.get("price"));
        String name = record.get("name");
        Category category = Category.valueOf(record.get("category"));
        return new Product(code, amount, price, name, category);
    }
}
