package vendingmachine.cc13.group1.database.purchases;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashDatabaseTest {
    @Test
    void testCheckIfTotalPaidTrue(){
        CashDatabase cd = new CashDatabase();
        assertEquals(true, cd.checkIfTotalPaid(55,53));
    }

    @Test
    void testCheckIfTotalPaidFalse(){
        CashDatabase cd = new CashDatabase();
        assertEquals(false,cd.checkIfTotalPaid(47,53));
    }

    @Test
    void testGetAmountDueString(){
        CashDatabase cd = new CashDatabase();
        assertEquals("52.86", cd.getAmountDueString(5286));
    }

    @Test
    void testGetAmountRemaining(){
        CashDatabase cd = new CashDatabase();
        assertEquals(22, cd.getAmountRemaining(34,56));
    }

    @Test
    void testGetAmountRemainingZero(){
        CashDatabase cd = new CashDatabase();
        assertEquals(0, cd.getAmountRemaining(56,56));
    }

    @Test
    void testGetAmountRemainingString(){
        CashDatabase cd = new CashDatabase();
        assertEquals("43.23",cd.getAmountRemainingString(4323));
    }

    @Test
    void testCalculateChangeAmount(){
        CashDatabase cd = new CashDatabase();
        assertEquals(1,cd.calculateChangeAmount(96,95));
    }

    @Test
    void testCalculateChangeAmountZero(){
        CashDatabase cd = new CashDatabase();
        assertEquals(0,cd.calculateChangeAmount(34,35));
    }

    @Test
    void testGetChangeString(){
        CashDatabase cd = new CashDatabase();
        assertEquals("21.45",cd.getChangeString(2145));
    }
}