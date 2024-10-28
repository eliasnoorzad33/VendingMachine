package vendingmachine.cc13.group1.frontend;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.transactions.TransactionDatabase;

import java.io.IOException;
import java.util.function.Consumer;

public class TransactionReportMenu implements Consumer<TextIO> {

    public TransactionReportMenu(String filename) {
        // just initialises the db if it isn't already
        TransactionDatabase.getInstance(filename);
    }


    public void accept(TextIO textIO){
        TextTerminal<?> terminal = textIO.getTextTerminal();
        int reportChoice = 0;
        while (true){
            reportChoice = textIO.newIntInputReader().withMinVal(1).withMaxVal(2).read("\nEnter '1' generate report of all completed transactions or '2' to generate the report of cancelled transactions\n");

            if (reportChoice == 1){
                // Check if user authorised
                CustomerDatabase cDB = CustomerDatabase.getInstance();
                if (cDB.getLoggedIn() == null) {
                    terminal.printf("Error: Not available to anonymous users.\n");
                    return;
                } else if (!(cDB.getLoggedInRole() == UserRole.CASHIER || cDB.getLoggedInRole() == UserRole.OWNER)) {
                    terminal.printf("Error: Not available to users with role %s\n", cDB.getLoggedInRole().name());
                    return;
                }

                try {
                    TransactionDatabase.getInstance().generateReport("transaction_report.csv");
                } catch (IOException e){
                    e.printStackTrace();
                    terminal.println("Failed to generate report");
                    return;
                }
                terminal.println("Successfully generated transaction report.");
                return;
            }
            else if (reportChoice == 2){
                // Check if user authorised
                CustomerDatabase cDB = CustomerDatabase.getInstance();
                if (cDB.getLoggedIn() == null) {
                    terminal.printf("Error: Not available to anonymous users.\n");
                    return;
                } else if (!(cDB.getLoggedInRole() == UserRole.OWNER)) {
                    terminal.printf("Error: Not available to users with role %s\n", cDB.getLoggedInRole().name());
                    return;
                }

                try {
                    TransactionDatabase.getInstance().generateCancelledReport("cancelled_transaction_report.csv");
                } catch (IOException e){
                    e.printStackTrace();
                    terminal.println("Failed to generate report");
                    return;
                }
                terminal.println("Successfully generated cancelled transactions report.");
                return;
            }
        }
    }

}
