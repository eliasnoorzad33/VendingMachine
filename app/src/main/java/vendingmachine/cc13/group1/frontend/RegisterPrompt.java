package vendingmachine.cc13.group1.frontend;

import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;


public class RegisterPrompt implements Consumer<TextIO> {

    /**
     * @param customerPasswordsFilename The csv list of usernames/passwords
     */
    public RegisterPrompt(String customerPasswordsFilename, String customerCardsFilename) {
        // just initialises the db if it isn't already
        CustomerDatabase.getInstance(customerPasswordsFilename, customerCardsFilename);
    }

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();
        CustomerDatabase cDB = CustomerDatabase.getInstance();

        String username = textIO.newStringInputReader().read("Username");

        // check if uname exists

        if (cDB.usernames().contains(username)) {
            terminal.println("Username already exists");
            return;
        }

        String password = textIO.newStringInputReader().withInputMasking(true).read("Password");

        // Add to customer db
        cDB.addCustomer(username, password);
        cDB.setLoggedIn(username);
        terminal.printf("Successfully registered as %s, you are now logged in.\n", username);

    }
    
}
