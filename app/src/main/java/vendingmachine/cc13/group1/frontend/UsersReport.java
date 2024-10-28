package vendingmachine.cc13.group1.frontend;

import java.io.IOException;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;

public class UsersReport implements Consumer<TextIO> {

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();

        // Check if user authorised - only owner
        CustomerDatabase cDB = CustomerDatabase.getInstance();
        if (cDB.getLoggedIn() == null) {
            terminal.printf("Error: Not available to anonymous users.\n");
            return;
        } else if (!(cDB.getLoggedInRole() == UserRole.OWNER)) {
            terminal.printf("Error: Not available to users with role %s\n", cDB.getLoggedInRole().name());
            return;
        }

        int selection = textIO.newIntInputReader().withMinVal(0).withMaxVal(2).read("[(0) to return to main menu] Enter '1' to generate user report '2' add/remove user: \n");

        if (selection == 0){
            return;
        }

        if (selection == 1) {
            try {
                CustomerDatabase.getInstance().generateReport("user_roles_report.csv");
                terminal.println("\nUser roles report (user_roles_report.csv) generated.\n");
            } catch (IOException e) {
                terminal.println("\nError generating report. Please try again.\n");
            }
        }

        while (selection == 2){
            String username = textIO.newStringInputReader().read("Please enter the Username to add/remove: ");
            String input = textIO.newStringInputReader().read("Enter (add) to add user or (remove) to remove user: ");
            if (input.equalsIgnoreCase("add")){
                String password = textIO.newStringInputReader().withInputMasking(true).read("Choose a password: ");
                String role = textIO.newStringInputReader().read("Enter please select a role (seller/cashier/owner): ").toLowerCase();
                switch (role){
                    case "seller":
                        cDB.addSeller(username, password);
                        terminal.println(username + " successfully add as " + role + "." );
                        return;
                    case "cashier":
                        cDB.addCashier(username, password);
                        terminal.println(username + " successfully add as " + role + "." );
                        return;
                    case "owner":
                        cDB.addOwner(username, password);
                        terminal.println(username + " successfully add as " + role + "." );
                        return;
                }
                terminal.println("Input correct role.");
                continue;
            }

            if (input.equalsIgnoreCase("remove")){
                if (cDB.userExists(username)){
                    cDB.removeUser(username);
                    terminal.println(username + " successfully removed.");
                    break;
                }
                terminal.println(username + " does not exist.");
            }
        }


    }

}