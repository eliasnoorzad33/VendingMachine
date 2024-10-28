package vendingmachine.cc13.group1.frontend;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

/**
 * Prints the registered commands and descriptions as a help menu
 * 
 * @author Darin Huang (dhua3771)
 */
public class HelpMenu implements Consumer<TextIO> {

    private HashMap<String, String> helpMenu;

    public HelpMenu() {
        this.helpMenu = new HashMap<>();
    }

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();
        terminal.println("---------------------------------------------");
        terminal.println("\t\tHELP MENU:\n");
        helpMenu.forEach((cmd, desc) -> {
            terminal.printf("%-12s\t%s\n", cmd, desc);
        });
        terminal.println("---------------------------------------------");
    }

    /**
     * Register a command and a description for that command.
     * 
     * @param cmd the string command
     * @param desc the description for the command
     * 
     * @return the current instance - for method chaining
     */
    public HelpMenu registerCmdHelp(String cmd, String desc) {
        this.helpMenu.put(cmd, desc);
        return this;
    }

    /**
     * @return a collection of the registered commands 
     */
    public Collection<String> getCmds() {
        return helpMenu.keySet();
    }
    
}
