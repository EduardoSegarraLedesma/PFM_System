package Data.Investment;

public class Sell {
    private final String Id;
    private final String Symbol;
    private final int Quantity;

    public Sell(String id, String symbol, int quantity) {
        Id = id;
        Symbol = symbol;
        Quantity = quantity;
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

}