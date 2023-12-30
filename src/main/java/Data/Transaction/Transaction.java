package Data.Transaction;

public class Transaction {

    private String userID;
    private String transactionType;
    private String subcategory;
    private String timestamp;
    private String amount;
    private String description;

    public Transaction(String userID, String transactionType, String subcategory, String timestamp, String amount, String description) {
        this.userID = userID;
        this.transactionType = transactionType;
        this.subcategory = subcategory;
        this.timestamp = timestamp;
        this.amount = amount;
        this.description = description;
    }

    public String getUserID() {
        return userID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
