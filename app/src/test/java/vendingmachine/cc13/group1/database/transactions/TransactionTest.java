package vendingmachine.cc13.group1.database.transactions;

import org.junit.jupiter.api.Test;
import vendingmachine.cc13.group1.database.product.Category;
import vendingmachine.cc13.group1.database.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionTest {

    @Test
    public void testTransactionCancel(){
        Transaction transaction = new Transaction("username", LocalDateTime.now(), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 3.5,"Pringles", Category.CHIPS)
        )));
        transaction.cancel();
        assertTrue(transaction.isCancelled());
    }

    @Test
    public void testTransactionGetUsername(){
        Transaction transaction = new Transaction("testuser", LocalDateTime.now(), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 7,"Lays", Category.CHIPS)
        )));
        assertEquals("testuser", transaction.getUsername());
    }

    @Test
    public void testTransactionGetDateTime(){
        Transaction transaction = new Transaction("testdatetime", LocalDateTime.of(2022, 11, 25,12,30,0,0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 7,"Lays", Category.CHIPS)
        )));
        assertEquals(LocalDateTime.of(2022, 11, 25,12,30,0,0), transaction.getDateTime());
    }

    @Test
    public void testTransactionGetProducts(){
        ArrayList<Product> expected =  new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 7,"Lays", Category.CHIPS), new Product(1003, 1, 6,"Coke", Category.DRINKS)
        ));

        Transaction transaction = new Transaction("testgetproducts", LocalDateTime.of(2022, 11, 25,12,30,0,0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 7,"Lays", Category.CHIPS), new Product(1003, 1, 6,"Coke", Category.DRINKS)
        )));
        assertEquals(expected, transaction.getProducts());
    }

    @Test
    public void testTransactionGetPayment(){

        Transaction transaction = new Transaction("testpayment", LocalDateTime.of(2022, 11, 25,12,30,0,0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 7,"Lays", Category.CHIPS), new Product(1003, 1, 6,"Coke", Category.DRINKS)
        )));

        transaction.setPayment(Payment.CARD);

        assertEquals(Payment.CARD, transaction.getPayment());
    }

    @Test
    public void testTransactionCancellationReason(){

        Transaction transaction = new Transaction("testpayment", LocalDateTime.of(2022, 11, 25,12,30,0,0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 7,"Lays", Category.CHIPS), new Product(1003, 1, 6,"Coke", Category.DRINKS)
        )));

        transaction.cancel();
        transaction.setReason("user cancelled");

        assertEquals("user cancelled", transaction.getCancellationReason());
    }

    @Test
    public void testTransactionAmountPaid(){

        Transaction transaction = new Transaction("testamountpaid", LocalDateTime.of(2022, 11, 25,12,30,0,0), new ArrayList<Product>(Arrays.asList(
                new Product(1002, 2, 7,"Lays", Category.CHIPS), new Product(1003, 1, 6,"Coke", Category.DRINKS)
        )));

        transaction.setAmountPaid(15);

        assertEquals(15, transaction.getAmountPaid());
    }





}
