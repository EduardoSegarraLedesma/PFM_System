package Data.Investment;

public class Sell {
    private final String Id;
    private final String Symbol;
    private final int Quantity;
    private final String TransactionDate;

    public Sell(String id, String symbol, int quantity, String transactionDate) {
        Id = id;
        Symbol = symbol;
        Quantity = quantity;
        TransactionDate = transactionDate;
    }

    public String getId() {
        return Id;
    }

    public String getSymbol() {
        return Symbol;
    }

    public int getQuantity() {
        return Quantity;
    }
    public String getTransactionDate() {
        return TransactionDate;
    }

}