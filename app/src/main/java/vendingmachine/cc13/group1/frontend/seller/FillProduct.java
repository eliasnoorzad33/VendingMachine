package vendingmachine.cc13.group1.frontend.seller;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.product.Product;
import vendingmachine.cc13.group1.database.product.ProductDatabase;

public class FillProduct implements Consumer<TextIO> {

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
                    if (!ProductDatabase.getInstance().checkCodeExists(val)) 
                        return Arrays.asList("Item Code: " + val + " does not exist.");
                    return null;
                }).read("Code");

        int amount = textIO.newIntInputReader()
                .withMinVal(1)
                .withValueChecker((val, itemName) -> {
                    Product p = ProductDatabase.getInstance().products().get(code);
                    if (ProductDatabase.getInstance().checkMaxQuantity(p.getAmount() + val)) 
                        return Arrays.asList("Adding " + val + " would exceed the maximum quantity of 15.");
                    return null;
                })
                .read("Amount");

        ProductDatabase.getInstance().fillProduct(code, amount);
        try {
            ProductDatabase.getInstance().save();
        } catch (IOException e) {
            terminal.println("\nError saving new amount to database file. Please try again.\n");
        }

        terminal.println("\nSuccessfully filled product.\n");
    }
    
}
