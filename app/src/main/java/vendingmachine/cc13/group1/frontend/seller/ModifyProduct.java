package vendingmachine.cc13.group1.frontend.seller;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.product.Category;
import vendingmachine.cc13.group1.database.product.Product;
import vendingmachine.cc13.group1.database.product.ProductDatabase;

public class ModifyProduct implements Consumer<TextIO> {

    public static enum Attribute {
        CODE,
        AMOUNT,
        PRICE,
        NAME,
        CATEGORY
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
                    if (!ProductDatabase.getInstance().checkCodeExists(val)) 
                        return Arrays.asList("Item Code: " + val + " does not exist.");
                    return null;
                }).read("Code");

        Attribute toMod = textIO.newEnumInputReader(Attribute.class)
                .read("What would you like to modify");

        ProductDatabase.getInstance().modifyProduct(code, p -> {
            Product newP = p;
            switch (toMod) {
                case AMOUNT:
                    int amount = textIO.newIntInputReader()
                            .withMinVal(1)
                            .withValueChecker((val, itemName) -> {
                                if (ProductDatabase.getInstance().checkMaxQuantity(val)) 
                                    return Arrays.asList(val + " is over the maximum quantity of 15.");
                                return null;
                            })
                            .read("Amount");
                    newP.setAmount(amount);
                    break;
                case CATEGORY:
                    Category cat = textIO.newEnumInputReader(Category.class)
                            .read("Category");
                    newP.setCategory(cat);        
                    break;
                case CODE:
                    int newCode = textIO.newIntInputReader()
                            .withMinVal(0)
                            .withValueChecker((val, itemName) -> {
                                if (ProductDatabase.getInstance().checkCodeExists(val)) 
                                    return Arrays.asList("Item Code: " + val + " already exists.");
                                return null;
                            }).read("Code");
                    newP = new Product(newCode, p.getAmount(), p.getPrice(), p.getName(), p.getCategory());
                    break;
                case NAME:
                    String name = textIO.newStringInputReader()
                            .withMinLength(1)
                            .withInputTrimming(true)
                            .withValueChecker((val, itemName) -> {
                                if (ProductDatabase.getInstance().checkNameExists(val))
                                    return Arrays.asList(val + " already exists.");
                                return null;
                            })
                            .read("Name");
                    newP.setName(name);
                    break;
                case PRICE:
                    double price = textIO.newDoubleInputReader()
                            .withMinVal(0.01)
                            .read("Price");
                    newP.setPrice(price);
                    break;
                default:
                    break;
            }
            return newP;
        });

        try {
            ProductDatabase.getInstance().save();
            terminal.println("\nProduct successfully modified.\n");
        } catch (IOException e) {
            terminal.println("\nError saving new product to database file. Please try again.\n");
        }
    }
    
}
