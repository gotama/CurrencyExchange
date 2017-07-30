package com.gautamastudios.currencyexchange.models;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.List;

public class SupportedCurrenciesModel implements Serializable {

    public SupportedCurrenciesModel() {
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String currencyCode;

    @DatabaseField
    private long timestamp;

    private List<RatesModel> rates;

    public SupportedCurrenciesModel(String currencyCode, long timestamp, List<RatesModel> rates) {
        this.currencyCode = currencyCode;
        this.timestamp = timestamp;
        this.rates = rates;
    }

    public int getId() {
        return id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<RatesModel> getRates() {
        return rates;
    }

    public void setRates(List<RatesModel> rates) {
        this.rates = rates;
    }
}
