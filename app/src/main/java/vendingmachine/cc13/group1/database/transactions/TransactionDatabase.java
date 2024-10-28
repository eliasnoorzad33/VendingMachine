package vendingmachine.cc13.group1.database.transactions;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import vendingmachine.cc13.group1.database.product.Product;
import vendingmachine.cc13.group1.database.product.UnintialisedException;


/**
 * Interface for the database that stores the transaction.
 * Products are stored in a JSON file.
 *
 * @author Kyle Schmidt (ksch0312)
 */
public class TransactionDatabase {

    private static TransactionDatabase instance;

    public static TransactionDatabase getInstance(String filename) {
        if (instance == null) instance = new TransactionDatabase(filename);
        return instance;
    }

    public static TransactionDatabase getInstance() {
        if (instance == null) throw new UnintialisedException();
        else return instance;
    }

    private final String filename;
    private ArrayList<Transaction> transactions;

    /**
     * @param filename The path for the json file to store transactions
     */
    private TransactionDatabase(String filename) {
        this.filename = filename;

        try {
            this.transactions = read(filename);
        } catch (Exception e) {
            this.transactions = new ArrayList<>();
        }
    }

    //TODO: Change to convert JSON to CSV
    public void generateReport(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Transaction.getHeaders()).build();
        transactions = read(this.filename);
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            for (Transaction t : transactions){
                if (t.isCancelled()){continue;}
                try {
                    printer.printRecord(Arrays.asList(t.asCSVRecord()));
                } catch (IOException e) {}
            }
        }
        writer.close();
    }

    public void generateCancelledReport(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Transaction.getCancelledHeaders()).build();
        transactions = read(this.filename);
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            for (Transaction t : transactions){
                if (!t.isCancelled()){continue;}
                try {
                    printer.printRecord(Arrays.asList(t.asCancelledCSVRecord()));
                } catch (IOException e) {}
            }
        }
        writer.close();
    }

    private void saveToFile(String filename) throws FileNotFoundException {
        File output = new File(filename);
        OutputStream out = new FileOutputStream(output);
        TransactionDBWriter writer = new TransactionDBWriter();

        try {
            writer.writeJsonStream(out, transactions);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }


    private ArrayList<Transaction> read(String path) throws IOException{

        ArrayList<Transaction> transactionList = new ArrayList<>();

        try {
            // create a reader
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

            Reader reader = Files.newBufferedReader(Paths.get(path));

            // convert JSON array to list of transactions
            transactionList = gson.fromJson(reader, new TypeToken<ArrayList<Transaction>>() {}.getType());

            // close reader
            reader.close();

        } catch (JsonIOException ex) {
            ex.printStackTrace();
        }
        return transactionList;
    }

    public void save() throws IOException {
        saveToFile(filename);
    }

    public List<Transaction> transactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Product> getLastFiveProducts(String username) {
        // if username is null, just fetch last five from anyone
        
        ArrayList<Product> result = new ArrayList<Product>();
        // use the reverse of transactions - newest transactions appended at end
        ArrayList<Transaction> txnCopy = (ArrayList<Transaction>) transactions.clone();
        Collections.reverse(txnCopy);

        for (Transaction t : txnCopy) {
            if (t.isCancelled() || username != null && !t.getUsername().equals(username)
                || username == null && !t.getUsername().equals("anonymous")) {
                continue;
            }

            for (Product p : t.getProducts()) {
                // check if product already in result, add if not
                if (!result.contains(p)) {
                    result.add(p);
                    if (result.size() == 5) {
                        return result;
                    }
                }
            }
        }

        // less than 5 found, user responsible for checking size
        return result;
    }

    public void reset() {
        instance = null;
    }

}
