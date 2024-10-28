package vendingmachine.cc13.group1.database.product;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

/**
 * Interface for the database that stores the products.
 * Products are stored in a CSV file. 
 * 
 * @author Darin Huang (dhua3771)
 */
public class ProductDatabase {

    private static ProductDatabase instance;

    public static ProductDatabase getInstance(String filename) {
        if (instance == null) instance = new ProductDatabase(filename);
        return instance;
    }

    public static ProductDatabase getInstance() {
        if (instance == null) throw new UnintialisedException();
        else return instance;
    } 

    private final String filename;
    private Map<Integer, Product> products;
    private Multimap<Category, Product> catProducts;

    /**
     * @param filename
     */
    private ProductDatabase(String filename) {
        this.filename = filename;
        this.catProducts = MultimapBuilder.enumKeys(Category.class)
                .arrayListValues().build();
        try {
            this.products = read();
            this.products.values().forEach(p -> {
                this.catProducts.put(p.getCategory(), p);
            });
        } catch (IOException e) {
            this.products = new HashMap<>();
        }
    }

    private void write(Collection<Product> products) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Product.getHeaders()).build();
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            products.forEach(p -> {
                try {
                    printer.printRecord(Arrays.asList(p.asCSVRecord()));
                } catch (IOException e) {
                }
            });
        }
        writer.close();
    }

    private Map<Integer, Product> read() throws IOException {
        Map<Integer, Product> read = new HashMap<>();
        FileReader reader = new FileReader(filename);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Product.getHeaders())
                .setSkipHeaderRecord(true).build();
        Iterable<CSVRecord> records = format.parse(reader);
        ProductFactory factory = new ProductFactory();
        for (CSVRecord r : records) {
            Product p = factory.create(r);
            read.put(p.getCode(), p);
        }
        return read;
    }

    public void save() throws IOException {
        write(products.values());
    }

    public Map<Integer, Product> products() {
        return products;
    }

    public Collection<Product> fromCategory(Category c) {
        return catProducts.get(c);
    }

    public void reset() {
        instance = null;
    }

    /**
     * Adds a new product to the database
     * @throws IOException
     */
    public void addNewProduct(int code, int amount, double price, String name, Category category) throws IOException {
        if (checkCodeExists(code) || checkNameExists(name) || checkMaxQuantity(amount)) return;
        Product newProduct = new Product(code, amount, price, name, category);
        products.put(newProduct.getCode(), newProduct);
        catProducts.put(newProduct.getCategory(), newProduct);
    }

    public boolean checkCodeExists(int code) {
        return products.keySet().contains(code);
    }

    public boolean checkNameExists(String name) {
        for (Product p : products.values()) {
            if (p.getName().equals(name)) return true;
        }
        return false;
    }

    public boolean checkMaxQuantity(int amount) {
        return amount > 15;
    }

    /**
     * Fills removes products from database based on list
     *
     * @param products the products and their amounts to be removed
     *
     */
    public void purchase(ArrayList<Product> products) throws IOException{
        for(Product product : products){

            Product existingProduct = this.products.get(product.getCode());
            existingProduct.setAmount(existingProduct.getAmount() - product.getAmount());
            this.products.put(product.getCode(), existingProduct);
        }
        save();
    }
    /**
     * Fills the product with the given amount
     * 
     * @param code the product code
     * @param amountToAdd add this amount of product
     */
    public void fillProduct(int code, int amountToAdd) {
        if (!checkCodeExists(code)) return;
        Product product = products.get(code);
        int newAmount = product.getAmount() + amountToAdd;
        if (checkMaxQuantity(newAmount)) return;
        product.setAmount(newAmount);
    }

    /**
     * Modifies the product 
     * 
     * @param code the product code
     * @param modification a function that modifies the product
     */
    public void modifyProduct(int code, Function<Product, Product> modification) {
        if (modification == null) return;
        if (!checkCodeExists(code)) return;
        Product product = products.remove(code);
        catProducts.remove(product.getCategory(), product);
        Product newProd = modification.apply(product);
        if (newProd == null) return;
        products.put(newProd.getCode(), newProd);
        catProducts.put(newProd.getCategory(), newProd);
    }

}
