package vendingmachine.cc13.group1.frontend;

import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.Customer;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;


public class LoginPrompt implements Consumer<TextIO> {

    
    public LoginPrompt() {}

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();
        CustomerDatabase cDB = CustomerDatabase.getInstance();

        // check if already logged in
        if (cDB.getLoggedIn() != null) {
            terminal.printf("Error: Already logged in as %s\n", cDB.getLoggedIn()); 
            return;
        }

        String username = textIO.newStringInputReader().read("Username");
        String password = textIO.newStringInputReader().withInputMasking(true).read("Password");

        // Check if in the database
        for (Customer c : cDB.customers()) {
            if (c.getUsername().equals(username)) {
                if (c.getPassword().equals(password)) {
                    cDB.setLoggedIn(username);
                    terminal.printf("Login successful. Welcome, %s\n", username);
                    terminal.printf("Role: %s\n", cDB.getLoggedInRole().name());
                    return;
                } else {
                    terminal.println("Incorrect password."); 
                    return;
                }
            }
        }

        terminal.println("Username not found.");   
    }
}