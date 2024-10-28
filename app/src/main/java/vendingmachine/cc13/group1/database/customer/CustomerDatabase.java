package vendingmachine.cc13.group1.database.customer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import vendingmachine.cc13.group1.database.product.UnintialisedException;

public class CustomerDatabase { 

    private static CustomerDatabase instance;

    public static CustomerDatabase getInstance(String customerPasswordsFilename, String customerCardsFilename) {
        if (instance == null) instance = new CustomerDatabase(customerPasswordsFilename, customerCardsFilename);
        return instance;
    }

    public static CustomerDatabase getInstance() {
        if (instance == null) throw new UnintialisedException();
        else return instance;
    } 

    private final String customerPasswordsFilename;
    private final String customerCardsFilename;
    private ArrayList<Customer> customers;
    private Customer loggedInCustomer;

    /**
     * @param customerPasswordsFilename
     */
    private CustomerDatabase(String customerPasswordsFilename, String customerCardsFilename) {
        this.customerPasswordsFilename = customerPasswordsFilename;
        this.customerCardsFilename = customerCardsFilename;
        try {
            this.customers = readCustomerPasswords();
        } catch (IOException e) {
            this.customers = new ArrayList<>();
        }
        try {
            readSavedCardDetails();
        } catch (IOException e) {}
        this.loggedInCustomer = null;
    }

    // This file stores roles as well as passwords
    private ArrayList<Customer> readCustomerPasswords() throws IOException {
        ArrayList<Customer> read = new ArrayList<>();
        FileReader reader = new FileReader(customerPasswordsFilename);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Customer.getHeaders())
                .setSkipHeaderRecord(true).build();
        Iterable<CSVRecord> records = format.parse(reader);
        for (CSVRecord r : records) {
            Customer newUser = new Customer(r.get("username"), r.get("password"));
            newUser.setRole(UserRole.valueOf(r.get("role")));
            read.add(newUser);
        }

        return read;
    }

    private void writeCustomerPasswords(ArrayList<Customer> customers) throws IOException {
        
        FileWriter writer = new FileWriter(customerPasswordsFilename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Customer.getHeaders()).build();
        
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            customers.forEach(c -> {
                try {
                    printer.printRecord(Arrays.asList(c.asCSVRecord()));
                } catch (IOException e) {
                }
            });
        }
        writer.close();
    }

    // After customer objects constructed, read card details from the appropriate file and fill in

    private void readSavedCardDetails() throws IOException {
        FileReader reader = new FileReader(customerCardsFilename);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Customer.getCardDetailsHeaders())
                .setSkipHeaderRecord(true).build();
        Iterable<CSVRecord> records = format.parse(reader);
        for (CSVRecord r : records) {
            // find the appropriate user by username and add the card details
            for (Customer c : customers) {
                if (c.getUsername().equals(r.get("username"))) {
                    String cardholderName = r.get("cardholderName");
                    int cardNumber = Integer.parseInt(r.get("cardNumber"));
                    c.setCardholderName(cardholderName);
                    c.setCardNumber(cardNumber);
                }
            }
        }
    }

    private void writeSavedCardDetails(ArrayList<Customer> customers) throws IOException {
        FileWriter writer = new FileWriter(customerCardsFilename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Customer.getCardDetailsHeaders()).build();
        
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            customers.forEach(c -> {
                if (c.getCardholderName() != null && c.getCardNumber() != 0) {
                    try {
                        printer.printRecord(Arrays.asList(c.asCardDetailsCSVRecord()));
                    } catch (IOException e) {}
                }
            });
        }
        writer.close();
    }

    public void save() throws IOException {
        writeCustomerPasswords(customers);
        writeSavedCardDetails(customers);
    }

    public void addCustomer(String username, String password) {
        // Default UserRole is CUSTOMER
        customers.add(new Customer(username, password));
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSeller(String username, String password) {
        Customer newSeller = new Customer(username, password);
        newSeller.setRole(UserRole.SELLER);
        customers.add(newSeller);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCashier(String username, String password) {
        Customer newCashier = new Customer(username, password);
        newCashier.setRole(UserRole.CASHIER);
        customers.add(newCashier);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addOwner(String username, String password) {
        Customer newOwner = new Customer(username, password);
        newOwner.setRole(UserRole.OWNER);
        customers.add(newOwner);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String username) {
        // for use as Owner for removing any role
        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {
                customers.remove(c);
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    public ArrayList<Customer> customers() {
        return customers;
    }

    public ArrayList<String> usernames() {
        ArrayList<String> usernames = new ArrayList<>();
        for (Customer c : customers) {
            usernames.add(c.getUsername());
        }
        return usernames;
    }

    public void reset() {
        instance = null;
    }

    public String getLoggedIn() {
        if (loggedInCustomer == null) {
            return null;
        }
        return loggedInCustomer.getUsername();
    }

    public UserRole getLoggedInRole() {
        if (loggedInCustomer == null) {
            return null;
        }
        return loggedInCustomer.getRole();
    }

    public Customer getLoggedInAsObject() {
        if (loggedInCustomer == null) {
            return null;
        }
        return loggedInCustomer;
    }

    public void setLoggedIn(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {
                loggedInCustomer = c;
                return;
            }
        }
    }

    public void saveCardToLoggedInUser(String cardholderName, int cardNumber) {
        loggedInCustomer.setCardholderName(cardholderName);
        loggedInCustomer.setCardNumber(cardNumber);
        try {
            save();
        } catch (IOException e) {}
    }

    public void generateReport(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename, false);
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader(Customer.getHeadersForRoleReport()).build();
        try (CSVPrinter printer = new CSVPrinter(writer, format)) {
            customers.forEach(c -> {
                try {
                    printer.printRecord(Arrays.asList(c.asCSVRecordForRoleReport()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        writer.close();
    }

    public boolean userExists(String username){
        for (Customer customer : customers){
            if (customer.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public void logout() {
        loggedInCustomer = null;
    }


}
