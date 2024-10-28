package vendingmachine.cc13.group1.database.purchases;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardCheckerTest {
    @Test
    void testInJson(){
        CardChecker cc = new CardChecker();
        assertEquals(true, cc.InJson("Charles","40691"));
    }

    @Test
    void testNotInJson(){
        CardChecker cc = new CardChecker();
        assertEquals(false,cc.InJson("jimbojoe", "6699"));
    }
}