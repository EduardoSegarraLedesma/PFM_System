package Data.Investment;

public class Sell {
    private final String Id;
    private final String Symbol;
    private final String TransactionDate;

    public Sell(String id, String symbol, String transactionDate) {
        Id = id;
        Symbol = symbol;
        TransactionDate = transactionDate;
    }

    public String getId() {
        return Id;
    }

    public String getSymbol() {
        return Symbol;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

}