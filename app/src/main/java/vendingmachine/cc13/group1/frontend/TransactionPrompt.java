package vendingmachine.cc13.group1.frontend;

import org.beryx.textio.*;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.product.Product;
import vendingmachine.cc13.group1.database.product.ProductDatabase;
import vendingmachine.cc13.group1.database.product.UnintialisedException;
import vendingmachine.cc13.group1.database.transactions.Payment;
import vendingmachine.cc13.group1.database.transactions.Transaction;
import vendingmachine.cc13.group1.database.transactions.TransactionDatabase;
import vendingmachine.cc13.group1.frontend.transaction.IdleTimer;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TransactionPrompt implements Consumer<TextIO> {
    private String username;
    private ArrayList<Product> products;
    private IdleTimer timer;


    private TextTerminal<?> terminal;
    /**
     * @param filename The json array of transactions
     */
    public TransactionPrompt(String filename) {
        // just initialises the db if it isn't already
        TransactionDatabase.getInstance(filename);
    }

    @Override
    public void accept(TextIO textIO) {
        terminal = textIO.getTextTerminal();
        timer = new IdleTimer(textIO.getTextTerminal(), 120000);
        timer.start();

        username = null;

        try {
            CustomerDatabase.getInstance();
        }catch (UnintialisedException e){
            username = "anonymous";
        }

        //Database initialised and there is a user logged in
        if (username == null){
            username = CustomerDatabase.getInstance().getLoggedIn();
        }
        //Database initialised but user not logged in
        if (username == null){
            username = "anonymous";
        }

        products = new ArrayList<>();

        String keyStrokeAbort = "ctrl X";

        terminal.registerHandler(keyStrokeAbort,
                t -> new ReadHandlerData(ReadInterruptionStrategy.Action.ABORT).withPayload("user cancelled"));

        while (true) {
            try {
                String productCode = textIO.newStringInputReader().read("[(Pay) to continue to payment, Ctrl X to return to main menu] Type product code: ");
                
                timer.reset();

                if (productCode.equalsIgnoreCase("q")) {
                    cancelTransaction("user cancelled");
                    timer.reset();
                    timer.stop();
                    return;
                }

                if (productCode.equalsIgnoreCase("pay") && products.size() == 0) {
                    terminal.println("Please select a product before continuing");
                }

                if (productCode.equalsIgnoreCase("pay")) {
                    break;
                }

                if (!ProductDatabase.getInstance().checkCodeExists(Integer.parseInt(productCode))) {
                    terminal.println("Please enter a valid product code");
                    continue;
                }

                Product product = ProductDatabase.getInstance().products().get(Integer.parseInt(productCode));

                Product purchasedProduct = new Product(product.getCode(), product.getAmount(), product.getPrice(), product.getName(), product.getCategory());

                if (product.getAmount() == 0) {
                    terminal.println("That product is no longer in stock, please select another product");
                    continue;
                }

                String amount = textIO.newStringInputReader().read("[Ctrl X to return to main menu] Select amount to purchase: ");

                timer.reset();

                if (amount.equalsIgnoreCase("q")) {
                    cancelTransaction("user cancelled");
                    timer.reset();
                    timer.stop();
                    return;
                }

                try {
                    int amountPurchased = Integer.parseInt(amount);
                    if (amountPurchased > product.getAmount() || amountPurchased < 0) {
                        System.out.println(product.getAmount());
                        System.out.println(product.getAmount());
                        terminal.println("Please select a valid amount");
                        continue;
                    }
                    purchasedProduct.setAmount(amountPurchased);

                } catch (NumberFormatException e) {
                    terminal.println("Please input a valid number");
                    continue;
                }

                products.add(purchasedProduct);

                terminal.println("Product added to purchase list");
            }catch (ReadAbortedException e){
                cancelTransaction(e.getPayload());
                return;
            }
        }

        Transaction transaction = new Transaction(username, LocalDateTime.now(), products);

        int paymentChoice = 0;
        boolean validInput = false;
        while (validInput == false){
            try {
                paymentChoice = textIO.newIntInputReader().withMinVal(1).withMaxVal(2).read("[Ctrl X to return to main menu] Enter '1' to pay with card or '2' to pay with cash: \n");

                timer.reset();

                if (paymentChoice == 1) {
                    transaction.setPayment(Payment.CARD);
                    CardPayment card = new CardPayment();
                    card.accept(textIO, timer);
                    validInput = true;
                } else if (paymentChoice == 2) {
                    transaction.setPayment(Payment.CASH);
                    CashPayment cash = new CashPayment();
                    cash.setAmountDue(transaction.getAmountDue());
                    cash.accept(textIO, timer);
                    if (cash.getAmountPaid() == 0) {
                        cancelTransaction("change not available");
                        timer.reset();
                        timer.stop();
                        return;
                    }
                    transaction.setAmountPaid(cash.getAmountPaid());
                    TransactionDatabase.getInstance().addTransaction(transaction);
                    try {
                        ProductDatabase.getInstance().purchase(products);
                    } catch (IOException e) {
                        terminal.println("Failed to complete purchase");
                        timer.reset();
                        timer.stop();
                        return;
                    }
                    timer.reset();
                    timer.stop();
                    return;
                }
            }catch(ReadAbortedException e){
                cancelTransaction(e.getPayload());
                return;
            }
        }

        double amountDue = transaction.getAmountDue();

        while (true){
            try {
                String pay = textIO.newStringInputReader().read("Amount due: $" + amountDue + "\nConfirm payment? (Y) Yes (Ctrl X) No: ");

                timer.reset();

                if (pay.equalsIgnoreCase("y")) {
                    transaction.setAmountPaid(amountDue);
                    try {
                        ProductDatabase.getInstance().purchase(products);
                    } catch (IOException e) {
                        terminal.println("Failed to complete purchase");
                        timer.reset();
                        timer.stop();
                        return;
                    }
                }
                else {
                    terminal.println("Please input (Y) to confirm or Ctrl X to cancel transaction");
                    continue;
                }

                TransactionDatabase.getInstance().addTransaction(transaction);
                terminal.println("Transaction completed successfully");
                break;
            }catch (ReadAbortedException e){
                cancelTransaction(e.getPayload());
                return;
            }
        }
        timer.reset();
        timer.stop();
    }

    public void cancelTransaction(String reason){
            Transaction transaction = new Transaction(username, LocalDateTime.now(), products);
            transaction.cancel();
            transaction.setReason(reason);
            TransactionDatabase.getInstance().transactions().add(transaction);
            terminal.println("transaction cancelled - returning to main menu");
            try {
                    TransactionDatabase.getInstance().save();
                }
            catch (IOException e){
                    e.printStackTrace();
            }
            CustomerDatabase.getInstance().logout();
            timer.reset();
            timer.stop();
    }

}
