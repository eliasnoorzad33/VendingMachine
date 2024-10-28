package vendingmachine.cc13.group1.database.product;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ProductDatabaseTest {

    @BeforeEach
    void restDatabase() {
        ProductDatabase.getInstance("").reset();
    }

    @Test
    @Order(1)
    void testGetInstance() {
        assertThrows(UnintialisedException.class, () -> ProductDatabase.getInstance());
        assertNotNull(ProductDatabase.getInstance(""));
    }

    @Test
    @Order(2)
    void testProducts() {
        assertNotNull(ProductDatabase.getInstance("").products());
        assertEquals(0, ProductDatabase.getInstance().products().size());
        ProductDatabase.getInstance().reset();
        assertEquals(2, ProductDatabase.getInstance("src/test/resources/test_products.csv").products().size());
        Product ex = new Product(0, 10, 13.3, "Hello World", Category.DRINKS);
        assertEquals(ex, ProductDatabase.getInstance("src/test/resources/test_products.csv").products().get(0));
        ex = new Product(8,11,100.0,"Smiths",Category.CHIPS);
        assertEquals(ex, ProductDatabase.getInstance().products().get(8));
    }

    @Test
    void testSave() throws IOException {
        ProductDatabase.getInstance("src/test/resources/test_products_save.csv");
        ProductDatabase.getInstance().products().clear();
        Product ex = new Product(0, 10, 13.3, "Hello World", Category.DRINKS);
        Product ex2 = new Product(8,11,100.0,"Smiths",Category.CHIPS);
        ProductDatabase.getInstance().products().put(0, ex);
        ProductDatabase.getInstance().products().put(8, ex2);
        ProductDatabase.getInstance().save();
        File expected = new File("src/test/resources/test_products.csv");
        File actual = new File("src/test/resources/test_products_save.csv");
        assertTrue(FileUtils.contentEqualsIgnoreEOL(expected, actual, null));
    }

    @Test
    void testFromCategory() {
        ProductDatabase.getInstance("src/test/resources/test_products.csv");
        Product[] ex = new Product[] {new Product(8,11,100.0,"Smiths",Category.CHIPS)};
        Product[] actual = new Product[1];
        actual = ProductDatabase.getInstance().fromCategory(Category.CHIPS).toArray(actual);
        assertArrayEquals(ex, actual);
    }

    @Test
    void testCheckCodeExists() {
        ProductDatabase.getInstance("src/test/resources/test_products.csv");
        assertTrue(ProductDatabase.getInstance().checkCodeExists(8));
        assertFalse(ProductDatabase.getInstance().checkCodeExists(99));
    }
    
    @Test
    void testCheckNameExists() {
        ProductDatabase.getInstance("src/test/resources/test_products.csv");
        assertTrue(ProductDatabase.getInstance().checkNameExists("Hello World"));
        assertFalse(ProductDatabase.getInstance().checkNameExists("na"));
    }
    
    @Test
    void testCheckMaxQuantity() {
        ProductDatabase.getInstance("src/test/resources/test_products.csv");
        assertTrue(ProductDatabase.getInstance().checkMaxQuantity(22));
        assertFalse(ProductDatabase.getInstance().checkMaxQuantity(1));
    }

    @Test
    void testAddNewProduct() {
        ProductDatabase.getInstance("src/test/resources/test_products.csv");
        assertDoesNotThrow(() -> ProductDatabase.getInstance().addNewProduct(0, 0, 0, "NEW", Category.CANDIES));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().addNewProduct(1, 0, 0, "Smiths", Category.CANDIES));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().addNewProduct(2, 22, 0, "DIFF", Category.CANDIES));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().addNewProduct(3, 2, 1, "ADD", Category.DRINKS));
    }

    @Test
    void testFillProduct() {
        ProductDatabase.getInstance("src/test/resources/test_products.csv");
        assertDoesNotThrow(() -> ProductDatabase.getInstance().fillProduct(-1, 0));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().fillProduct(8, 20));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().fillProduct(8, 1));
    }

    @Test
    void testModifyProduct() {
        ProductDatabase.getInstance("src/test/resources/test_products.csv");
        assertDoesNotThrow(() -> ProductDatabase.getInstance().modifyProduct(0, null));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().modifyProduct(0, p -> {
            return null;
        }));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().modifyProduct(-1, p -> {
            return null;
        }));
        assertDoesNotThrow(() -> ProductDatabase.getInstance().modifyProduct(8, p -> {
            return p;
        }));
    }

}
