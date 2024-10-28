package vendingmachine.cc13.group1.database.product;

public class Product {

    private final int code;
    private int amount;
    private double price;
    private String name;
    private Category category;

    public Product(int code, int amount, double price, String name, Category category) {
        this.code = code;
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.category = category;
    }

    public int getCode() {
        return code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal(){
        return price*amount;
    }

    /**
     * Returns the product's attributes as a csv string array
     * 
     * @return a csv string array representing the product's attributes
     */
    String[] asCSVRecord() {
        return new String[]{ Integer.toString(code), Integer.toString(amount),
                Double.toString(price), name, category.toString() };
    }

    public String toString(){
        return String.format("ID:%s, Amount:%s, Price:$%s, Item:%s, Category:%s",code,amount,price,name,category.getTitle());
    }

    /**
     * Returns the header for a product CSV.
     * 
     * @return an array of strings for the header
     */
    static String[] getHeaders() {
        return new String[]{ "code", "amount", "price", "name", "category" };
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
        result = prime * result + amount;
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Product)) {
            return false;
        }
        Product other = (Product) obj;
        if (code != other.code) {
            return false;
        }
        if (amount != other.amount) {
            return false;
        }
        if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (category != other.category) {
            return false;
        }
        return true;
    }

}
