package vendingmachine.cc13.group1.database.purchases;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CashRegister {

    int[] cashInRegisterList = new int[10];

    public int[] getCashInRegister(){
        try{
            FileReader cashRegisterFile = new FileReader("CashRegister.json");
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(cashRegisterFile);

            for (Object details:json){
                JSONObject cashInRegister = (JSONObject) details;
                cashInRegisterList[0] = (int) ((long) cashInRegister.get("5000"));
                cashInRegisterList[1] = (int) ((long) cashInRegister.get("2000"));
                cashInRegisterList[2] = (int) ((long) cashInRegister.get("1000"));
                cashInRegisterList[3] = (int) ((long) cashInRegister.get("500"));
                cashInRegisterList[4] = (int) ((long) cashInRegister.get("200"));
                cashInRegisterList[5] = (int) ((long) cashInRegister.get("100"));
                cashInRegisterList[6] = (int) ((long) cashInRegister.get("50"));
                cashInRegisterList[7] = (int) ((long) cashInRegister.get("20"));
                cashInRegisterList[8] = (int) ((long) cashInRegister.get("10"));
                cashInRegisterList[9] = (int) ((long) cashInRegister.get("5"));

                return cashInRegisterList;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public void changeCashInRegister(int[] cashQuantities){

        JSONArray array = new JSONArray();
        JSONObject cashInRegister = new JSONObject();
        cashInRegister.put("5000",cashQuantities[0]);
        cashInRegister.put("2000",cashQuantities[1]);
        cashInRegister.put("1000",cashQuantities[2]);
        cashInRegister.put("500",cashQuantities[3]);
        cashInRegister.put("200",cashQuantities[4]);
        cashInRegister.put("100",cashQuantities[5]);
        cashInRegister.put("50",cashQuantities[6]);
        cashInRegister.put("20",cashQuantities[7]);
        cashInRegister.put("10",cashQuantities[8]);
        cashInRegister.put("5",cashQuantities[9]);
        array.add(cashInRegister);

        try{
            FileWriter file = new FileWriter("CashRegister.json");
            file.write(array.toJSONString());
            file.flush();
            file.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateReport(String filename, int[] quantities) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(new String[]{ "Note/Coin", "Quantity"}).build();
        CSVPrinter printer = new CSVPrinter(writer, format);
        String[] noteAndCoinNames = new String[] {"5c", "10c", "20c", "50c", "$1", "$2", "$5", "$10", "$20", "$50"};
        for (int i = 0; i < 10; i++) {
            try {
                printer.printRecord(Arrays.asList(new String[]{noteAndCoinNames[i], Integer.toString(quantities[9-i])}));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        printer.close();
        writer.close();
    }
}
