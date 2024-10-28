package vendingmachine.cc13.group1.frontend.seller;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.product.Category;
import vendingmachine.cc13.group1.database.product.ProductDatabase;
import vendingmachine.cc13.group1.frontend.ProductList;

/**
 * Fronted for adding a product to the vending machine
 */
public class AddProduct implements Consumer<TextIO> {

    ProductList productList;

    /**
     * @param productList to show the products after adding 
     */
    public AddProduct(ProductList productList) {
        this.productList = productList;
    }

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();

        // Check if user authorised
        CustomerDatabase cDB = CustomerDatabase.getInstance();
        if (cDB.getLoggedIn() == null) {
            terminal.printf("Error: Not available to anonymous users.\n");
            return;
        } else if (!(cDB.getLoggedInRole() == UserRole.SELLER || cDB.getLoggedInRole() == UserRole.OWNER)) {
            terminal.printf("Error: Not available to users with role %s\n", cDB.getLoggedInRole().name());
            return;
        }


        int code = textIO.newIntInputReader()
                .withMinVal(0)
                .withValueChecker((val, itemName) -> {
                    if (ProductDatabase.getInstance().checkCodeExists(val)) 
                        return Arrays.asList("Item Code: " + val + " already exists.");
                    return null;
                }).read("Code");
        String name = textIO.newStringInputReader()
                .withMinLength(1)
                .withInputTrimming(true)
                .withValueChecker((val, itemName) -> {
                    if (ProductDatabase.getInstance().checkNameExists(val))
                        return Arrays.asList(val + " already exists.");
                    return null;
                })
                .read("Name");
        Category category = textIO.newEnumInputReader(Category.class)
                .read("Category");
        int amount = textIO.newIntInputReader()
                .withMinVal(1)
                .withValueChecker((val, itemName) -> {
                    if (ProductDatabase.getInstance().checkMaxQuantity(val)) 
                        return Arrays.asList(val + " is over the maximum quantity of 15.");
                    return null;
                })
                .read("Amount");
        double price = textIO.newDoubleInputReader()
                .withMinVal(0.01)
                .read("Price");

        try {
            ProductDatabase.getInstance().addNewProduct(code, amount, price, name, category);
            ProductDatabase.getInstance().save();
            productList.accept(textIO);
            terminal.println("\nProduct successfully added.\n");
        } catch (IOException e) {
            terminal.println("\nError saving new product to database file. Please try again.\n");
		} 
    }
    
}
