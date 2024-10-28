package vendingmachine.cc13.group1.database.purchases;

public class CashDatabase {

    public boolean checkIfTotalPaid(int amountPaid, int amountDue){
        if (amountPaid>=amountDue){
           return true;
        }
        else {
            return false;
        }
    }

    public String getAmountDueString(int amountDue){
        String amountDueInitial = String.valueOf(amountDue);
        String subString1 = amountDueInitial.substring(0,amountDueInitial.length()-2);
        String subString2 = amountDueInitial.substring(amountDueInitial.length()-2);
        String amountDueFinal = subString1+"."+subString2;

        return amountDueFinal;
    }

    public int getAmountRemaining(int amountPaid, int amountDue){
        if (amountPaid>=amountDue){
            return 0;
        }
        else {
            return amountDue-amountPaid;
        }
    }

    public String getAmountRemainingString(int amountRemaining){
        String amountRemainingInitial = String.valueOf(amountRemaining);
        String subString1 = amountRemainingInitial.substring(0,amountRemainingInitial.length()-2);
        String subString2 = amountRemainingInitial.substring(amountRemainingInitial.length()-2);
        String amountRemainingFinal = subString1+"."+subString2;

        return amountRemainingFinal;
    }

    public int calculateChangeAmount(int amountPaid, int amountDue){
        if (amountPaid>amountDue){
            return amountPaid-amountDue;
        }
        else{
            return 0;
        }
    }

    public String getChangeString(int change){
        String changeStringInitial = String.valueOf(change);
        String subString1 = changeStringInitial.substring(0,changeStringInitial.length()-2);
        String subString2 = changeStringInitial.substring(changeStringInitial.length()-2);
        String changeStringFinal = subString1+"."+subString2;

        return changeStringFinal;
    }

    public String changeReturned(int change){
        CashRegister register = new CashRegister();
        int[] cashQuantity = register.getCashInRegister();
        int[] cashValues = new int[]{5000,2000,1000,500,200,100,50,20,10,5};
        int[] quantityReturned = new int[]{0,0,0,0,0,0,0,0,0,0};


        for(int i=0; i<cashValues.length; i++){
            while (change >= cashValues[i] && cashQuantity[i] > 0){
                change -= cashValues[i];
                cashQuantity[i] -= 1;
                quantityReturned[i] += 1;
            }
        }

        if (change>0){
            return "Sorry we don't have enough change to complete the purchase, please provide a different amount of cash";
        }
        else{
            register.changeCashInRegister(cashQuantity);
            String changeReturnedString = "";
            for(int i=0; i<quantityReturned.length;i++)
            if (quantityReturned[i] > 0){
                if (cashValues[i]>200){
                    String notesString = "you recieved " + quantityReturned[i] +" $" + (cashValues[i]/100) + " notes\n";
                    changeReturnedString += notesString;
                }
                else if (cashValues[i]>=100 && cashValues[i]<=200){
                    String coinString = "you recieved " + quantityReturned[i] +" $" + (cashValues[i]/100) + " coins\n";
                    changeReturnedString += coinString;
                }
                else {
                    String centString = "you recieved " + quantityReturned[i] + " " + cashValues[i] + "c coins\n";
                    changeReturnedString += centString;
                }
            }
            return changeReturnedString;
        }
    }
}
