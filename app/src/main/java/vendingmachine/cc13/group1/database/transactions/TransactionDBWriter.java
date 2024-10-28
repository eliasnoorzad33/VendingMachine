package vendingmachine.cc13.group1.database.transactions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

public class TransactionDBWriter {

    public void writeJsonStream(OutputStream out, List<Transaction> transactions) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writeTransactionArray(writer, transactions);
        writer.setIndent("    ");
        writer.close();
    }

    private void writeTransactionArray(JsonWriter writer, List<Transaction> transactions) throws IOException{
        writer.beginArray();
        for (Transaction transaction : transactions){
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            var jsonString = gson.toJson(transaction);
            writer.jsonValue(jsonString);
        }
        writer.endArray();
    }
}
