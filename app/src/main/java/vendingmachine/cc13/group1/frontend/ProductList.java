package vendingmachine.cc13.group1.frontend;

import java.util.Arrays;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.product.Category;
import vendingmachine.cc13.group1.database.product.Product;
import vendingmachine.cc13.group1.database.product.ProductDatabase;

/**
 * The product list page for the application.
 * 
 * @author Darin Huang (dhua3771)
 */
public class ProductList implements Consumer<TextIO> {

    /**
     * @param filename The product database csv
     */
    public ProductList(String filename) {
        ProductDatabase.getInstance(filename);
    }

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();
        terminal.println();
        terminal.println("---------------------------------------------");
        Arrays.stream(Category.values()).forEach(c -> {
            terminal.printf("%s:\n", c.getTitle());
            ProductDatabase.getInstance().fromCategory(c).forEach(p -> {
                printProduct(terminal, p);
            });
            terminal.println();
        });
        terminal.println("---------------------------------------------");

    }

    private void printProduct(TextTerminal<?> terminal, Product product) {
        terminal.printf("[%d] %s $%.2f x %d\n", product.getCode(), 
                product.getName(), product.getPrice(), product.getAmount());
    }
    
}
