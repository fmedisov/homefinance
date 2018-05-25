package ru.medisov.home_finance.dao.model;

public class CurrencyModel extends IdModel {
    private String name;
    private String code;
    private String symbol;

    public CurrencyModel() {}

    public CurrencyModel(String name, String code, String symbol) {
        this.name = name;
        this.code = code;
        this.symbol = symbol;
    }

    public CurrencyModel(long id, String name, String code, String symbol) {
        this(name, code, symbol);
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "CurrencyModel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
