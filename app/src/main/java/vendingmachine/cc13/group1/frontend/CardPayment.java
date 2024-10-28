package vendingmachine.cc13.group1.frontend;

import org.beryx.textio.*;
import java.util.function.BiConsumer;

import vendingmachine.cc13.group1.database.customer.*;
import vendingmachine.cc13.group1.database.purchases.CardChecker;
import vendingmachine.cc13.group1.frontend.transaction.IdleTimer;

public class CardPayment implements BiConsumer<TextIO, IdleTimer> {
    private String cardHolderName;
    private String cardNumber;

    CardChecker checker = new CardChecker();

    public void accept(TextIO textIO, IdleTimer it) {
        TextTerminal<?> terminal = textIO.getTextTerminal();

        // first check if logged in and has saved card details
        CustomerDatabase cDB = CustomerDatabase.getInstance();
        if (cDB.getLoggedIn() != null && cDB.getLoggedInAsObject().getCardNumber() != 0
                && cDB.getLoggedInAsObject().getCardholderName() != null) {
            String useSavedResponse = textIO.newStringInputReader().read(
                    "Would you like to use your saved card details? (yes/no):").toLowerCase();
            if (useSavedResponse.equals("yes") || useSavedResponse.equals("y")) {
                // Assumes saved details are correct
                // They should have been checked against the provided db when purchasing the first time
                // terminal.print("\n ---Transaction Approved--- \n");
                return;
            }
        }

        boolean validCard = false;

        //loop which continues to ask for input until a valid card number is entered
        while (validCard == false) {
            //reading the card name and card number from input
            cardHolderName = textIO.newStringInputReader().read("Enter the cardholder name:");
            it.reset();
            cardNumber = textIO.newStringInputReader().withInputMasking(true).read("Enter the card number:");
            it.reset();

            //removing whitespaces and other formatting issues
            cardNumber = cardNumber.replaceAll("\\s", "");


            if (checker.InJson(cardHolderName, cardNumber) == true) {
                validCard = true;

                // After transaction approved, check if logged in
                // If so, ask to save details
                if (cDB.getLoggedIn() != null) {
                    String saveResponse = textIO.newStringInputReader().read(
                            "Would you like to save these details to your account? (yes/no):").toLowerCase();
                    it.reset();

                    if (saveResponse.equals("yes") || saveResponse.equals("y")) {
                        Integer cardNumberInt = Integer.parseInt(cardNumber);
                        cDB.saveCardToLoggedInUser(cardHolderName, cardNumberInt);
                        terminal.print("Card details saved to your account. \n");
                    } else {
                        terminal.print("Card details not saved. \n");
                    }
                }
            } else {
                terminal.print("Invalid card. Please enter a valid credit card. \n");
            }
            it.reset();
        }
    }
}

