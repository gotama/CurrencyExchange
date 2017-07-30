package com.gautamastudios.currencyexchange.models;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class RatesModel implements Serializable {

    public RatesModel() {
    }

    public RatesModel(int currencyId, String currencyCode, int rate) {
        this.currencyId = currencyId;
        this.currencyCode = currencyCode;
        this.rate = rate;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(uniqueCombo = true)
    private int currencyId;

    @DatabaseField(uniqueCombo = true)
    private String currencyCode;

    @DatabaseField
    private int rate;

    public int getId() {
        return id;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public int getRate() {
        return rate;
    }
}
