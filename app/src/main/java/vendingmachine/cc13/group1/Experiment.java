package vendingmachine.cc13.group1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.beryx.textio.ReadAbortedException;
import org.beryx.textio.ReadHandlerData;
import org.beryx.textio.ReadInterruptionStrategy;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.frontend.transaction.IdleTimer;

/**
 * An experiment to test whether the text-io dependency worked. 
 * This is the shopping list exmaple demo from the module's GitHub repository.
 * https://github.com/beryx/text-io/blob/master/text-io-demo/src/main/java/org/beryx/textio/demo/app/ShoppingList.java
 */
public class Experiment implements Consumer<TextIO> {

    private IdleTimer timer;

    /**
     * @param timer
     */
    public Experiment(IdleTimer timer) {
        this.timer = timer;
    }

    @Override
    public void accept(TextIO textIO) {
        TextTerminal<?> terminal = textIO.getTextTerminal();
        
        String keyStrokeReboot = "ctrl H";
        String keyStrokeAutoValue = "ctrl S";
        String keyStrokeHelp = "ctrl U";
        String keyStrokeAbort = "alt X";

        boolean registeredReboot = terminal.registerHandler(keyStrokeReboot, t -> {
            JOptionPane optionPane = new JOptionPane("System reboot in 5 minutes!", JOptionPane.WARNING_MESSAGE);
            JDialog dialog = optionPane.createDialog("REBOOT");
            dialog.setModal(true);
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            dialog.dispose();
            return new ReadHandlerData(ReadInterruptionStrategy.Action.ABORT);
        });

        boolean registeredAutoValue = terminal.registerHandler(keyStrokeAutoValue, t -> {
            terminal.println();
            return new ReadHandlerData(ReadInterruptionStrategy.Action.RETURN)
                    .withReturnValueProvider(partialInput -> partialInput.isEmpty() ? "nothing" : "high-quality-" + partialInput);
        });

        boolean registeredHelp = terminal.registerHandler(keyStrokeHelp, t -> {
            terminal.executeWithPropertiesPrefix("help",
                    tt -> tt.print("\n\nType the name of a product to be included in your shopping list."));
            return new ReadHandlerData(ReadInterruptionStrategy.Action.RESTART).withRedrawRequired(true);
        });

        boolean registeredAbort = terminal.registerHandler(keyStrokeAbort,
                t -> new ReadHandlerData(ReadInterruptionStrategy.Action.ABORT)
                        .withPayload(System.getProperty("user.name", "nobody")));

        boolean hasHandlers = registeredReboot || registeredAutoValue || registeredHelp || registeredAbort;
        if(!hasHandlers) {
            terminal.println("No handlers can be registered.");
        } else {
            terminal.println("--------------------------------------------------------------------------------");
            if(registeredReboot) {
                terminal.println("Press " + keyStrokeReboot + " to display a 'reboot' message box");
            }
            if(registeredAutoValue) {
                terminal.println("Press " + keyStrokeAutoValue + " to provide a product name based on the current input text");
            }
            if(registeredHelp) {
                terminal.println("Press " + keyStrokeHelp + " to print a help message");
            }
            if(registeredAbort) {
                terminal.println("Press " + keyStrokeAbort + " to abort the program");
            }
            terminal.println("You can use these key combinations at any moment during your data entry session.");
            terminal.println("--------------------------------------------------------------------------------");

            List<String> products = new ArrayList<>();
            timer.start();
            while(true) {
                String product = "";
                try {
                    product = textIO.newStringInputReader().withPropertiesPrefix("product").read("product");
                    timer.reset();
                } catch (ReadAbortedException e) {
                    terminal.executeWithPropertiesPrefix("abort",
                            // t -> t.println("\nRead aborted by user " + e.getPayload()));
                            t -> t.println("\n"+e.getPayload()));
                    break;
                }
                products.add(product);
                String content = products.stream().collect(Collectors.joining(", "));
                terminal.executeWithPropertiesPrefix("content", t ->t.println("Your shopping list contains: " + content));
                terminal.println();
            }
        }

        textIO.newStringInputReader().withMinLength(0).read("\nPress enter to terminate...");
        textIO.dispose();
    }
    
}
