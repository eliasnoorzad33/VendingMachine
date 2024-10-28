package vendingmachine.cc13.group1.frontend;

import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;


public class LogoutMessage implements Consumer<TextIO> {

    
    public LogoutMessage() {}

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();
        CustomerDatabase cDB = CustomerDatabase.getInstance();

        String wasLoggedIn = cDB.getLoggedIn();
        if (wasLoggedIn == null) {
            terminal.println("Error: not logged in");
            return;
        }
        cDB.logout();
        terminal.printf("Successfully logged out. Goodbye, %s.\n", wasLoggedIn);   
    }
}