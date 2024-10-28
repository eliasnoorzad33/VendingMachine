package vendingmachine.cc13.group1.frontend;

import java.util.ArrayList;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.product.*;
import vendingmachine.cc13.group1.database.customer.*;
import vendingmachine.cc13.group1.database.transactions.*;


public class LastFiveProducts implements Consumer<TextIO> {

    public LastFiveProducts() {}

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();
        CustomerDatabase cDB = CustomerDatabase.getInstance();
        
        // head
        String name;
        if (cDB.getLoggedIn() != null) {
            name = cDB.getLoggedIn();
        } else {
            name = "anonymous users";
        }
        terminal.println();
        terminal.println("---------------------------------------------");
        terminal.printf("5 most recent products purchased by %s:\n", name);
        terminal.println("---------------------------------------------");
        TransactionDatabase tDB = TransactionDatabase.getInstance();
        ArrayList<Product> fiveProducts = tDB.getLastFiveProducts(cDB.getLoggedIn());
        for (Product p : fiveProducts) {
            printProduct(terminal, p);
        }
        terminal.println("---------------------------------------------");
    }



    private void printProduct(TextTerminal<?> terminal, Product product) {
        terminal.printf("[Code %d] %s $%.2f\n", product.getCode(), 
                product.getName(), product.getPrice());
    }
    
}
