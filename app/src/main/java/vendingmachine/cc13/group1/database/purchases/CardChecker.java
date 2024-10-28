package vendingmachine.cc13.group1.database.purchases;

import org.json.simple.parser.*;
import org.json.simple.*;


import java.io.FileReader;
import java.io.IOException;

public class CardChecker {

    public boolean InJson(String name, String cardNumber){
        try{
            FileReader CardsFile = new FileReader("credit_cards.json");
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(CardsFile);

            for (Object details:json){
                JSONObject cardDetails = (JSONObject) details;
                String storedNameLower = cardDetails.get("name").toString().toLowerCase();
                if (storedNameLower.equals(name.toLowerCase()) && cardDetails.get("number").equals(cardNumber)){
                    return true;
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
