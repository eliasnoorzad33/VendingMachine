package vendingmachine.cc13.group1.database.transactions;

public enum Payment {

    CASH("Cash"),
    CARD("Card");

    private String title;

    Payment(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
