package vendingmachine.cc13.group1.frontend;

//import org.beryx.textio.TextIO;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import vendingmachine.cc13.group1.database.customer.CustomerDatabase;
import vendingmachine.cc13.group1.database.customer.UserRole;
import vendingmachine.cc13.group1.database.purchases.CashDatabase;
import vendingmachine.cc13.group1.database.purchases.CashRegister;
import vendingmachine.cc13.group1.frontend.transaction.IdleTimer;
import java.util.function.BiConsumer;

public class CashPayment implements BiConsumer<TextIO, IdleTimer> {

    //valid cash amounts
    int fiveCents = 5;
    int tenCents = 10;
    int twentyCents = 20;
    int fiftyCents = 50;
    int oneDollar = 100;
    int twoDollars = 200;
    int fiveDollars = 500;
    int tenDollars = 1000;
    int twentyDollars = 2000;
    int fiftyDollars = 5000;
    int amountPaid;
    int amountDue;
    int change;
    int amountRemaining;

    public void accept(TextIO textIO, IdleTimer it){
        CashDatabase cdb = new CashDatabase();
        TextTerminal<?> terminal = textIO.getTextTerminal();
        boolean paymentCompleted = false;
        String amountDueString = cdb.getAmountDueString(amountDue);
        terminal.printf("\nYour amount due is: $%s\n",amountDueString);
         while (paymentCompleted == false){
            String cashGiven = textIO.newStringInputReader().read("[Ctrl X to return to main menu] Enter the number corresponding to the amount of cash you wish to provide\n1: $0.05\n2: $0.10\n3: $0.20\n4: $0.50\n5: $1.00\n6: $2.00\n7: $5.00\n8: $10.00\n9: $20.00\n10: $50.00");
            it.reset();
            if (cashGiven.equals("1")){
                amountPaid += fiveCents;
            }
            else if (cashGiven.equals("2")){
                amountPaid += tenCents;
            }
            else if (cashGiven.equals("3")){
                amountPaid += twentyCents;
            }
            else if (cashGiven.equals("4")){
                amountPaid += fiftyCents;
            }
            else if (cashGiven.equals("5")){
                amountPaid += oneDollar;
            }
            else if (cashGiven.equals("6")){
                amountPaid += twoDollars;
            }
            else if (cashGiven.equals("7")){
                amountPaid += fiveDollars;
            }
            else if (cashGiven.equals("8")){
                amountPaid += tenDollars;
            }
            else if (cashGiven.equals("9")){
                amountPaid += twentyDollars;
            }
            else if (cashGiven.equals("10")) {
                amountPaid += fiftyDollars;
            }
            else if (cashGiven.equalsIgnoreCase("q")) {
                amountPaid = 0;
                return;
            }
            else{
                terminal.print("\nINVALID INPUT");
            }

            paymentCompleted = cdb.checkIfTotalPaid(amountPaid, amountDue);
            amountRemaining = cdb.getAmountRemaining(amountPaid, amountDue);
            if (amountRemaining>0){
                String amountRemainingString = cdb.getAmountRemainingString(amountRemaining);
                terminal.printf("\nAmount remaining: $%s\n",amountRemainingString);
            }
            else{
                change = cdb.calculateChangeAmount(amountPaid,amountDue);
                if (change!=0) {
                    String changeString = cdb.getChangeString(change);
                    String changeStringFull = cdb.changeReturned(change);
                    if (changeStringFull.equals("Sorry we don't have enough change to complete the purchase, please provide a different amount of cash")) {
                        terminal.print("Sorry we don't have enough change to complete the purchase, please provide a different amount of cash\n");
                        paymentCompleted = false;
                        amountRemaining = amountDue;
                        amountPaid = 0;
                    } else {
                        terminal.print("\n---FULL AMOUNT PAID---\n");
                        terminal.printf("\nYour change is $%s\n", changeString);
                        terminal.printf("\n%s\n", changeStringFull);
                    }
                } else {
                    terminal.print("\n---FULL AMOUNT PAID---\n");
                }
            }
         }
    }

    public void setCashInRegister(TextIO textIO){
        TextTerminal<?> terminal = textIO.getTextTerminal();

        CustomerDatabase cDB = CustomerDatabase.getInstance();
        if (cDB.getLoggedIn() == null) {
            terminal.printf("Error: Not available to anonymous users.\n");
            return;
        } else if (!(cDB.getLoggedInRole() == UserRole.OWNER || cDB.getLoggedInRole() == UserRole.CASHIER)) {
            terminal.printf("Error: Not available to users with role %s\n", cDB.getLoggedInRole().name());
            return;
        }

        int fiftyDollarCash = textIO.newIntInputReader().withMinVal(0).read("Enter the amount of $50 notes: ");
        int twentyDollarCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of $20 notes: ");
        int tenDollarCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of $10 notes: ");
        int fiveDollarCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of $5 notes: ");
        int twoDollarCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of $2 coins: ");
        int oneDollarCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of $1 coins: ");
        int fiftyCentCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of 50c coins: ");
        int twentyCentCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of 20c coins: ");
        int tenCentCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of 10c coins: ");
        int fiveCentCash = textIO.newIntInputReader().withMinVal(0).read("\nEnter the amount of 5c coins: ");
        int[] newCashQuantities = {fiftyDollarCash,twentyDollarCash,tenDollarCash,fiveDollarCash,twoDollarCash,oneDollarCash,fiftyCentCash,twentyCentCash,tenCentCash,fiveCentCash};
        CashRegister cr = new CashRegister();
        cr.changeCashInRegister(newCashQuantities);
    }

    public void setAmountDue(double amountDue){
        this.amountDue = (int)(amountDue * 100);
    }

    public double getAmountPaid(){return this.amountPaid/100;}
}
