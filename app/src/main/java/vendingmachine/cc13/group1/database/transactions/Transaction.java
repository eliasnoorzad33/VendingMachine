package vendingmachine.cc13.group1.database.transactions;

import vendingmachine.cc13.group1.database.product.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    /**
     * - user
     * - transaction date and time
     * - item sold
     * - amount of money paid
     * - returned change
     * - payment method.
     */
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;


    private String username;

    private LocalDateTime dateTime;

    private List<Product> products = new ArrayList<Product>();

    private double amountPaid;

    private Payment payment;

    private boolean isCancelled = false;

    public String cancellationReason;
    public Transaction(String username, LocalDateTime dateTime, List<Product> products) {
        this.username = username;
        this.dateTime = dateTime;
        this.products = products;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setTransactions(List<Product> products) {
        this.products = products;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void cancel(){
        this.isCancelled = true;
    }

    public boolean isCancelled(){
        return isCancelled;
    }

    public void setReason(String reason){
        this.cancellationReason = reason;
    }

    public String getCancellationReason(){
        return this.cancellationReason;
    }

     /**
     * Returns the Transaction's attributes as a csv string array
     *
     * @return a csv string array representing the Transaction's attributes
     */
    String[] asCSVRecord() {
        String productRecord = "";
        for (int i = 0; i < products.size(); i++){
            productRecord += products.get(i).toString() + "\n";
        }
        return new String[]{ username, dateTime.format(formatter),
              productRecord, Double.toString(amountPaid), payment.toString() };
    }

    String[] asCancelledCSVRecord() {
        return new String[]{ dateTime.format(formatter), username, cancellationReason};
    }

    /**
     * Returns the header for a Transaction CSV.
     *
     * @return an array of strings for the header
     */
    public static String[] getHeaders() {
        return new String[]{ "username", "datetime", "products", "paid", "payment type" };
    }

    public static String[] getCancelledHeaders() {
        return new String[]{  "datetime", "username","reason"};
    }

    public double getAmountDue(){
        double amountDue = 0;
        for (Product product : products){
            amountDue += (product.getAmount() * product.getPrice());
        }
        return amountDue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) obj;

        if (Double.doubleToLongBits(amountPaid) != Double.doubleToLongBits(other.amountPaid)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }

        } else if (!username.equals(other.username)) {
            return false;
        }

        if (!products.equals(other.products)){
            return false;
        }

        if (!dateTime.equals(other.dateTime)){
            return false;
        }

        if (payment != other.payment) {
            return false;
        }
        return true;
    }


}

