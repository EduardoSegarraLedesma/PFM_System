package Data.Investment;

public class ComparePurchase {

    private final String Symbol;
    private final int Quantity;
    private final Float buyPrice;
    private final Float nowPrice;
    private final Float difference;
    private final String TransactionDate;

    public ComparePurchase(String symbol, int quantity, Float buyPrice, Float nowPrice, String TransactionDate) {
        this.Symbol = symbol;
        this.Quantity = quantity;
        this.buyPrice = buyPrice;
        this.nowPrice = nowPrice;
        this.difference = (1 - (buyPrice / nowPrice)) * 100;
        this.TransactionDate = TransactionDate;
    }

    public String getSymbol() {
        return Symbol;
    }

    public int getQuantity() {
        return Quantity;
    }

    public Float getBuyPrice() {
        return buyPrice;
    }

    public Float getNowPrice() {
        return nowPrice;
    }

    public Float getDifference() {
        return difference;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }


}
