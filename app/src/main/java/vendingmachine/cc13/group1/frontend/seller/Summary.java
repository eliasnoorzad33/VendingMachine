package vendingmachine.cc13.group1.frontend.seller;

import java.io.IOException;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.product.ProductDatabase;
import vendingmachine.cc13.group1.database.transactions.SellerSummary;
import vendingmachine.cc13.group1.database.transactions.TransactionDatabase;

public class Summary implements Consumer<TextIO> {

    private String filename;

    /**
     * @param filename for the summary
     */
    public Summary(String filename) {
        this.filename = filename;
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

        TransactionDatabase tb = TransactionDatabase.getInstance();
        ProductDatabase pd = ProductDatabase.getInstance();

        try {
            SellerSummary.write(filename, tb.transactions(), pd.products().values());
            terminal.printf("\nSeller Summary (%s) generated.\n\n", filename);
        } catch (IOException e) {
            terminal.println("\nError generating summary. Please try again.\n");
        }
    }
    
}
