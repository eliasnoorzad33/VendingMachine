package vendingmachine.cc13.group1.frontend;

import java.io.IOException;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.purchases.CashRegister;

public class ChangeReport implements Consumer<TextIO> {

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();

        // Check if user authorised - owner or cashier
        CustomerDatabase cDB = CustomerDatabase.getInstance();
        if (cDB.getLoggedIn() == null) {
            terminal.printf("Error: Not available to anonymous users.\n");
            return;
        } else if (!(cDB.getLoggedInRole() == UserRole.OWNER || cDB.getLoggedInRole() == UserRole.CASHIER)) {
            terminal.printf("Error: Not available to users with role %s\n", cDB.getLoggedInRole().name());
            return;
        }

        try {
            CashRegister cr = new CashRegister();
            int[] quantities = cr.getCashInRegister();
            if (quantities == null) {
                terminal.println("\nError generating report. Please try again.\n");
                return;
            }
            cr.generateReport("change_report.csv", quantities);
            terminal.println("\nAvailable change report (change_report.csv) generated.\n");
        } catch (IOException e) {
            terminal.println("\nError generating report. Please try again.\n");
        }
    }

}