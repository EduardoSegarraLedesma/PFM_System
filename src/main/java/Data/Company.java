package Data;

public class Company {

    private final String companyCode;
    private final String marketCap;
    private final String stockPrice;
    private final String lastDayVariation;
    private final String companyName;

    public Company(String companyCode, String marketCap, String stockPrice, String lastDayVariation, String companyName) {
        this.companyCode = companyCode;
        this.marketCap = marketCap;
        this.stockPrice = stockPrice;
        this.lastDayVariation = lastDayVariation;
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public String getLastDayVariation() {
        return lastDayVariation;
    }

    public String getCompanyName() {
        return companyName;
    }

}
