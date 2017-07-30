package com.gautamastudios.currencyexchange.models;

import java.io.Serializable;

public class ExchangeModel implements Serializable {

    private int customerAmount;
    private String currencyName;

    public ExchangeModel(int customerAmount, String currencyName) {
        this.customerAmount = customerAmount;
        this.currencyName = currencyName;
    }

    public int getCustomerAmount() {
        return customerAmount;
    }

    public String getCurrencyName() {
        return currencyName;
    }
}
