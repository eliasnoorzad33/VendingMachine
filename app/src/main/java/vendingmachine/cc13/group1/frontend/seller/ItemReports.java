package vendingmachine.cc13.group1.frontend.seller;

import java.io.IOException;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.product.ItemReport;
import vendingmachine.cc13.group1.database.product.ProductDatabase;

public class ItemReports implements Consumer<TextIO> {

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

        try {
            ItemReport.write("available_items.csv", ProductDatabase.getInstance().products().values());
            terminal.println("\nItem report (available_items.csv) generated.\n");
        } catch (IOException e) {
            terminal.println("\nError generating report. Please try again.\n");
        }
    }
    
}
