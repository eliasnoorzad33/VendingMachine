package vendingmachine.cc13.group1.database.product;

public enum Category {

    DRINKS("Drinks"),
    CHOCOLATES("Chocolates"),
    CHIPS("Chips"),
    CANDIES("Candies");

    private String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
}
