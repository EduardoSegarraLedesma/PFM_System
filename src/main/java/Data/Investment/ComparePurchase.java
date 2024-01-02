package Data.Investment;

public class ComparePurchase {

    private final String Symbol;
    private final String Quantity;
    private final String buyPrice;
    private final String nowPrice;
    private final String difference;
    private final String TransactionDate;

    public ComparePurchase(String symbol, String quantity, String buyPrice, String nowPrice, String difference, String TransactionDate) {
        this.Symbol = symbol;
        this.Quantity = quantity;
        this.buyPrice = buyPrice;
        this.nowPrice = nowPrice;
        this.difference = difference;
        this.TransactionDate = TransactionDate;
    }

    public String getSymbol() {
        return Symbol;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public String getDifference() {
        return difference;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

}
