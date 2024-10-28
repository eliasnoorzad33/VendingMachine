package vendingmachine.cc13.group1.database.product;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    void testAsCSVRecord() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        String[] ex = new String[] {"0","1","2.0","test","CHIPS"};
        assertArrayEquals(ex, p.asCSVRecord());
    }
    
    @Test
    void testEquals() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        Product p2 = new Product(0, 1, 2.0, "test", Category.CHIPS);
        assertTrue(p.equals(p));
        assertTrue(p.equals(p2));
        assertFalse(p.equals(null));
        assertFalse(p.equals("p2"));
        assertFalse(p.equals(new Product(1, 1, 2.0, "test", Category.CHIPS)));
        assertFalse(p.equals(new Product(0, 2, 2.0, "test", Category.CHIPS)));
        assertFalse(p.equals(new Product(0, 1, 2.1, "test", Category.CHIPS)));
        assertFalse(p.equals(new Product(0, 1, 2.0, "tt", Category.CHIPS)));
        assertFalse(p.equals(new Product(0, 1, 2.0, "test", Category.DRINKS)));
        assertFalse(p.equals(new Product(0, 1, 2.0, null, Category.DRINKS)));
        p = new Product(0, 1, 2.0, null, Category.CHIPS);
        assertFalse(p.equals(new Product(0, 1, 2.0, null, Category.DRINKS)));
        assertFalse(p.equals(new Product(0, 1, 2.0, "test", Category.CHIPS)));
    }

    @Test
    void testGetAmount() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        assertEquals(1, p.getAmount());
    }

    @Test
    void testGetCategory() {
        Product product = new Product(1001, 2, 15.0, "Lays", Category.CHIPS);
        assertEquals(product.getCategory().getTitle(), "Chips");
    }

    @Test
    void testGetCode() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        assertEquals(0, p.getCode());
    }

    @Test
    void testGetHeaders() {
        String[] ex = new String[] {"code", "amount", "price", "name", "category"};
        assertArrayEquals(ex, Product.getHeaders());
    }

    @Test
    void testGetName() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        assertEquals("test", p.getName());
    }

    @Test
    void testGetPrice() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        assertEquals(2.0, p.getPrice());
    }

    @Test
    void testGetTotal() {
        Product product = new Product(1001, 2, 15.0, "Lays", Category.CHIPS);
        assertEquals(product.getTotal(), 30);
    }

    @Test
    void testHashCode() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        assertNotNull(p.hashCode());
        p = new Product(0, 1, 2.0, null, Category.CHIPS);
        assertNotNull(p.hashCode());
        p = new Product(0, 1, 2.0, "test", null);
        assertNotNull(p.hashCode());
    }

    @Test
    void testSetAmount() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        p.setAmount(2);
        assertEquals(2, p.getAmount());
    }

    @Test
    void testSetCategory() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        p.setCategory(Category.CANDIES);
        assertEquals(Category.CANDIES, p.getCategory());
    }

    @Test
    void testSetName() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        p.setName("haha");
        assertEquals("haha", p.getName());
    }

    @Test
    void testSetPrice() {
        Product p = new Product(0, 1, 2.0, "test", Category.CHIPS);
        p.setPrice(2.5);
        assertEquals(2.5, p.getPrice());
    }
}
