package vendingmachine.cc13.group1.database.customer;

public class Customer {
    // Would be more accurately called "User" with different roles,
    // but can't be bothered to rename everywhere in the codebase

    private String username;
    private String password;
    // card details
    private String cardholderName;
    private int cardNumber;
    // Might be slightly confusing - all users are instances of Customer class, but can have extra permissions
        // Can other roles still buy like a customer?
        // If not, simply add a check before starting a transaction
    private UserRole role;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        this.cardholderName = null;
        this.cardNumber = 0;
        this.role = UserRole.CUSTOMER;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserRole getRole() {
        return role; 
    }

    /**
     * Returns the product's attributes as a csv string array
     * 
     * @return a csv string array representing the product's attributes
     */
    String[] asCSVRecord() {
        return new String[]{ username, password, role.name() };
    }

    // Headers for username/passsword file
    static String[] getHeaders() {
        return new String[]{ "username", "password", "role" };
    }

    String[] asCardDetailsCSVRecord() {
        if (cardholderName == null || cardNumber == 0) {
            return null;
        }
        return new String[]{ username, cardholderName, Long.toString(cardNumber) };
    }

    static String[] getCardDetailsHeaders() {
        return new String[]{ "username", "cardholderName", "cardNumber" };
    }

    String[] asCSVRecordForRoleReport() {
        return new String[]{username, role.name()};
    }

    static String[] getHeadersForRoleReport() {
        return new String[]{ "username", "role" };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) obj;
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }

        return true;
    }

}